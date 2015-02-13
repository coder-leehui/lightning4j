import com.hql.lightning.buffer.GameUpBuffer;
import com.hql.lightning.core.*;
import com.hql.lightning.util.ModuleUtil;
import com.hql.lightning.util.TimerTaskUtil;

import java.util.concurrent.TimeUnit;

/**
 * server测试
 */
public class ServiceTest {

    public void run() throws Exception {
        ServerInit.getInstance().initConfPath("lightning");
        ServerInit.getInstance().initLog4j();
        ServerInit.getInstance().initGameWorkers();
        ServerInit.getInstance().initModules();

        TimerTaskUtil.getInstance().run(new AutoUpdateTest(), 10, TimeUnit.SECONDS);

        GameBoss.getInstance().boot(new GameUpProcessor() {
            @Override
            public void process(GameUpBuffer buffer) {
                GameWorkerManager.getInstance().pushDataToWorker(buffer);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new ServiceTest().run();
    }
}

/**
 * 自动更新测试
 */
class AutoUpdateTest implements Runnable {

    private boolean isUpdated = true;

    private boolean isFinish = false;

    @Override
    public void run() {
        try {
            if (!isUpdated && !isFinish) {
                System.out.println("start update module...");
                ModuleUtil.getInstance().updateModule("test2");
                System.out.println("update module finished, now modules:");
                System.out.println(ModuleUtil.getInstance().getModuleInfo());
                isFinish = true;
            }
            isUpdated = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


