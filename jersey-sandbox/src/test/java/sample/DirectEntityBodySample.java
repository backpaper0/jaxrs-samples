package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

/**
 * エンティティボディをそのまま受け取るサンプル
 *
 */
public class DirectEntityBodySample extends JerseyTest {

    @Test
    public void test() throws Exception {
        String response = target("echo").request().post(Entity.text("hello"),
                String.class);
        assertThat(response, is("hello"));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Echo.class);
    }

    @Path("echo")
    public static class Echo {
        @POST
        @Consumes("text/plain")
        public String echo(String text) {
            return text;
        }
    }
}
