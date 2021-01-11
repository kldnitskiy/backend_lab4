package Token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Stateless
public class Token {
    @EJB
    CreateKey createKey;

    public static String createToken(String login){
        CreateKey generator = new CreateKey();
        Key key =  generator.generateKey();
        return Jwts.builder().setSubject(login).setIssuedAt(new Date()).setExpiration(createDateExpiration(LocalDateTime.now().plusMinutes(60L))).signWith(SignatureAlgorithm.HS512,key).compact();
    }
    private static Date createDateExpiration(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
