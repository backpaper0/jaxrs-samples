package sample.prematching;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class PseudoHttpMethodFilterTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        Person person = new Person();
        person.name = "うらがみ";
        person.age = 31;

        int id = target("person/12345").request()
                .header("X-Http-Method-Override", "PUT")
                .post(Entity.json(person), int.class);

        assertThat(id, is(12345));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(PseudoHttpMethodFilter.class,
                PersonApi.class);
    }
}
