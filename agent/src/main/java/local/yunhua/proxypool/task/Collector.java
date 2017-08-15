package local.yunhua.proxypool.task;

import local.yunhua.proxypool.domain.Proxy;
import local.yunhua.proxypool.domain.ProxyPool;
import local.yunhua.proxypool.util.HttpHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class Collector {

    private static Logger logger = LoggerFactory.getLogger(Collector.class);

    @Resource
    private ProxyPool proxyPool;

    @Resource
    private HttpHelper httpHelper;

    @Scheduled(fixedRate = 1000 * 30)
    @Async
    public void run() {
        List<Proxy> proxies = new ArrayList<>();
        String url;
        for (int index = 0; index < 5; index++) {
            url = String.format("http://www.xicidaili.com/nn/%d", index + 1);
            proxies.addAll(getProxiesFromUrl(url));
            logger.info(String.format("Get #%d new proxies.", proxies.size()));
        }
        proxyPool.addProxies(proxies);
    }


    private List<Proxy> getProxiesFromUrl(String url) {
        String html = httpHelper.get(url);

        List<Proxy> proxies = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements elements = document.select("table[id=ip_list] tbody tr");
        elements.forEach(element -> {

            String host;
            String port;
            String location;
            String type;
            String protocol;
            String latency;
            String delay;

            try {
                host = element.select("td:nth-child(2)").get(0).text();
                port = element.select("td:nth-child(3)").get(0).text();
                location = element.select("td:nth-child(4)").get(0).text();
                type = element.select("td:nth-child(5)").get(0).text();
                protocol = element.select("td:nth-child(6)").get(0).text();
                latency = element.select("td:nth-child(7) div[class=bar]").get(0).attr("title").trim();
                delay = element.select("td:nth-child(8) div[class=bar]").get(0).attr("title").trim();

            } catch (IndexOutOfBoundsException e) {
                return;
            }

            latency = latency.replace(".", "").replace("秒", "");
            delay = latency.replace(".", "").replace("秒", "");
            Proxy proxy = new Proxy();


            proxy.setHost(host);
            proxy.setPort(Integer.valueOf(port));
            proxy.setProtocol(protocol);
            proxy.setLatency(Integer.valueOf(latency));
            proxies.add(proxy);

        });
        return proxies;
    }
}
