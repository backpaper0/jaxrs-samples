package sample.basicauth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

/**
 * ContainerRequestFilterを使ってBasic認証
 *
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthenticationProvider implements ContainerRequestFilter {

    private final Pattern credentialsPattern = Pattern.compile("^Basic +(.+)$");

    //認証情報。本来はRDBなどから取得する。
    private String username = "backpaper0";
    private String password = "secret";

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        String authorization = requestContext
                .getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorization != null) {

            //次のようなリクエストヘッダが送られてくるから
            //Base64でデコードして ユーザー名:パスワード を得る。
            //Authorization: Basic YmFja3BhcGVyMDpzZWNyZXQ=

            Matcher m = credentialsPattern.matcher(authorization);
            if (m.matches()) {

                String credentialsEncodedBase64 = m.group(1);
                byte[] decoded = Base64.getDecoder().decode(
                        credentialsEncodedBase64);
                String credentials = new String(decoded, StandardCharsets.UTF_8);

                if (credentials.equals(username + ":" + password)) {
                    //認証成功！
                    return;
                }
            }
        }

        //認証失敗(´;ω;`)

        Response response = Response.status(Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Basic Realm=\"(-_-)\"")
                .type("text/plain; charset=UTF-8")
                .entity("認証しないとダメですよ！(-_-)ｼﾞﾄｯ").build();
        requestContext.abortWith(response);
    }
}
