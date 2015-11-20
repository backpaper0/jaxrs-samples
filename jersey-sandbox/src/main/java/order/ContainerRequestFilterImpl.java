package order;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;

public abstract class ContainerRequestFilterImpl
        implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {
        ResourceClass.log(this);
    }

    @Priority(1)
    public static class ContainerRequestFilter1
            extends ContainerRequestFilterImpl {
    }

    @Priority(2)
    public static class ContainerRequestFilter2
            extends ContainerRequestFilterImpl {
    }

    @Priority(3)
    public static class ContainerRequestFilter3
            extends ContainerRequestFilterImpl {
    }

    @PreMatching
    @Priority(1)
    public static class PreMatchingContainerRequestFilter1
            extends ContainerRequestFilterImpl {
    }

    @PreMatching
    @Priority(2)
    public static class PreMatchingContainerRequestFilter2
            extends ContainerRequestFilterImpl {
    }

    @PreMatching
    @Priority(3)
    public static class PreMatchingContainerRequestFilter3
            extends ContainerRequestFilterImpl {
    }
}
