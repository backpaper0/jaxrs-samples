package order;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

public abstract class ReaderInterceptorImpl implements ReaderInterceptor {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context)
            throws IOException, WebApplicationException {
        ResourceClass.log(this, "before");
        try {
            return context.proceed();
        } finally {
            ResourceClass.log(this, "after");
        }
    }

    @Priority(1)
    public static class ReaderInterceptor1 extends ReaderInterceptorImpl {
    }

    @Priority(2)
    public static class ReaderInterceptor2 extends ReaderInterceptorImpl {
    }

    @Priority(3)
    public static class ReaderInterceptor3 extends ReaderInterceptorImpl {
    }
}
