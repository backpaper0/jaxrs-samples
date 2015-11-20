package transaction;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Transactional
@Path("transaction")
public class TransactionalSample {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void create(String entity) {
    }
}
