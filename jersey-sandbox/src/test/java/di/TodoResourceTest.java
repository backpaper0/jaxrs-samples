package di;

import javax.ws.rs.core.Application;

import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class TodoResourceTest extends JerseyTest {

    @Test
    public void test() throws Exception {
        target("todo/1").request().get(String.class);
    }

    @Override
    protected Application configure() {
        Binder binder = new AbstractBinder() {

            @Override
            protected void configure() {
                bind(MockTodoService.class).to(TodoService.class);
            }
        };
        return new ResourceConfig(TodoResource.class).register(binder);
    }

    private static class MockTodoService implements TodoService {

        @Override
        public Todo find(int id) {
            return null;
        }
    }
}
