package app;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.TransactionalException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class TransactionalExceptionMapper implements
        ExceptionMapper<TransactionalException> {

    @Override
    public Response toResponse(TransactionalException exception) {
        return Response.ok(TransactionalException.class.getSimpleName())
                .build();
    }
}
