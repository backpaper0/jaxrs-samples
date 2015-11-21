package sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class XmlJsonSample extends JerseyTest {

    @Test
    public void test() throws Exception {
        System.out.println(target().request().accept("application/json")
                .post(Entity.json(new Person("うらがみ", 31)), String.class));
        System.out.println(target().request().accept("application/xml")
                .post(Entity.xml(new Person("うらがみ", 31)), String.class));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(Sample.class);
    }

    @Path("")
    public static class Sample {
        @POST
        @Consumes({ "application/xml", "application/json" })
        @Produces({ "application/xml", "application/json" })
        public Person get(Person p) {
            return p;
        }
    }

    @XmlRootElement
    public static class Person {
        public String name;
        public int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
