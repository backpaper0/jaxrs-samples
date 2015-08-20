package sample;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("{name:.+\\.(html|js|css)}")
public class StaticResources {

    @GET
    public Response getResource(@PathParam("name") String name) {

        URL resource = getClass().getResource("/static/" + name);
        if (resource == null) {
            throw new NotFoundException();
        }

        Map<String, MediaType> types = new HashMap<>();
        types.put(".html", MediaType.TEXT_HTML_TYPE);
        types.put(".js", MediaType.valueOf("text/javascript"));
        types.put(".css", MediaType.valueOf("text/css"));

        MediaType type = types.get(name.substring(name.lastIndexOf('.')));

        try {
            return Response.ok(resource.openStream(), type).build();
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
