package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class GuiceSample extends JerseyTest {

    @Test
    public void test() throws Exception {
        String s = target("tests/world").request().get(String.class);
        assertThat(s, is("hello world"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(TestApi.class);
    }

    @Path("tests/{id}")
    public static class TestApi {

        @PathParam("id")
        private String id;

        @GET
        public String get() {
            return "hello " + id;
        }
    }
}
