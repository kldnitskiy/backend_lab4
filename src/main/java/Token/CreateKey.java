package Token;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


public class CreateKey {
    public static Key generateKey(){
        String keyString = "simplekey";
        return new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
    }
}
