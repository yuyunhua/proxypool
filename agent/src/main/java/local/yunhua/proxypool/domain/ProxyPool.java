package local.yunhua.proxypool.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class ProxyPool {

    private final List<Proxy> proxies = new ArrayList<>();
    private final SortedMap<String, Proxy> proxySet = new TreeMap<>();

    public void addProxy(Proxy proxy) {
        synchronized (this.proxies) {
            proxies.add(proxy);
        }
    }

    public void addProxies(List<Proxy> newProxies) {
        synchronized (this.proxies) {
            proxies.addAll(newProxies);
        }
    }

    public void deleteProxy(Proxy proxy) {
        synchronized (this.proxies) {
            proxies.remove(proxy);
        }
    }

    public List<Proxy> getProxies() {
        synchronized (this.proxies) {
            return proxies;
        }
    }

    public void clear() {
        synchronized (this.proxies) {
            proxies.clear();
        }
    }

}
