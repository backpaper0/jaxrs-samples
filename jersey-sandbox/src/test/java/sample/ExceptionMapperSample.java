package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class ExceptionMapperSample extends JerseyTest {

    @Test
    public void test() throws Exception {
        String response = target("sample").request().post(Entity.text("hello"),
                String.class);
        assertThat(response, is("hello"));
    }

    @Test
    public void testResource() throws Exception {
        String response = target("sample").request().header("fire", "resource")
                .post(Entity.text("hello"), String.class);
        assertThat(response, is("resource"));
    }

    @Test
    public void testContainerRequestFilter() throws Exception {
        String name = ContainerRequestFilter.class.getSimpleName();
        String response = target("sample").request().header("fire", name)
                .post(Entity.text("hello"), String.class);
        assertThat(response, is(name));
    }

    @Test
    public void testContainerResponseFilter() throws Exception {
        String name = ContainerResponseFilter.class.getSimpleName();
        String response = target("sample").request().header("fire", name)
                .post(Entity.text("hello"), String.class);
        assertThat(response, is(name));
    }

    @Test
    public void testReaderInterceptor() throws Exception {
        String name = ReaderInterceptor.class.getSimpleName();
        String response = target("sample").request().header("fire", name)
                .post(Entity.text("hello"), String.class);
        assertThat(response, is(name));
    }

    @Test
    public void testWriterInterceptor() throws Exception {
        //Attempt to release request processing resources has failed for a request.
        //java.lang.IllegalStateException: Illegal attempt to call getOutputStream() after getWriter() has already been called.

        String name = WriterInterceptor.class.getSimpleName();
        Response response = target("sample").request().header("fire", name)
                .post(Entity.text("hello"), Response.class);
        assertThat(response.getStatus(), is(500));
    }

    @Test
    public void testMessageBodyReader() throws Exception {
        String name = MessageBodyReader.class.getSimpleName();
        String response = target("sample").request().header("fire", name)
                .post(Entity.text("hello"), String.class);
        assertThat(response, is(name));
    }

    @Test
    public void testMessageBodyWriter() throws Exception {
        String name = MessageBodyWriter.class.getSimpleName();
        String response = target("sample").request().header("fire", name)
                .post(Entity.text("hello"), String.class);
        assertThat(response, is(name));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(SampleExceptionMapper.class,
                SampleResource.class, SampleProvider.class);
    }

    public static class SampleException extends RuntimeException {

        public SampleException(String message) {
            super(message);
        }
    }

    public static class SampleExceptionMapper
            implements ExceptionMapper<SampleException> {

        @Override
        public Response toResponse(SampleException exception) {
            return Response.ok(exception.getMessage()).header("fired", true)
                    .build();
        }
    }

    @Path("sample")
    public static class SampleResource {
        @POST
        public Sample echo(Sample entity, @HeaderParam("fire") String fire) {
            if (Objects.equals(fire, "resource")) {
                throw new SampleException("resource");
            }
            return entity;
        }
    }

    public static class SampleProvider implements ContainerRequestFilter,
            ContainerResponseFilter, ReaderInterceptor, WriterInterceptor,
            MessageBodyReader<Sample>, MessageBodyWriter<Sample> {

        @Context
        private HttpHeaders headers;

        private void fire(Class<?> clazz) {
            String name = clazz.getSimpleName();
            if (Objects.equals(headers.getHeaderString("fire"), name)) {
                throw new SampleException(name);
            }
        }

        @Override
        public boolean isWriteable(Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType) {
            return true;
        }

        @Override
        public long getSize(Sample t, Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType) {
            return -1;
        }

        @Override
        public void writeTo(Sample t, Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType,
                MultivaluedMap<String, Object> httpHeaders,
                OutputStream entityStream)
                        throws IOException, WebApplicationException {
            fire(MessageBodyWriter.class);
            entityStream.write(t.getValue().getBytes());
        }

        @Override
        public boolean isReadable(Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType) {
            return true;
        }

        @Override
        public Sample readFrom(Class<Sample> type, Type genericType,
                Annotation[] annotations, MediaType mediaType,
                MultivaluedMap<String, String> httpHeaders,
                InputStream entityStream)
                        throws IOException, WebApplicationException {
            fire(MessageBodyReader.class);
            return new Sample(
                    new BufferedReader(new InputStreamReader(entityStream))
                            .lines().collect(Collectors.joining()));
        }

        @Override
        public void aroundWriteTo(WriterInterceptorContext context)
                throws IOException, WebApplicationException {
            fire(WriterInterceptor.class);
            context.proceed();
        }

        @Override
        public Object aroundReadFrom(ReaderInterceptorContext context)
                throws IOException, WebApplicationException {
            fire(ReaderInterceptor.class);
            return context.proceed();
        }

        @Override
        public void filter(ContainerRequestContext requestContext,
                ContainerResponseContext responseContext) throws IOException {
            if (Boolean.parseBoolean(
                    responseContext.getHeaderString("fired")) == false) {
                fire(ContainerResponseFilter.class);
            }
        }

        @Override
        public void filter(ContainerRequestContext requestContext)
                throws IOException {
            fire(ContainerRequestFilter.class);
        }
    }

    static class Sample {
        private final String value;

        public Sample(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
