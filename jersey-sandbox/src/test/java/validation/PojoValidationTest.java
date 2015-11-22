package validation;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class PojoValidationTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        Person p = new Person();
        p.name = "";
        p.age = -10;
        Response response = target("person").request().post(Entity.xml(p));
        assertThat(response.getStatus(), is(400));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(PersonResource.class
        //,ValidationErrorProvider.class
        );
    }

    @Path("person")
    public static class PersonResource {

        @POST
        @Produces(MediaType.APPLICATION_XML)
        public void create(@Valid Person person) {
        }
    }

    @XmlRootElement
    public static class Person {

        @NotNull
        @Size(min = 1, max = 20)
        public String name;

        @Min(0)
        @Max(150)
        public int age;
    }

    //    public static class ValidationErrorProvider
    //            implements ExceptionMapper<ConstraintViolationException> {
    //
    //        @Override
    //        public Response toResponse(ConstraintViolationException exception) {
    //            exception.getConstraintViolations().stream()
    //                    .map(ConstraintViolation::getMessage)
    //                    .forEach(System.out::println);
    //            return Response.status(400).build();
    //        }
    //    }
}
