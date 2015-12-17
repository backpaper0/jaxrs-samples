package app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.internet.MimeUtility;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("download")
public class DownloadResource {

    @GET
    @Produces("application/force-download")
    public Response download(
            @HeaderParam(HttpHeaders.USER_AGENT) @DefaultValue("") String userAgent) {
        try {
            String filename0 = "サンプル.txt";
            String filename;
            if (userAgent.contains("Gecko")) {
                filename = MimeUtility.encodeText(filename0, "UTF-8", "B");
            } else {
                filename = URLEncoder.encode(filename0, "UTF-8");
            }
            return Response.ok("hello world")
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
