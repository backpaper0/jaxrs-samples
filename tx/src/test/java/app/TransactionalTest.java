package app;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.ejb.EJBException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TransactionalTest {

    @Test
    public void testCdi() throws Exception {
        testEcho("cdi", "hello", "hello");
    }

    @Test
    public void testCdiException() throws Exception {
        testEcho("cdi", "x", EchoException.class.getSimpleName());
    }

    @Test
    public void testEjb() throws Exception {
        testEcho("ejb", "hello", "hello");
    }

    @Test
    public void testEjbException() throws Exception {
        //EjbExceptionMapperがなければEchoExceptionを処理できてるっぽい。
        //ハマりそう……
        testEcho("ejb", "x", EJBException.class.getSimpleName());
    }

    private static void testEcho(String path, String text, String expected) {
        String actual = ClientBuilder.newClient()
                .target("http://localhost:8181/sample/echo").path(path)
                .request().post(Entity.text(text), String.class);
        assertThat(actual, is(expected));
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "sample.war").addClasses(
                EchoApplication.class, CdiEcho.class, CdiEchoService.class,
                EjbEcho.class, EjbEchoService.class, EchoException.class,
                EchoExceptionMapper.class, TransactionalExceptionMapper.class,
                EjbExceptionMapper.class);
    }
}
