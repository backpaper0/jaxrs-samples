package sample;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.java8.RxCompletionStageInvoker;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * めっちゃシンプルなRxクライアントのサンプル。
 *
 */
public class SimpleSample {

    public static void main(String[] args) throws Exception {
        URI uri = URI.create("http://localhost:8080/");

        ResourceConfig config = new ResourceConfig();
        config.register(HelloWorld.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri,
                config);
        try {

            RxClient<RxCompletionStageInvoker> client = Rx
                    .newClient(RxCompletionStageInvoker.class);

            //普通のJAX-RSクライアントと同じ感覚で書ける。
            //request()のあとにrx()を忘れずに呼ぶこと！
            CompletionStage<Void> stage = client.target(uri).path("hello")
                    .queryParam("name", "world").request().rx()
                    .get(String.class).thenAccept(System.out::println);

            //今回は結果を確認するためCompletableFutureにしてget()して終了を待つ。
            //サンプルだからやっているけれど、実際にはget()したら負けだと思う。
            stage.toCompletableFuture().get();

        } finally {
            server.shutdown();
        }
    }

    @Path("hello")
    public static class HelloWorld {

        @GET
        @Produces(MediaType.TEXT_PLAIN)
        public void say(@QueryParam("name") String name,
                @Suspended AsyncResponse response) {

            //無駄に非同期処理してみる
            CompletableFuture
                    .supplyAsync(() -> String.format("Hello, %s!", name))
                    .thenAccept(response::resume);
        }
    }
}
