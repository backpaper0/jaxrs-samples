package sample.gzip;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class GZipProviderTest extends JerseyTest {

    @Test
    public void testGZip() throws Exception {
        Response response = target("test").request().acceptEncoding("gzip")
                .get();
        InputStream in = new GZIPInputStream(
                response.readEntity(InputStream.class));
        byte[] b = new byte[Math.max(response.getLength(), 1024)];
        int i;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (-1 != (i = in.read(b))) {
            out.write(b, 0, i);
        }

        assertThat("Header",
                response.getHeaderString(HttpHeaders.CONTENT_ENCODING),
                is("gzip"));
        assertThat("Entity body", out.toString("UTF-8"),
                is("･:*+.\\(( °ω° ))/.:+"));
    }

    @Test
    public void testPlain() throws Exception {
        Response response = target("test").request().get();

        assertThat("Header",
                response.getHeaderString(HttpHeaders.CONTENT_ENCODING),
                is(nullValue()));
        assertThat("Entity body", response.readEntity(String.class),
                is("･:*+.\\(( °ω° ))/.:+"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(GZipProvider.class, TestResource.class);
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
