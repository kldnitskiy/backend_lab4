package Token;

import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateful;
import java.security.Key;

@Stateful
public class CreateKey {
    public static Key generateKey(){
        String keyString = "simplekey";
        return new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
    }
}
