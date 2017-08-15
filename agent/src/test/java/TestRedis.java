import local.yunhua.proxypool.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestRedis {

    private static final Logger logger = LoggerFactory.getLogger(TestRedis.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    public void testHash() {
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        redisTemplate.opsForHash().putAll("h1", map);

        List<Object> list = redisTemplate.opsForHash().values("h1");
        logger.info(list.toString());

        list = redisTemplate.opsForHash().multiGet("h1", redisTemplate.opsForHash().keys("h1"));
        logger.info(list.toString());

        Map<Object, Object> map2 = redisTemplate.opsForHash().entries("h1");
        logger.info(map2.toString());
        logger.info(String.valueOf(map2.get("two") == null));

    }
}
