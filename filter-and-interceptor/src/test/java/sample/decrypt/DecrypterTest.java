package sample.decrypt;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class DecrypterTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        byte[] key = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        byte[] text = c.doFinal("HelloWorld".getBytes());
        String s = target("echo").request().post(Entity.text(text),
                String.class);
        assertThat(s, is("HelloWorld"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Decrypter.class, Echo.class);
    }

    @Path("echo")
    public static class Echo {
        @POST
        public String echo(String text) {
            return text;
        }
    }
}
