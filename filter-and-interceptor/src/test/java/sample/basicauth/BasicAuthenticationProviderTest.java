package sample.basicauth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class BasicAuthenticationProviderTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        System.in.read();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(BasicAuthenticationProvider.class,
                TestResource.class);
    }

    @Path("test")
    public static class TestResource {
        @GET
        @Produces("text/plain; charset=UTF-8")
        public String get() {
            return "･:*+.\\(( °ω° ))/.:+";
        }
    }
}
