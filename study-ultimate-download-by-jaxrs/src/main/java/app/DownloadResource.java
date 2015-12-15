package app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("download")
public class DownloadResource {

    @GET
    @Produces("application/force-download")
    public Response download() {
        try {
            return Response.ok("hello world").header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\""
                            + URLEncoder.encode("サンプル.txt", "UTF-8") + "\"")
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
