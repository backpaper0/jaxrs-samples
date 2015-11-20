package order;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import order.ContainerRequestFilterImpl.ContainerRequestFilter1;
import order.ContainerRequestFilterImpl.ContainerRequestFilter2;
import order.ContainerRequestFilterImpl.ContainerRequestFilter3;
import order.ContainerRequestFilterImpl.PreMatchingContainerRequestFilter1;
import order.ContainerRequestFilterImpl.PreMatchingContainerRequestFilter2;
import order.ContainerRequestFilterImpl.PreMatchingContainerRequestFilter3;
import order.ContainerResponseFilterImpl.ContainerResponseFilter1;
import order.ContainerResponseFilterImpl.ContainerResponseFilter2;
import order.ContainerResponseFilterImpl.ContainerResponseFilter3;
import order.ReaderInterceptorImpl.ReaderInterceptor1;
import order.ReaderInterceptorImpl.ReaderInterceptor2;
import order.ReaderInterceptorImpl.ReaderInterceptor3;
import order.WriterInterceptorImpl.WriterInterceptor1;
import order.WriterInterceptorImpl.WriterInterceptor2;
import order.WriterInterceptorImpl.WriterInterceptor3;

public class OrderTest extends JerseyTest {

    @Test
    public void test() {
        String response = target().request().post(Entity.text("hello"),
                String.class);
        assertThat(response, is("hello"));
        ResourceClass.printLogs();
    }

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig();
        config.register(ContainerRequestFilter1.class);
        config.register(ContainerRequestFilter2.class);
        config.register(ContainerRequestFilter3.class);
        config.register(PreMatchingContainerRequestFilter1.class);
        config.register(PreMatchingContainerRequestFilter2.class);
        config.register(PreMatchingContainerRequestFilter3.class);
        config.register(ContainerResponseFilter1.class);
        config.register(ContainerResponseFilter2.class);
        config.register(ContainerResponseFilter3.class);
        config.register(ReaderInterceptor1.class);
        config.register(ReaderInterceptor2.class);
        config.register(ReaderInterceptor3.class);
        config.register(WriterInterceptor1.class);
        config.register(WriterInterceptor2.class);
        config.register(WriterInterceptor3.class);
        config.register(MessageBodyReaderImpl.class);
        config.register(MessageBodyWriterImpl.class);
        config.register(ResourceClass.class);
        return config;
    }
}
