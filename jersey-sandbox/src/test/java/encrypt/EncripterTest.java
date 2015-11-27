package encrypt;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.crypto.Cipher;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class EncripterTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        byte[] response = target("sample").request().get(byte[].class);
        Cipher c = Encripter.cipher(Cipher.DECRYPT_MODE);
        String s = new String(c.doFinal(response));
        assertThat(s, is("HelloWorld"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Encripter.class, Sample.class);
    }

    @Path("sample")
    public static class Sample {

        @GET
        public String get() {
            return "HelloWorld";
        }
    }
}
