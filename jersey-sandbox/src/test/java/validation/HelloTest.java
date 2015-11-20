package validation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class HelloTest extends JerseyTest {

    @Test
    public void testValid() throws Exception {
        String response = target("hello").queryParam("name", "world").request()
                .get(String.class);
        assertThat(response, is("Hello, world!"));
    }

    @Test
    public void testNull() throws Exception {
        Response response = target("hello").request().get();
        assertThat(response.getStatus(),
                is(Status.BAD_REQUEST.getStatusCode()));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Hello.class);
    }
}
