package order;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("")
public class ResourceClass {

    private static final List<String> logs = new ArrayList<>();

    @POST
    public EntityClass post(EntityClass entity) {
        log(this);
        return entity;
    }

    static void log(Object component, String message) {
        String log = component.getClass().getSimpleName();
        if (message != null) {
            log += " " + message;
        }
        logs.add(log);
    }

    static void log(Object component) {
        log(component, null);
    }

    public static void printLogs() {
        for (String log : logs) {
            System.out.println(log);
        }
    }
}
