package app;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("ejb")
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class EjbEcho {

    @EJB
    private EjbEchoService service;

    @POST
    public String echo(String s) {
        return service.echo(s);
    }
}
