package sample.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import sample.annotation.Prefix;

@Path("foo")
@Produces(MediaType.TEXT_PLAIN)
public class FooResource {

    @Prefix("***")
    @GET
    public String foo() {
        return "foo";
    }

    @Prefix("+++")
    @Path("bar")
    @GET
    public String bar() {
        return "bar";
    }

    @Path("baz")
    public SubResource baz() {
        return new SubResource();
    }

    public class SubResource {
        @Prefix("@@@")
        @GET
        public String baz() {
            return "baz";
        }
    }
}
