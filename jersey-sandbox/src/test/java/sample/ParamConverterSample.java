package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class ParamConverterSample extends JerseyTest {

    @Test
    public void test() throws Exception {
        String response = target().queryParam("date", "2015-11-21").request()
                .get(String.class);
        assertThat(response, is("2015-11-21"));
    }

    @Test
    public void testNull() throws Exception {
        Response response = target().request().get();
        assertThat(response.getStatus(), is(Status.NO_CONTENT.getStatusCode()));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Sample.class,
                ParamConverterProviderImpl.class);
    }

    public static class LocalDateConverter
            implements ParamConverter<LocalDate> {

        @Override
        public LocalDate fromString(String value) {
            return Optional.ofNullable(value).map(LocalDate::parse)
                    .orElse(null);
        }

        @Override
        public String toString(LocalDate value) {
            return Optional.ofNullable(value).map(Object::toString)
                    .orElse(null);
        }
    }

    public static class ParamConverterProviderImpl
            implements ParamConverterProvider {

        @SuppressWarnings("unchecked")
        @Override
        public <T> ParamConverter<T> getConverter(Class<T> rawType,
                Type genericType, Annotation[] annotations) {
            if (rawType == LocalDate.class) {
                return (ParamConverter<T>) new LocalDateConverter();
            }
            return null;
        }
    }

    @Path("")
    public static class Sample {
        @GET
        public String get(@QueryParam("date") LocalDate date) {
            return Optional.ofNullable(date).map(Object::toString).orElse(null);
        }
    }
}
