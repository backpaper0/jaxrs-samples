package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

@Path("download/async")
public class AsyncDownloadResource {

    @GET
    @Path("prepare")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput prepare() throws IOException {
        EventOutput output = new EventOutput();
        UUID id = UUID.randomUUID();
        output.write(new OutboundEvent.Builder().name("ready")
                .data(id.toString()).build());
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                Files.write(Paths.get("target", id.toString()),
                        id.toString().getBytes());
                output.write(new OutboundEvent.Builder().name("id")
                        .data(id.toString()).build());
                output.close();
            } catch (Exception e) {
            }
        });
        return output;
    }

    @GET
    @Path("{id}")
    @Produces("application/force-download")
    public Response download(@PathParam("id") UUID id) {
        String filename = id + ".txt";
        StreamingOutput so = out -> {
            Files.copy(Paths.get("target", id.toString()), out);
            Files.delete(Paths.get("target", id.toString()));
        };
        return Response.ok(so).header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"").build();
    }
}
