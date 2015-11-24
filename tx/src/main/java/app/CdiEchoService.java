package app;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CdiEchoService {

    public String echo(String s) {
        if (s.equals("x")) {
            throw new EchoException();
        }
        return s;
    }
}
