package app;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class EchoExceptionMapper implements ExceptionMapper<EchoException> {

    @Override
    public Response toResponse(EchoException exception) {
        return Response.ok(EchoException.class.getSimpleName()).build();
    }
}
