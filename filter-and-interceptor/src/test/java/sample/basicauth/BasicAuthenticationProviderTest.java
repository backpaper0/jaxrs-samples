package sample.basicauth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class BasicAuthenticationProviderTest extends JerseyTest {

    /**
     * 認証の情報を送らなかったら401 Unauthorizedが返ってくる。
     * 
     * @throws Exception
     */
    @Test
    public void testUnauthenticated() throws Exception {

        Response response = target("test").request().get();

        assertThat("Status code", response.getStatus(),
                is(Status.UNAUTHORIZED.getStatusCode()));

        String wwwAuthenticate = response
                .getHeaderString(HttpHeaders.WWW_AUTHENTICATE);
        assertThat("WWW-Authenticate", wwwAuthenticate,
                is("Basic Realm=\"(-_-)\""));

        assertThat("Entity body", response.readEntity(String.class),
                is("認証しないとダメですよ！(-_-)ｼﾞﾄｯ"));
    }

    /**
     * 認証の情報を送って認証を成功させると200 OKでリソース取得！
     * 
     * @throws Exception
     */
    @Test
    public void testSuccessfulAuthentication() throws Exception {

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(
                "backpaper0", "secret");

        Response response = target("test").register(feature).request().get();

        assertThat("Status code", response.getStatus(),
                is(Status.OK.getStatusCode()));

        String wwwAuthenticate = response
                .getHeaderString(HttpHeaders.WWW_AUTHENTICATE);
        assertThat("WWW-Authenticate", wwwAuthenticate, is(nullValue()));

        assertThat("Entity body", response.readEntity(String.class),
                is("･:*+.\\(( °ω° ))/.:+"));
    }

    /**
     * 認証に失敗したら401 Unauthorizedが返ってくる。
     * 
     * @throws Exception
     */
    @Test
    public void testWrongPassword() throws Exception {

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(
                "backpaper0", "mistake");

        Response response = target("test").register(feature).request().get();

        assertThat("Status code", response.getStatus(),
                is(Status.UNAUTHORIZED.getStatusCode()));

        String wwwAuthenticate = response
                .getHeaderString(HttpHeaders.WWW_AUTHENTICATE);
        assertThat("WWW-Authenticate", wwwAuthenticate,
                is("Basic Realm=\"(-_-)\""));

        assertThat("Entity body", response.readEntity(String.class),
                is("認証しないとダメですよ！(-_-)ｼﾞﾄｯ"));
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
