import com.hql.lightning.util.StackTraceUtil;

/**
 * Created by Administrator on 2015/8/5.
 */
public class StackTraceTest {

    public static void main(String[] args) {
        try {
            StackTraceTest.test();
        } catch (Exception e) {
            System.out.println(StackTraceUtil.getStackTrace(e));
        }
    }

    private static void test() throws Exception{
        throw new Exception("test exception");
    }
}
