import com.hql.lightning.buffer.GameUpBuffer;
import com.hql.lightning.core.*;
import com.hql.lightning.handler.GameHandlerManager;
import com.hql.test.handler.*;

/**
 * server测试
 */
public class ServiceTest {

    public void run() throws Exception {
        ServerInit.getInstance().initConfPath("lightning");
        ServerInit.getInstance().initLog4j();
        ServerInit.getInstance().initGameWorkers();

        GameBoss.getInstance().boot(new GameUpProcessor() {
            @Override
            public void process(GameUpBuffer buffer) {
                GameWorkerManager.getInstance().pushDataToWorker(buffer);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        GameHandlerManager.getInstance().register(HandlerTest.class, "test");
        GameHandlerManager.getInstance().register(DisconnectHandlerTest.class, "onDisconnect");
        new ServiceTest().run();
    }
}


