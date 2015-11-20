package async;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("download")
public class LongTimeLargeDataSample {

    @GET
    @Produces("text/csv")
    public Response downloadCsv() {
        //長い時間がかかり、大量データを扱う処理
        String csv = createCsv();
        String filename = "sample.csv";
        return Response.ok(csv).header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + filename).build();
    }

    private static String createCsv() {
        return "1,foo\n2,bar\n3,baz";
    }
}
