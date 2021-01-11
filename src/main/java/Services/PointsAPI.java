package Services;

import Model.Database;
import Responses.OperationResult;
import Responses.ServiceCheck;
import Token.CreateKey;
import Token.TokenVerify;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Path(value = "/points")
@RequestScoped
public class PointsAPI {
   @EJB
   private Database database;

    @POST
    @TokenVerify
    @Path("/save")
    @Produces("application/json")
    public Response renderPoint(@HeaderParam("Authorization") String authorization, @FormParam("x") String x, @FormParam("y") Float y, @FormParam("r") String r, @Context HttpServletRequest req, @Context HttpServletResponse resp) throws IOException, SQLException, NamingException {
        if(x == null || y == null || r == null){
            ServiceCheck response = new ServiceCheck("error", "Не все параметры заполнены");
            String ResponseEntity = new Gson().toJson(response);
            return Response.ok().entity(ResponseEntity).build();
        }
        List<String> x_items = Arrays.asList(x.split("\\s*,\\s*"));
        List<String> r_items = Arrays.asList(r.split("\\s*,\\s*"));
        Integer all_points = 0;
        Integer succeded_points = 0;
        Boolean validation_status = false;
        String token = authorization.substring("Enigma".length()).trim();
        Claims jwt = Jwts.parser().setSigningKey(CreateKey.generateKey()).parseClaimsJws(token).getBody();
        for (Integer i = 0; i < r_items.size(); i++){
            for (Integer j = 0; j < x_items.size(); j++){
                all_points++;
                if(preValidation(Float.parseFloat(x_items.get(j)), y, Float.parseFloat(r_items.get(i)))){
                    validation_status = validation(Float.parseFloat(x_items.get(j)), y, Float.parseFloat(r_items.get(i)));
                    System.out.println(Float.parseFloat(x_items.get(j)));
                    System.out.println(y);
                    System.out.println(Float.parseFloat(r_items.get(i)));
                    System.out.println(validation_status);
                    succeded_points++;
                    database.savePoint(Float.parseFloat(x_items.get(j)), y, Float.parseFloat(r_items.get(i)), validation_status, jwt.getSubject());
                }else{
                }

            }
        }
            OperationResult response = new OperationResult("success", "Сохранено точек: " + succeded_points + "/" + all_points, validation_status);
            String ResponseEntity = new Gson().toJson(response);

            return Response.ok().entity(ResponseEntity).build();

    }



    @POST
    @TokenVerify
    @Path("/get")
    @Produces("application/json")
    public Response getPoints(@HeaderParam("Authorization") String authorization) throws SQLException, NamingException {
        System.out.println(authorization);
        String token = authorization.substring("Enigma".length()).trim();
        Claims jwt = Jwts.parser().setSigningKey(CreateKey.generateKey()).parseClaimsJws(token).getBody();
        System.out.println(jwt.getSubject());
       String ResponseEntity = new Gson().toJson(database.getPoint(jwt.getSubject()));
        return Response.ok().entity(ResponseEntity).build();
    }

    public Boolean validation(Float x, Float y, Float r){
        if((x <= 0 && y >= 0) && (Math.abs(x) <= r/2) && (Math.abs(y) <= r)){
            return true;
        }
        else if(x >= 0 && y >= 0 && y <= (-x+(r/2))){
            return true;
        }
        else return x <= 0 && y <= 0 && r/2 >= Math.sqrt(y * y + x * x);
    }

    public Boolean preValidation(Float x, Float y, Float r){
        if(x <= 3 && x >= -5 && y <= 3 && y >= -3 && r <= 3 && r > 0){
            return true;
        }else{
            return false;
        }
    }
}
