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

        Map<String, String> param = new HashMap<>();
        param.put("a", "is a");
        param.put("b", "is b");

        Map<String, String> param2 = new HashMap<>();
        param2.put("a", "is a2");
        param2.put("b", "is b2");

        String getData = HttpUtil.doGet("http://127.0.0.1/test.php", param);
        System.out.println(getData);
        System.out.println("----------------------分割线-----------------------");
        String postData = HttpUtil.doPost("http://127.0.0.1/test.php", param2);
        System.out.println(postData);
    }
}
