package local.yunhua.proxypool.task;

import local.yunhua.proxypool.dao.IProxyDAO;
import local.yunhua.proxypool.domain.ProxyPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PersistentProxy {

    private static final Logger logger = LoggerFactory.getLogger(PersistentProxy.class);


    @Resource
    private IProxyDAO proxyDAO;

    @Resource
    private ProxyPool proxyPool;


    @Scheduled(fixedRate = 1000 * 25)
    @Async
    public void run() {
        int updated = proxyDAO.createMany(proxyPool.getProxies());
        proxyPool.clear();
        logger.info(String.format("Created #%d proxies.", updated));
    }


}
