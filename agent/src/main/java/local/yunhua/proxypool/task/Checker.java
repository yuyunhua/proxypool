package local.yunhua.proxypool.task;

import local.yunhua.proxypool.dao.IProxyDAO;
import local.yunhua.proxypool.domain.Proxy;
import local.yunhua.proxypool.util.ProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class Checker {

    private static Logger logger = LoggerFactory.getLogger(Checker.class);

    @Resource
    private IProxyDAO proxyDAO;

    @Resource
    private ProxyHelper proxyHelper;

    @Scheduled(fixedRate = 1000 * 60)
    @Async
    public void run() {
        List<Proxy> proxies = proxyDAO.retrieveAll();
        proxies.forEach(proxy -> {
            if (!proxyHelper.isAvailable(proxy)) {
                proxyDAO.delete(proxy);
            }
        });

    }
}
