package async;

import java.io.OutputStream;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@RequestScoped
@Path("download_async")
public class AsyncLongTimeLargeDataSample {

    @Inject
    private ManagedExecutorService executor;

    @GET
    @Produces("text/csv")
    public void downloadCsv(@Suspended AsyncResponse ar) {
        executor.submit(() -> {
            StreamingOutput so = (OutputStream out) -> {
                Csv csv = createCsv();
                while (csv.fetch()) {
                    out.write(csv.nextLine());
                }
            };

            String filename = "sample.csv";
            Response response = Response.ok(so)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + filename)
                    .build();
            ar.resume(response);
        });
    }

    interface Csv {
        boolean fetch();

        byte[] nextLine();
    }

    private static Csv createCsv() {
        return null;
    }
}
