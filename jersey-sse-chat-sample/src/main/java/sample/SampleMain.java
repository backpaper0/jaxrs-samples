package sample;

import java.net.URI;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class SampleMain {

    public static void main(String[] args) {
        URI uri = URI.create("http://localhost:8080");
        ResourceConfig configuration = new ResourceConfig();
        configuration.register(StaticResources.class);
        configuration.register(Chat.class);
        GrizzlyHttpServerFactory.createHttpServer(uri, configuration);
    }
}
