
import java.net.URI;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

@Singleton
@Path("")
public class Sample {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final SseBroadcaster broadcaster = new SseBroadcaster();

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String view() {
        return "<script type='text/javascript'>console.log('load');"
                + "var es = new EventSource('/connect?name=html-client');"
                + "es.onmessage = function(event) { console.log(event); };"
                + "</script>";
    }

    @Path("connect")
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput connect(@QueryParam("name") String name) {
        logger.info(() -> "connected: " + name);
        EventOutput output = new EventOutput();
        broadcaster.add(output);

        //Chromeだとこのイベントは届かない？
        OutboundEvent event = new OutboundEvent.Builder().name("connect")
                .data("connected").build();
        broadcaster.broadcast(event);

        return output;
    }

    @Path("post")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void post(@FormParam("name") String name,
            @FormParam("data") String data) {
        logger.info(() -> "posted: " + name + "=" + data);
        OutboundEvent event = new OutboundEvent.Builder().name(name).data(data)
                .build();
        broadcaster.broadcast(event);
    }

    public static void main(String[] args) {
        URI uri = URI.create("http://localhost:8080");
        ResourceConfig configuration = new ResourceConfig();
        configuration.register(Sample.class);
        GrizzlyHttpServerFactory.createHttpServer(uri, configuration);

        Client client = ClientBuilder.newClient();
        EventSource es = new EventSource(
                client.target("http://localhost:8080/connect")
                        .queryParam("name", "jersey-client"));
        es.register(event -> {
            System.out.println(event.getName() + ": " + event.readData());
        });
    }
}
