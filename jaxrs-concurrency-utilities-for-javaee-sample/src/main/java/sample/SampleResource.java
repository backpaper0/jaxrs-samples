package sample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("")
public class SampleResource {

    @Inject
    private ManagedExecutorService executor;

    @Path("async")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void async(@QueryParam("sleep") int seconds,
            @QueryParam("name") String name,
            @Suspended AsyncResponse response) {
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (Exception e) {
            }
            response.resume(String.format("Hello, %s!", name));
        } , executor);
    }

    @Path("sync")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sync(@QueryParam("name") String name) {
        return String.format("Hello, %s!", name);
    }
}
