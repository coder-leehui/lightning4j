import com.hql.lightning.core.ServerInit;
import com.hql.lightning.util.JedisUtil;
import com.hql.lightning.util.ProReaderUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

/**
 * Redis功能测试
 */
public class RedisTest {

    private static Logger logger = Logger.getLogger(RedisTest.class);

    public static void main(String[] args) throws Exception {
        ServerInit.getInstance().initConfPath("lightning");
        ServerInit.getInstance().initLog4j();

        Jedis jedis = JedisUtil.getInstance().getJedis(ProReaderUtil.getInstance().getRedisPro().get("host"),
                Integer.parseInt(ProReaderUtil.getInstance().getRedisPro().get("port")));

        jedis.set("testKey", "hello");

        HashMap<String, String> h = new HashMap<String, String>();
        h.put("a", "aa");
        h.put("b", "bb");
        jedis.hmset("testKey2", h);

        logger.info(jedis.get("testKey"));
        logger.info(jedis.hget("testKey2", "a"));
    }
}
