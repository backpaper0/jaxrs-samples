package encrypt;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Provider
public class Encripter implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        try {
            OutputStream out = context.getOutputStream();
            Cipher c = cipher(Cipher.ENCRYPT_MODE);
            context.setOutputStream(new CipherOutputStream(out, c));
            context.proceed();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }
    }

    static Cipher cipher(int mode) throws GeneralSecurityException {
        Cipher c = Cipher.getInstance("AES");
        c.init(mode, new SecretKeySpec(
                new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5 },
                "AES"));
        return c;
    }
}
