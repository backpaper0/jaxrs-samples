package app;

import java.io.IOException;
import java.net.URLEncoder;

import javax.mail.internet.MimeUtility;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

/**
 * Content-Dispositionのfilenameをエンコードするフィルタ。
 *
 */
@Provider
public class FileNameEncoder implements ContainerResponseFilter {

    private static final String PREFIX = "filename=\"";
    private static final String SUFFIX = "\"";

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        String cd = responseContext
                .getHeaderString(HttpHeaders.CONTENT_DISPOSITION);
        if (cd == null) {
            return;
        }
        int start = cd.indexOf(PREFIX);
        if (start < 0) {
            return;
        }
        start += PREFIX.length();
        int end = cd.indexOf(SUFFIX, start);
        if (end < 0) {
            return;
        }
        String rawFileName = cd.substring(start, end);
        String fileName;
        String userAgent = responseContext
                .getHeaderString(HttpHeaders.USER_AGENT);
        if (userAgent != null && userAgent.contains("Gecko")) {
            fileName = MimeUtility.encodeText(rawFileName, "UTF-8", "B");
        } else {
            fileName = URLEncoder.encode(rawFileName, "UTF-8");
        }
        cd = cd.substring(0, start) + fileName + cd.substring(end);
        responseContext.getHeaders().putSingle(HttpHeaders.CONTENT_DISPOSITION,
                cd);
    }
}
