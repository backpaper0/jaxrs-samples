package authentication;

import java.io.IOException;
import java.util.Objects;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Secure
@Provider
public class Authenticator implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        MultivaluedMap<String, String> queryParameters = requestContext
                .getUriInfo().getQueryParameters();
        String username = queryParameters.getFirst("username");
        String password = queryParameters.getFirst("password");
        if (!Objects.equals(username, "backpaper0")
                || !Objects.equals(password, "secret")) {

            requestContext.abortWith(Response.status(Status.FORBIDDEN).build());
        }
    }
}
