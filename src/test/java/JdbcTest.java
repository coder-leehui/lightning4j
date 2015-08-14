import com.hql.lightning.util.jdbc.JdbcHelper;

/**
 * jdbc测试
 */
public class JdbcTest {

    public static void main(String[] args) throws Exception {
        System.out.println(JdbcHelper.query("select * from skill limit 2"));
        System.out.println(JdbcHelper.query("select * from itemtype limit 2"));
    }
}
