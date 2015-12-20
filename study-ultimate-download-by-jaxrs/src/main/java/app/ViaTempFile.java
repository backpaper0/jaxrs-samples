package app;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * application/force-downloadの場合に一時ファイルを経由させるインターセプター。
 *
 * TODO レジュームを組み込む。そうでないと一時ファイルを経由する意味が無い気がする。
 */
@Provider
public class ViaTempFile implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {

        if (context.getMediaType()
                .isCompatible(MediaType.valueOf("application/force-download"))
                && (context.getEntity() instanceof StreamingOutput) == false) {
            OutputStream originalOut = context.getOutputStream();
            Path tempFile = Files.createTempFile(Paths.get("target"),
                    "download-", ".tmp");
            OutputStream fileOut = Files.newOutputStream(tempFile);
            context.setOutputStream(new FilterOutputStream(fileOut) {

                @Override
                public void close() throws IOException {
                    super.close();
                    context.getHeaders().putSingle(HttpHeaders.CONTENT_LENGTH,
                            Files.size(tempFile));
                    Files.copy(tempFile, originalOut);
                    originalOut.close();
                    Files.delete(tempFile);
                }
            });
        }

        context.proceed();
    }
}
