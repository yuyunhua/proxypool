package local.yunhua.proxypool.dao.impl;

import local.yunhua.proxypool.dao.IProxyDAO;
import local.yunhua.proxypool.domain.Proxy;
import local.yunhua.proxypool.util.ProxyHelper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

@Repository
public class ProxyDAO implements IProxyDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProxyDAO.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private ProxyHelper proxyHelper;


    @Override
    public int create(Proxy proxy) {
        try {
            redisTemplate.opsForHash().putAll(proxyHelper.getProxyIdentifier(proxy), proxyHelper.toMap(proxy));
            return 1;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
        }
        return 0;
    }

    @Override
    public Proxy retrieveOne(Proxy proxy) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(proxyHelper.getProxyIdentifier(proxy));
        return proxyHelper.getProxy(map);
    }

    @Override
    public int delete(Proxy proxy) {
        redisTemplate.delete(proxyHelper.getProxyIdentifier(proxy));
        return 0;
    }

    @Override
    public int createMany(List<Proxy> proxies) {
        int count = 0;
        for (Proxy proxy : proxies) {
            count += create(proxy);
        }
        return count;
    }

    @Override
    public List<Proxy> retrieveAll() {
        Set<String> keys = redisTemplate.keys("proxy_*");
        List<Proxy> proxyList = new ArrayList<>();
        keys.forEach(key -> {
            Object[] proxyKeys = redisTemplate.opsForHash().keys(key).toArray();
            List<Object> proxyValues = redisTemplate.opsForHash().values(key);
            Map<Object, Object> map = new HashMap<>();
            int len = proxyKeys.length;
            for (int index = 0; index < len; index++) {
                map.put(proxyKeys[index], proxyValues.get(index));
            }
            Proxy proxy = proxyHelper.getProxy(map);
            proxyList.add(proxy);
        });

        return proxyList;
    }
}
