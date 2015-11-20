package validation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class AdditionTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        int result = target("addition").queryParam("a", 1).queryParam("b", 2)
                .request().get(int.class);
        assertThat(result, is(3));
    }

    @Test
    public void testNullA() throws Exception {
        Response response = target("addition").queryParam("b", 2).request()
                .get();
        assertThat(response.getStatus(),
                is(Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void testNullB() throws Exception {
        Response response = target("addition").queryParam("a", 1).request()
                .get();
        assertThat(response.getStatus(),
                is(Status.BAD_REQUEST.getStatusCode()));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Addition.class);
    }
}
