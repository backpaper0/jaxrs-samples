package sample;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class HelloResourceTest extends JerseyTest {

    @Test
    public void testHelloWorld() throws Exception {
        String response =
        //リクエストするパスを指定する。
        //ベースとなるURLはJerseyTestによって設定済みなので
        //ここではリソースクラスの@Pathに書かれたパスから指定する。
        target("hello")
        //クエリパラメータを設定する。
        //URLの後ろにくっつく ?name=world の部分。
                .queryParam("name", "world")
                //ここまで組み立てたリソースに対してリクエストを行うよ！っていう印的な
                .request()
                //GETリクエストをして、レスポンスをStringで受け取る。
                .get(String.class);

        assertThat(response, is("Hello, world!"));
    }

    /**
     * Responseクラスでレスポンスの内容を詳しく調べる事もできます。
     * 
     * @throws Exception
     */
    @Test
    public void testHelloWorldResponse() throws Exception {
        Response response = target("hello").queryParam("name", "world")
                .request().get();
        assertThat("Status code", response.getStatusInfo(), is(Status.OK));
        assertThat("Entity body", response.readEntity(String.class),
                is("Hello, world!"));
    }

    @Override
    protected Application configure() {
        //configureメソッドでApplicationサブクラスを作って返します。
        //ここで返すApplicationサブクラスがテスト対象のJAX-RSアプリケーションの
        //定義となります。
        //Jerseyに用意されているResourceConfigクラスを利用するのが
        //簡単です。
        return new ResourceConfig(HelloResource.class);
    }
}
