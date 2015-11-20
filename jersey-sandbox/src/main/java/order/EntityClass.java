package order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class EntityClass {

    private final String value;

    public EntityClass(InputStream in) {
        value = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8)).lines()
                        .collect(Collectors.joining());
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(value.getBytes(StandardCharsets.UTF_8));
    }
}
