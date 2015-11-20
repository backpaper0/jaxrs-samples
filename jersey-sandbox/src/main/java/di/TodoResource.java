package di;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("todo/{id}")
public class TodoResource {

    @Inject
    private TodoService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Todo get(@PathParam("id") int id) {
        return service.find(id);
    }
}
