package sample.gzip;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

public class GZipProvider implements ContainerResponseFilter, WriterInterceptor {

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {

        //エンティティボディがあり、リクエストヘッダの
        //Accept-EncodingにgzipがあればGZip圧縮する。

        if (responseContext.getEntity() == null) {
            return;
        }

        String acceptEncoding = requestContext
                .getHeaderString(HttpHeaders.ACCEPT_ENCODING);
        if (acceptEncoding == null) {
            return;
        }

        String[] acceptEncodings = acceptEncoding.split("\\s*,\\s*");
        if (Arrays.stream(acceptEncodings).anyMatch(a -> a.equals("gzip")) == false) {
            return;
        }

        responseContext.getHeaders().add(HttpHeaders.CONTENT_ENCODING, "gzip");

        //GZipOutputStream.writeHeader()で例外が出る。
        //この時点でエンティティのストリームに書き込みを行ってはいけない。
        //GZipOutputStreamをラップしてwriteHeaderを遅延させればいけるとは思う。
        //        responseContext.setEntityStream(new GZIPOutputStream(responseContext
        //                .getEntityStream()));
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        Object contentEncoding = context.getHeaders().getFirst(
                HttpHeaders.CONTENT_ENCODING);
        if (contentEncoding != null && contentEncoding.equals("gzip")) {
            context.setOutputStream(new GZIPOutputStream(context
                    .getOutputStream()));
        }
        context.proceed();
    }
}
