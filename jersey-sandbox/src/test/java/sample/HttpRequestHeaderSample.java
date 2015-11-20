package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class HttpRequestHeaderSample extends JerseyTest {

    @Test
    public void test() throws Exception {
        test("foo", "bar", "HelloWorld");
        test("FOO", "bar", "HelloWorld");
        test("foo", "bar", "HelloWorld");
        test("foo", "BAR", "Hello");
    }

    private void test(String header, String cookie, String expected) {
        String response = target().request().header(header, "Hello")
                .cookie(cookie, "World").get(String.class);
        assertThat(response, is(expected));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Sample.class);
    }

    @Path("")
    public static class Sample {
        @GET
        public String header(@HeaderParam("foo") @DefaultValue("") String foo,
                @CookieParam("bar") @DefaultValue("") String bar) {
            return foo + bar;
        }
    }
}
