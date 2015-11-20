package async;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class LongTimeLargeDataSampleTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        System.in.read();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(LongTimeLargeDataSample.class,
                AsyncLongTimeLargeDataSample.class);
    }
}
