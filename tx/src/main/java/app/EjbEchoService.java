package app;

import javax.ejb.Stateless;

@Stateless
public class EjbEchoService {

    public String echo(String s) {
        if (s.equals("x")) {
            throw new EchoException();
        }
        return s;
    }
}
