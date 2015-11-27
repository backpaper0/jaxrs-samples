package app;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("hello")
public class AsyncHelloWorld {

    @Resource
    private ManagedExecutorService executor;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void say(@QueryParam("name") String name,
            @Suspended AsyncResponse ar) {
        executor.submit(() -> ar.resume(String.format("Hello, %s!", name)));
    }
}
