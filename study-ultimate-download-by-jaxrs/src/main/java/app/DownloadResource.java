package app;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@javax.ws.rs.Path("download")
public class DownloadResource {

    @GET
    @Produces("application/force-download")
    public Response download() {
        try {
            Path tempFile = Files.createTempFile("download-", ".tmp");
            try (BufferedWriter out = Files.newBufferedWriter(tempFile)) {
                out.write("hello world");
            }

            String filename = "サンプル.txt";
            StreamingOutput so = out -> {
                Files.copy(tempFile, out);
                Files.delete(tempFile);
            };
            return Response.ok(so)
                    .header(HttpHeaders.CONTENT_LENGTH, Files.size(tempFile))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .build();
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
