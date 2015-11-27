package validation;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class ErrorMessageTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        Response response = target().request().post(Entity.text(""));
        assertThat(response.getStatus(), is(400));
        String errorMessage = response.readEntity(String.class);
        assertThat(errorMessage, is(notNullValue()));
        System.out.println(errorMessage);
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(SampleResource.class,
                ValidationErrorProvider.class);
    }

    @Path("")
    public static class SampleResource {

        @POST
        public void create(@Size(min = 1) String text) {
        }
    }

    public static class ValidationErrorProvider
            implements ExceptionMapper<ConstraintViolationException> {

        @Override
        public Response toResponse(ConstraintViolationException exception) {
            String messages = exception.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(","));
            return Response.status(400).entity(messages).build();
        }
    }
}
