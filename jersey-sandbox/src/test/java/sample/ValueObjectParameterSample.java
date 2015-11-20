package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class ValueObjectParameterSample extends JerseyTest {

    @Test
    public void test() throws Exception {
        String response = target().queryParam("value", "HelloWorld").request()
                .get(String.class);
        assertThat(response, is("HelloWorld"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Sample.class);
    }

    @Path("")
    public static class Sample {
        @GET
        public String get(@QueryParam("value") ValueObject vo) {
            return vo.getValue();
        }
    }

    public static class ValueObject {

        private final String value;

        public ValueObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
