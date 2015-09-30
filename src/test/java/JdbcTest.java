import com.hql.lightning.util.jdbc.JdbcHelper;

/**
 * jdbc测试
 */
public class JdbcTest {

    public static void main(String[] args) throws Exception {
        //System.out.println(JdbcHelper.insertWithReturnPrimeKey("INSERT INTO test (id ,name) VALUES(0,'lee')"));
        //System.out.println(JdbcHelper.query("select * from skill limit 2"));
        //System.out.println(JdbcHelper.query("select * from itemtype limit 2"));

        for (int i = 0; i < 500; i++) {
            new Thread(new worker()).start();
        }

        for (int i = 0; i < 500; i++) {
            new Thread(new worker2()).start();
        }
    }

    static class worker implements Runnable {
        public void run() {
            try {
                System.out.println(JdbcHelper.insertWithReturnPrimeKey("INSERT INTO test (id ,name) VALUES(0,'lee')"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class worker2 implements Runnable {
        public void run() {
            try {
                System.out.println(JdbcHelper.query("select * from skill limit 2"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
