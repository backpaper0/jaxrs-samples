package sample;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

public class ClientApiSample {

    public static void main(String[] args) {
        Object account = null;
        Object clientId = null;

        Client client = ClientBuilder.newClient();
        Account result = client.target("http://localhost:8080/account")
                .queryParam("id", "backpaper0").request()
                .header("X-Client-ID", clientId)
                .post(Entity.json(account), Account.class);
    }

    static class Account {
    }
}
