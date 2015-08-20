package sample;

import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

@Singleton
@Path("chat")
public class Chat {

    private static final Logger logger = Logger.getLogger(Chat.class.getName());

    private final SseBroadcaster broadcaster = new SseBroadcaster();

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput connect() {
        logger.info(() -> "connected");

        EventOutput output = new EventOutput();
        broadcaster.add(output);
        return output;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void post(String message) {
        logger.info(() -> "posted: " + message);

        OutboundEvent event = new OutboundEvent.Builder().name("message")
                .data(String.class, message)
                .mediaType(MediaType.APPLICATION_JSON_TYPE).build();
        broadcaster.broadcast(event);
    }
}
