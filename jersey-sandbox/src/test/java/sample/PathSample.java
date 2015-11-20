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

public class PathSample extends JerseyTest {

    @Test
    public void testFoo() throws Exception {
        assertThat(target("foo").request().get(String.class), is("foo"));
    }

    @Test
    public void testBar() throws Exception {
        assertThat(target("foo/bar").request().get(String.class), is("bar"));
    }

    @Test
    public void testBaz() throws Exception {
        assertThat(target("foo/hello").request().get(String.class),
                is("bazhello"));
    }

    @Test
    public void testQux() throws Exception {
        assertThat(target("foo/123").request().get(String.class), is("qux123"));
    }

    @Test
    public void testHello() throws Exception {
        assertThat(target("hello/world").request().get(String.class),
                is("Hello, world!"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Sample.class, Hello.class);
    }

    @Path("foo")
    public static class Sample {

        @GET
        public String foo() {
            return "foo";
        }

        @GET
        @Path("bar")
        public String bar() {
            return "bar";
        }

        @GET
        @Path("{baz}")
        public String baz(@PathParam("baz") String baz) {
            return "baz" + baz;
        }

        @GET
        @Path("{qux:\\d+}")
        public String qux(@PathParam("qux") String qux) {
            return "qux" + qux;
        }
    }

    @Path("hello/{name}")
    public static class Hello {
        @GET
        public String say(@PathParam("name") String name) {
            return "Hello, " + name + "!";
        }
    }
}
