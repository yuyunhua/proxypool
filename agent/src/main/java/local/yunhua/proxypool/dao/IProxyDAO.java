package local.yunhua.proxypool.dao;

import local.yunhua.proxypool.domain.Proxy;

import java.util.List;

public interface IProxyDAO {

    int create(Proxy proxy);

    int createMany(List<Proxy> proxies);

    Proxy retrieveOne(Proxy proxy);

    int delete(Proxy proxy);

    List<Proxy> retrieveAll();


}
