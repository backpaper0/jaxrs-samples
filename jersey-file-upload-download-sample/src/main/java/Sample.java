import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@Path("")
public class Sample {

    static final AtomicReference<byte[]> data = new AtomicReference<>();

    @GET
    @Produces(MediaType.TEXT_HTML)
    public InputStream index() {
        return getClass().getResourceAsStream("/index.html");
    }

    @Path("upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void upload(FormDataMultiPart multiPart) {
        FormDataBodyPart file = multiPart.getField("file");
        byte[] b = file.getEntityAs(byte[].class);
        data.set(b);
    }

    @Path("download")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] download() {
        byte[] b = data.get();
        if (b == null) {
            throw new NotFoundException();
        }
        return b;
    }

    public static void main(String[] args) throws IOException {
        URI uri = URI.create("http://localhost:8080/");
        ResourceConfig config = new ResourceConfig();
        config.register(Sample.class);
        config.register(MultiPartFeature.class);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri,
                config);
        System.in.read();
        server.shutdown();
    }
}
