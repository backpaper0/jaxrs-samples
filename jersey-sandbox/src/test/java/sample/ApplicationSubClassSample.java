package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class ApplicationSubClassSample extends JerseyTest {

    @Test
    public void test() throws Exception {
        String response = target("/foo").request().get(String.class);
        assertThat(response, is("foo"));
    }

    @Override
    protected Application configure() {
        return new FooApplication();
    }

    public static class FooApplication extends Application {
        private final Set<Class<?>> classes = new HashSet<>();

        public FooApplication() {
            classes.add(Foo.class);
        }

        @Override
        public Set<Class<?>> getClasses() {
            return classes;
        }
    }

    @Path("/foo")
    public static class Foo {
        @GET
        public String get() {
            return "foo";
        }
    }
}
