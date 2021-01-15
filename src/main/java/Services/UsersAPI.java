package Services;

import Beans.UserBean;
import Model.Database;
import Model.Validation;
import Responses.ServiceCheck;
import Responses.ValidateParameters;
import Token.CreateKey;
import Token.TokenVerify;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@Path("/users")

@RequestScoped
public class UsersAPI {
    @EJB
    private Database database;

    @GET
    @Path("/service-check")
    @Produces("application/json")
    public Response serviceCheck(){
        ServiceCheck response = new ServiceCheck("success", "service-check is working");
        return Response.ok().entity(response).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/get-session")
    @TokenVerify
    @Produces("application/json")
    public Response getSessionUser(@HeaderParam("Authorization") String authorization) throws SQLException {
            String token = authorization.substring("Enigma".length()).trim();
            Claims jwt = Jwts.parser().setSigningKey(CreateKey.generateKey()).parseClaimsJws(token).getBody();
            if(!database.userAlreadyExisted(jwt.getSubject())){
                ServiceCheck response = new ServiceCheck("success", "Пользователь авторизирован");
                return Response.ok().entity(response).type(MediaType.APPLICATION_JSON).build();
            }else{
                ServiceCheck response = new ServiceCheck("error", "Доступ запрещен");
                return Response.status(Response.Status.UNAUTHORIZED).entity(response).type(MediaType.APPLICATION_JSON).build();
            }


    }

    @GET
    @Path("/get-user")
    @Produces("application/json")
    public Response checkUser(@QueryParam("username") String username) throws SQLException {
        System.out.println(username);
        ServiceCheck response = new ServiceCheck("success", username);
        return Response.ok().entity(response).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/login")
    @Produces("application/json")
    public Response loginUser(@FormParam("username") String username, @FormParam("pwd") String pwd) throws SQLException, ClassNotFoundException {
        Validation validator = new Validation();
        if(validator.getStrict_mode()){
            validator.validate_username(username);
            validator.validate_pwd(pwd);
        }
        if(validator.getBad_parameter() != null){
            ValidateParameters response = new ValidateParameters("error", validator.getMessage(), validator.getBad_parameter());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).type(MediaType.APPLICATION_JSON).build();
        }else{
            return Response.ok().entity(database.loginUser(username, pwd)).type(MediaType.APPLICATION_JSON).build();

        }
    }

    @POST
    @Path("/register")
    @Produces("application/json")
    //@Consumes("multipart/form-data")
    public Response registerUser(@FormParam("username") String username, @FormParam("pwd") String pwd, @FormParam("pwd_repeat") String pwd_repeat, @FormParam("isu") Integer isu, @FormParam("group_number") String group_number, @FormParam("email") String email) throws SQLException, UnsupportedEncodingException {
        System.out.println("register was called with parameters: " + username + "|" + pwd + "|" + pwd_repeat + "|" + isu + "|" + group_number + "|" + email);
        //Validation
        Validation validator = new Validation();
        if(validator.getStrict_mode()){
            validator.validate_username(username);
            validator.validate_pwd(pwd);
            validator.validate_repeat(pwd, pwd_repeat);
            validator.validate_isu(isu);
            validator.validate_email(email);
        }
        //Returning request based on validation results
        if(validator.getBad_parameter() != null){
            ValidateParameters response = new ValidateParameters("error", validator.getMessage(), validator.getBad_parameter());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).type(MediaType.APPLICATION_JSON).build();
        }else{
            if(database.userAlreadyExisted(username)){
               return Response.ok().entity(database.registerUser(username, pwd, isu, group_number, email)).type(MediaType.APPLICATION_JSON).build();
            }
            ServiceCheck response = new ServiceCheck("error", "Данный пользователь уже существует");
            return Response.status(Response.Status.CONFLICT).entity(response).type(MediaType.APPLICATION_JSON).build();
        }
    }







}
