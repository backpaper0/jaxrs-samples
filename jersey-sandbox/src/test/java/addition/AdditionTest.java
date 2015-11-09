package addition;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class AdditionTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        int result = target("/addition").queryParam("a", 1).queryParam("b", 2)
                .request().get(int.class);
        assertThat(result, is(3));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Addition.class);
    }
}
