package cc.xfl12345.mybigdata;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cc.xfl12345.mybigdata.listener.ContextFinalizer;

public class TestSpringAppLoad {

    public static AbstractApplicationContext applicationContext;

    public static void main(String[] args) throws Exception {
        System.out.println("Classpath root="+Thread.currentThread().getContextClassLoader().getResource("").getPath());

        TestInitSlf4jAndLog4jOutsideWebApp.main(null);

        applicationContext = new ClassPathXmlApplicationContext("spring/application_context.xml");
//        applicationContext.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Begin to shutdown ... ");
            ContextFinalizer contextFinalizer = StaticSpringApp.getBean(ContextFinalizer.class);
            contextFinalizer.contextDestroyed(null);
            System.out.println("App stopped,bye bye!");
        }));
    }
}
