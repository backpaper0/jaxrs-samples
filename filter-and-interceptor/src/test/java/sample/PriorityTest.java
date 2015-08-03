package sample;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

/**
 * {@link Priority}を使った
 * {@link ContainerRequestFilter}の適用順を確認する。
 *
 */
public class PriorityTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        target("test").request().get();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(TestResource.class, C.class, B.class, A.class);
    }

    @Priority(-1)
    public static class A implements ContainerRequestFilter {
        @Override
        public void filter(ContainerRequestContext requestContext)
                throws IOException {
            System.err.println(getClass().getSimpleName());
        }
    }

    @Priority(0)
    public static class B implements ContainerRequestFilter {
        @Override
        public void filter(ContainerRequestContext requestContext)
                throws IOException {
            System.err.println(getClass().getSimpleName());
        }
    }

    @Priority(1)
    public static class C implements ContainerRequestFilter {
        @Override
        public void filter(ContainerRequestContext requestContext)
                throws IOException {
            System.err.println(getClass().getSimpleName());
        }
    }

    @Path("test")
    public static class TestResource {
        @GET
        public String get() {
            return "test";
        }
    }
}
