package versioning;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class VersioningTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        assertThat(target("/rest/v1/products").request().get(String.class)).isEqualTo("v1");
        assertThat(target("/rest/v2/products").request().get(String.class)).isEqualTo("v2");
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(TestApi.class, VersioningFilter.class);
    }

    @PreMatching
    public static class VersioningFilter implements ContainerRequestFilter {

        @Override
        public void filter(ContainerRequestContext requestContext) throws IOException {
            UriBuilder builder = requestContext.getUriInfo().getBaseUriBuilder();
            Iterator<PathSegment> it = requestContext.getUriInfo().getPathSegments().iterator();
            builder.path(it.next().getPath());
            String version = it.next().getPath();
            while (it.hasNext()) {
                builder.path(it.next().getPath());
            }
            requestContext.setRequestUri(builder.build());
            requestContext.getHeaders().add("version", version);
        }
    }

    @Path("/rest")
    public static class TestApi {
        @Path("/products")
        @GET
        public String get1(@HeaderParam("version") String version) {
            return version;
        }
    }
}
