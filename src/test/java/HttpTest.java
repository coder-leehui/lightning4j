import com.hql.lightning.core.ServerInit;
import com.hql.lightning.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * http测试
 */
public class HttpTest {

    public static void main(String[] args) throws Exception {

        ServerInit.getInstance().initConfPath("lightning");
        ServerInit.getInstance().initLog4j();

        for (int i = 0; i < 100; i++) {
            new Thread(new worker()).start();
        }
    }

    static class worker implements Runnable {
        public void run() {
            Map<String, String> param = new HashMap<>();
            param.put("a", "is a");
            param.put("b", "is b");

            try {
                String getData = HttpUtil.getInstance().doPost("http://127.0.0.1/test.php", param);
                System.out.println(getData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
