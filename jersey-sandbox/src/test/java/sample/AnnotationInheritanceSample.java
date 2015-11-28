package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class AnnotationInheritanceSample extends JerseyTest {

    @Test
    public void test() throws Exception {
        String response = target("hello").queryParam("name", "world").request()
                .get(String.class);
        assertThat(response, is("Hello, world!"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(HelloImpl.class);
    }

    @Path("hello")
    public interface Hello {
        @GET
        @Produces(MediaType.TEXT_PLAIN)
        String say(@QueryParam("name") String name);
    }

    public static class HelloImpl implements Hello {

        @Override
        public String say(String name) {
            return String.format("Hello, %s!", name);
        }
    }
}
