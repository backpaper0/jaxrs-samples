package sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

public class MediaTypeSample {

    private final MediaType mediaType = MediaType
            .valueOf("application/json;charet=utf-8");

    @Test
    public void testEquals() throws Exception {
        assertThat(mediaType.equals(MediaType.APPLICATION_JSON_TYPE),
                is(false));
    }

    @Test
    public void testIsCompatible() throws Exception {
        assertThat(mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE),
                is(true));
    }
}
