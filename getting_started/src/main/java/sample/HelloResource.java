package sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("hello")
public class HelloResource {

    @GET
    public String say(@QueryParam("name") String name) {
        return String.format("Hello, %s!", name);
    }
}
