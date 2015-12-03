package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class JerseyListenerSample extends JerseyTest {

    @Test
    public void test() throws Exception {
        String response = target("sample").request().get(String.class);
        assertThat(response, is("hello"));
    }

    @Test
    public void test_exception() throws Exception {
        Response response = target("sample/exception").request().get();
        assertThat(response.getStatus(),
                is(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Sample.class,
                SampleApplicationEventListener.class);
    }

    @Path("sample")
    public static class Sample {

        @GET
        public String get() {
            return "hello";
        }

        @Path("exception")
        @GET
        public String exception() {
            throw new RuntimeException();
        }
    }

    public static class SampleApplicationEventListener
            implements ApplicationEventListener {

        @Override
        public void onEvent(ApplicationEvent event) {
            System.out.printf("+++ %s +++%n", event.getType());
        }

        @Override
        public RequestEventListener onRequest(RequestEvent requestEvent) {
            System.out.printf("### %s ###%n", requestEvent.getType());
            return new SampleRequestEventListener();
        }
    }

    public static class SampleRequestEventListener
            implements RequestEventListener {

        @Override
        public void onEvent(RequestEvent event) {
            System.out.printf("*** %s ***%n", event.getType());
        }
    }
}
