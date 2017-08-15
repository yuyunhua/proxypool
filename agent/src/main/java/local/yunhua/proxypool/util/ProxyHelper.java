package local.yunhua.proxypool.util;

import local.yunhua.proxypool.domain.Proxy;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProxyHelper {

    @Value("proxy.pool.test.url")
    private String url;

    private final Logger logger = LoggerFactory.getLogger(ProxyHelper.class);

    public String getProxyIdentifier(Proxy proxy) {
        if (proxy == null || proxy.getHost() == null) {
            return null;
        }
        return String.format("proxy_%s:%d", proxy.getHost(), proxy.getPort());
    }

    public boolean isAvailable(Proxy proxy) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;

        HttpHost httpProxy = new HttpHost(proxy.getHost(), proxy.getPort());

        RequestConfig config = RequestConfig.custom()
                .setProxy(httpProxy)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);

        try {
            response = httpClient.execute(httpGet);
            httpClient.close();
            response.close();
        } catch (IOException e) {
            logger.error(String.format("Invalid proxy: %s", this.toString()));
            return false;
        }
        return true;

    }


    public Proxy getProxy(Map<Object, Object> map) {
        if (map == null || map.isEmpty() || !map.containsKey("host") || !map.containsKey("port")) {
            return null;
        }

        Proxy proxy = new Proxy();

        Object value = map.get("host");
        if (value != null) {
            proxy.setHost(value.toString());
        }

        value = map.get("port");
        if (value != null) {
            proxy.setPort(Integer.valueOf(value.toString()));
        }

        value = map.get("protocol");
        if (value != null) {
            proxy.setProtocol(value.toString());
        }

        value = map.get("latency");
        if (value != null) {
            proxy.setLatency(Integer.valueOf(value.toString()));
        }

        return proxy;
    }


    public Map<String, String> toMap(Proxy proxy) {
        Map<String, String> map = new HashMap<>(8);
        map.put("host", proxy.getHost());
        map.put("port", String.valueOf(proxy.getPort()));
        map.put("protocol", proxy.getProtocol());
        map.put("latency", String.valueOf(proxy.getLatency()));
        return map;
    }


}
