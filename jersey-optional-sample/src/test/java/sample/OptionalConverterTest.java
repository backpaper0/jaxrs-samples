package sample;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class OptionalConverterTest extends JerseyTest {

    @Test
    public void testOptional() throws Exception {
        String s = target().queryParam("opt", "hello").request()
                .get(String.class);
        assertThat(s, is("hello"));
    }

    @Test
    public void testNull() throws Exception {
        String s = target().request().get(String.class);
        assertThat(s, is("empty"));
    }

    @Test
    public void testEmpty() throws Exception {
        String s = target().queryParam("opt", "").request().get(String.class);
        assertThat(s, is("empty"));
    }

    @Test
    public void testBlank() throws Exception {
        String s = target().queryParam("opt", " ").request().get(String.class);
        assertThat(s, is("empty"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(OptionalProvider.class, TestApi.class);
    }

    @Path("")
    public static class TestApi {
        @GET
        public String optional(@QueryParam("opt") Optional<String> opt) {
            return opt.orElse("empty");
        }
    }
}
