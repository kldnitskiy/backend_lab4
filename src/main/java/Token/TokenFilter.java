package Token;

import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Key;

@TokenVerify
@Priority(Priorities.AUTHENTICATION)
@Provider
public class TokenFilter implements ContainerRequestFilter {
    CreateKey createKey;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String controlHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (controlHeader == null || !controlHeader.startsWith("ENIGMA ")) {
            throw new NotAuthorizedException("Не указан заголовок аутентификации");
        }

        String token = controlHeader.substring("ENIGMA".length()).trim();

        try {
            Key key = createKey.generateKey();
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            System.out.println(key);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
