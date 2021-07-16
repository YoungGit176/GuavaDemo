import org.junit.Test;

import java.util.UUID;

public class TestApi {
    public static String uuid = UUID.randomUUID().toString().replace("-", "");

    @Test
    public void testUUID(){
        System.out.println(uuid);
    }
}
