package sample;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

/**
 * {@link ContainerRequestFilter}、
 * {@link ContainerResponseFilter}、
 * {@link ReaderInterceptor}、
 * {@link WriterInterceptor}、
 * リソースメソッドの実行順序を確認する。
 *
 * (めっちゃ見づらいけど)コンソールを見たら実行順序が分かるはず！めっちゃ見づらいけど！
 */
public class OrderTest extends JerseyTest {

    @Test
    public void test() throws Exception {

        System.err.println("はじめ！");

        String response = target("test").request().post(
                Entity.text("フォーエバー(～ 'ω' )～"), String.class);
        assertThat(response, is("フォーエバー(～ 'ω' )～"));

        System.err.println("もう一回！");

        response = target("test").request().post(Entity.text("㌰㌰⋋( 'Θ')⋌㌰㌰"),
                String.class);
        assertThat(response, is("㌰㌰⋋( 'Θ')⋌㌰㌰"));

        System.err.println("おわり！");
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(TestResource.class,
                TestPreMatchingContainerRequestFilter.class,
                TestContainerRequestFilter.class,
                TestContainerResponseFilter.class, TestReaderInterceptor.class,
                TestWriterInterceptor.class);
    }

    interface Logging {
        default void log(String message) {
            System.err.printf("[%1$s@%2$x] %3$s%n", getClass().getSimpleName(),
                    hashCode(), message);
        }
    }

    @PreMatching
    public static class TestPreMatchingContainerRequestFilter implements
            ContainerRequestFilter, Logging {

        public TestPreMatchingContainerRequestFilter() {
            log("<init>");
        }

        @Override
        public void filter(ContainerRequestContext requestContext)
                throws IOException {
            log("filter");
        }
    }

    public static class TestContainerRequestFilter implements
            ContainerRequestFilter, Logging {

        public TestContainerRequestFilter() {
            log("<init>");
        }

        @Override
        public void filter(ContainerRequestContext requestContext)
                throws IOException {
            log("filter");
        }
    }

    public static class TestContainerResponseFilter implements
            ContainerResponseFilter, Logging {

        public TestContainerResponseFilter() {
            log("<init>");
        }

        @Override
        public void filter(ContainerRequestContext requestContext,
                ContainerResponseContext responseContext) throws IOException {
            log("ContainerResponseFilter");
        }
    }

    public static class TestReaderInterceptor implements ReaderInterceptor,
            Logging {

        public TestReaderInterceptor() {
            log("<init>");
        }

        @Override
        public Object aroundReadFrom(ReaderInterceptorContext context)
                throws IOException, WebApplicationException {
            log("aroundReadFrom");
            return context.proceed();
        }
    }

    public static class TestWriterInterceptor implements WriterInterceptor,
            Logging {

        public TestWriterInterceptor() {
            log("<init>");
        }

        @Override
        public void aroundWriteTo(WriterInterceptorContext context)
                throws IOException, WebApplicationException {
            log("aroundWriteTo");
            context.proceed();
        }
    }

    @Path("test")
    public static class TestResource implements Logging {

        public TestResource() {
            log("<init>");
        }

        @POST
        @Consumes("text/plain; charset=UTF-8")
        @Produces("text/plain; charset=UTF-8")
        public String passThrough(String s) {
            log("resource method");
            return s;
        }
    }
}
