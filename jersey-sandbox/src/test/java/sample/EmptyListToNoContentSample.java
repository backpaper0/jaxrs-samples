package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class EmptyListToNoContentSample extends JerseyTest {

    @Test
    public void testEmpty() throws Exception {
        Response response = target("sample").queryParam("size", 0).request()
                .get();
        assertThat(response.getStatus(), is(Status.NO_CONTENT.getStatusCode()));
        assertThat(response.hasEntity(), is(false));
    }

    @Test
    public void testOne() throws Exception {
        Response response = target("sample").queryParam("size", 1).request()
                .get();
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
        assertThat(response.readEntity(String.class), is("[0]"));
    }

    @Test
    public void testMany() throws Exception {
        Response response = target("sample").queryParam("size", 10).request()
                .get();
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
        assertThat(response.readEntity(String.class),
                is("[0,1,2,3,4,5,6,7,8,9]"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Sample.class, ListProvider.class,
                EmptyListToNoContentProvider.class);
    }

    @Provider
    public static class EmptyListToNoContentProvider
            implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext requestContext,
                ContainerResponseContext responseContext) throws IOException {
            Object entity = responseContext.getEntity();
            if (entity instanceof List && ((List<?>) entity).isEmpty()) {
                responseContext.setStatus(204);
            }
        }
    }

    @Path("sample")
    public static class Sample {

        @GET
        @Produces(MediaType.TEXT_PLAIN)
        public List<Integer> get(@QueryParam("size") int size) {
            return IntStream.range(0, size).boxed()
                    .collect(Collectors.toList());
        }
    }

    @Provider
    @Produces(MediaType.TEXT_PLAIN)
    public static class ListProvider
            implements MessageBodyWriter<List<Integer>> {

        @Override
        public boolean isWriteable(Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType) {
            return true;
        }

        @Override
        public long getSize(List<Integer> t, Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType) {
            return -1;
        }

        @Override
        public void writeTo(List<Integer> t, Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType,
                MultivaluedMap<String, Object> httpHeaders,
                OutputStream entityStream)
                        throws IOException, WebApplicationException {
            entityStream.write(t.stream().map(Object::toString)
                    .collect(Collectors.joining(",", "[", "]")).getBytes());
        }
    }
}
