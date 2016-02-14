package optional;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class ReturnOptionalTest extends JerseyTest {

    @Test
    public void return_optional() throws Exception {
        Response response = target().request().post(Entity.form(new Form().param("text", "x")));
        assertThat(response).extracting(Response::getStatusInfo, r -> r.readEntity(String.class))
                .contains(Status.OK, "x");
    }

    @Test
    public void return_empty() throws Exception {
        Response response = target().request().post(Entity.form(new Form()));
        assertThat(response).extracting(Response::getStatusInfo).contains(Status.NO_CONTENT);
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(ReturnOptional.class, TestApi.class);
    }

    @Path("")
    public static class TestApi {

        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Produces(MediaType.TEXT_PLAIN)
        public Optional<String> post(@FormParam("text") String text) {
            return Optional.ofNullable(text);
        }
    }
}
