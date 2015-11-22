package validation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("addition")
public class Addition {

    @GET
    public int calculate(@Valid @BeanParam Parameters ps) {
        return ps.a + ps.b;
    }

    public static class Parameters {

        @NotNull
        @QueryParam("a")
        public Integer a;

        @NotNull
        @QueryParam("b")
        public Integer b;
    }
}
