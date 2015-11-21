package props;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class PropertiesProvidersTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        StringWriter out = new StringWriter();
        Properties p1 = new Properties();
        p1.setProperty("foo", "Hello");
        p1.setProperty("bar", "World");
        p1.store(out, "");

        String response = target().request().post(Entity.text(out.toString()),
                String.class);

        System.out.println(response);
        Properties p2 = new Properties();
        p2.load(new StringReader(response));

        assertThat(p2.getProperty("foo"), is("Hello"));
        assertThat(p2.getProperty("bar"), is("World"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Sample.class, PropertiesReader.class,
                PropertiesWriter.class);
    }

    @Path("")
    public static class Sample {
        @POST
        public Properties post(Properties props) {
            return props;
        }
    }
}
