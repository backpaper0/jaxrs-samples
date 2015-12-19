package app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("download")
public class DownloadResource {

    @GET
    @Produces("application/force-download")
    public Response download() {
        String filename = "サンプル.txt";
        return Response.ok("hello world")
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .build();
    }

    @GET
    @Path("async/prepare")
    public void prepare() {
        System.out.println("prepare");
    }
}
