package order;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public abstract class ContainerResponseFilterImpl
        implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        ResourceClass.log(this);
    }

    @Priority(1)
    public static class ContainerResponseFilter1
            extends ContainerResponseFilterImpl {
    }

    @Priority(2)
    public static class ContainerResponseFilter2
            extends ContainerResponseFilterImpl {
    }

    @Priority(3)
    public static class ContainerResponseFilter3
            extends ContainerResponseFilterImpl {
    }
}
