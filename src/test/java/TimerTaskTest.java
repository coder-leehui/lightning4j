import com.hql.lightning.util.TimerTaskUtil;

import java.util.concurrent.TimeUnit;

/**
 * 定时任务测试
 */
public class TimerTaskTest {

    public static void main(String[] args) throws Exception {

        TimerTaskUtil.getInstance().run(new TimerTest(), 1, TimeUnit.SECONDS);
    }
}

class TimerTest implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println("timer worked.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
