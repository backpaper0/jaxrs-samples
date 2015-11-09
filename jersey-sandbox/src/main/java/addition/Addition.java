package addition;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/addition")
public class Addition {
    @GET
    public int calculate(@QueryParam("a") int a, @QueryParam("b") int b) {
        return a + b;
    }
}
