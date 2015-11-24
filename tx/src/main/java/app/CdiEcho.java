package app;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("cdi")
@RequestScoped
public class CdiEcho {

    @Inject
    private CdiEchoService service;

    @POST
    public String echo(String s) {
        return service.echo(s);
    }
}
