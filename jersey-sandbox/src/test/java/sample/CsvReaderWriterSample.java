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
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class CsvReaderWriterSample extends JerseyTest {

    @Test
    public void testRead() throws Exception {
        String s = target("csv").request()
                .post(Entity.entity("1,foo\n2,bar", "text/csv"), String.class);
        assertThat(s, is("1,foo\n2,bar"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Csv.class, CsvReader.class, CsvWriter.class);
    }

    @Path("/csv")
    public static class Csv {
        @POST
        @Consumes("text/csv")
        @Produces("text/csv")
        public String[][] post(String[][] csv) {
            return csv;
        }
    }

    @Consumes("text/csv")
    public static class CsvReader implements MessageBodyReader<String[][]> {

        @Override
        public boolean isReadable(Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType) {
            return true;
        }

        @Override
        public String[][] readFrom(Class<String[][]> type, Type genericType,
                Annotation[] annotations, MediaType mediaType,
                MultivaluedMap<String, String> httpHeaders,
                InputStream entityStream)
                        throws IOException, WebApplicationException {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(entityStream));
            return in.lines().map(line -> line.split(","))
                    .toArray(String[][]::new);
        }
    }

    @Produces("text/csv")
    public static class CsvWriter implements MessageBodyWriter<String[][]> {

        public CsvWriter() {
            new Throwable().printStackTrace();
        }

        @Override
        public boolean isWriteable(Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType) {
            return true;
        }

        @Override
        public long getSize(String[][] t, Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType) {
            return -1;
        }

        @Override
        public void writeTo(String[][] t, Class<?> type, Type genericType,
                Annotation[] annotations, MediaType mediaType,
                MultivaluedMap<String, Object> httpHeaders,
                OutputStream entityStream)
                        throws IOException, WebApplicationException {
            String csv = Arrays.stream(t)
                    .map(line -> Arrays.stream(line)
                            .collect(Collectors.joining(",")))
                    .collect(Collectors.joining("\n"));
            entityStream.write(csv.getBytes());
        }
    }
}
