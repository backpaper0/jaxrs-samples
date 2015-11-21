package sample.prematching;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;

@PreMatching
public class PseudoHttpMethodFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {
        String actual = requestContext.getMethod();
        String pseudo = requestContext
                .getHeaderString("X-Http-Method-Override");
        if (actual.equalsIgnoreCase("POST") && pseudo != null) {
            requestContext.setMethod(pseudo);
        }
    }
}
