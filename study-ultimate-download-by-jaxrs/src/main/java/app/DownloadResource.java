package app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("download")
public class DownloadResource {

    @GET
    @Produces("application/force-download")
    public String download() {
        return "hello world";
    }
}
