package authentication;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class AuthenticatorTest extends JerseyTest {

    @Test
    public void testFooOK() throws Exception {
        Response response = target("foo").queryParam("username", "backpaper0")
                .queryParam("password", "secret").request().get();
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
        assertThat(response.readEntity(String.class), is("foo"));
    }

    @Test
    public void testFooNG() throws Exception {
        Response response = target("foo").request().get();
        assertThat(response.getStatus(), is(Status.FORBIDDEN.getStatusCode()));
    }

    @Test
    public void testBar() throws Exception {
        Response response = target("bar").request().get();
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
        assertThat(response.readEntity(String.class), is("bar"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Authenticator.class, Foo.class, Bar.class);
    }

    @Secure
    @Path("foo")
    public static class Foo {
        @GET
        public String get() {
            return "foo";
        }
    }

    @Path("bar")
    public static class Bar {
        @GET
        public String get() {
            return "bar";
        }
    }
}
