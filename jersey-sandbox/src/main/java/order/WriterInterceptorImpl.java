package order;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

public abstract class WriterInterceptorImpl implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        ResourceClass.log(this, "before");
        context.proceed();
        ResourceClass.log(this, "after");
    }

    @Priority(1)
    public static class WriterInterceptor1 extends WriterInterceptorImpl {
    }

    @Priority(2)
    public static class WriterInterceptor2 extends WriterInterceptorImpl {
    }

    @Priority(3)
    public static class WriterInterceptor3 extends WriterInterceptorImpl {
    }
}
