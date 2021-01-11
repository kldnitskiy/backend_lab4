package Model;

import Beans.PointsBean;
import Beans.UserBean;
import Responses.LoginUser;
import Responses.RegisterUser;
import Token.Token;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.dbutils.DbUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class Database {
    @Inject
    private UserBean userBean;
    @Resource(name = "java:/comp/env/jdbc/postgres")
    private DataSource dataSource;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection conn = null;
    Boolean isLogged = false;
    Boolean isRegistered = false;
    Boolean isExist = false;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory( "sample" );
    EntityManager entityManager = emf.createEntityManager();

    @PostConstruct
    public void init() throws  SQLException {
        //connectionDB();
    }
    @Transactional
    public Response checkUserExistence(){
        UserBean test_user = new UserBean("test", "12345678", 123456, "p3232", "core2pixel@gmail.com");
        entityManager.persist(test_user);
        //entityManager.flush();
        List<UserBean> foo = entityManager.createQuery("select a FROM username a").getResultList();
        for (UserBean entity : foo) {
            System.out.println(entity);
            return Response.ok().entity(entity).build();
        }
        return Response.ok().entity(foo).build();
    }

    private void connectionDB() throws SQLException{
        try {
            conn = dataSource.getConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        conn.createStatement().execute(
                "create table if not exists users (" +
                        "username char, pwd char, isu float, group_number char, email char, id INT GENERATED ALWAYS AS IDENTITY )");
        conn.createStatement().execute(
                "create table if not exists results (" +
                        "x float , y float, r float, result boolean, username char)");
        conn.close();
    }

    //Get current connection link
    public void setConnection(){
        try{
            Class.forName("org.postgresql.Driver");
            conn = dataSource.getConnection();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void savePoint(Float x, Float y, Float r, Boolean status, String username) throws NamingException, SQLException{
        try{
            setConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO results(x,y,r,result,username) VALUES(?,?,?,?,?)");
            ps.setDouble(1, x);
            ps.setDouble(2, y);
            ps.setDouble(3, r);
            ps.setBoolean(4, status);
            ps.setString(5, username);
            Integer resp = ps.executeUpdate();
        }catch (SQLException ex){
            System.out.println(ex);
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public List<PointsBean> getPoint(String username) throws SQLException {
        List<PointsBean> list = new ArrayList<PointsBean>();
        try{
            setConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM results WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                PointsBean fetch = new PointsBean();
                fetch.setX(Math.round(rs.getDouble("x")*100.0)/100.0);
                fetch.setY(Math.round(rs.getDouble("y")*100.0)/100.0);
                fetch.setR(rs.getDouble("r"));
                fetch.setRes(rs.getBoolean("result"));
                list.add(fetch);
            }
        }catch (SQLException ex){
            System.out.println(ex);
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
            return list;
        }


    }

    public LoginUser loginUser(String username, String pwd) {
        try{
            setConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT username FROM users WHERE username = ? AND pwd = ? ");
            ps.setString(1, username);
            ps.setString(2, md5Apache(pwd));
            ResultSet rs = ps.executeQuery();
            rs.last();
            if(rs.getRow() != 0){
                isLogged = true;
            }else{
                isLogged = false;
            }

        }catch (SQLException ex){
            System.out.println(ex);
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
            if(isLogged){
                String token = Token.createToken(username);
                System.out.println(retrieveGroupNumber(username));
                return new LoginUser("success", token, username, retrieveGroupNumber(username));
            }else{
                return new LoginUser("error", "Введённые данные не соответствуют ни одному пользователю", username, retrieveGroupNumber(username));
            }
        }

    }

    public RegisterUser registerUser(String username, String pwd, Integer isu, String group_number, String email) throws SQLException {
        try{
            setConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT into users(username, pwd, isu, group_number, email) VALUES(?,?,?,?,?)");
            ps.setString(1, username);
            ps.setString(2, md5Apache(pwd));
            ps.setInt(3, isu);
            if(group_number == null || group_number.length() == 0)
                group_number = "ITMO University";
            ps.setString(4, group_number);
            ps.setString(5, email);
            Integer operation = ps.executeUpdate();
            isRegistered = operation == 1;
        }catch (SQLException ex){
            System.out.println(ex);
        }
        finally {
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
            if(isRegistered){
                String token = Token.createToken(username);
                return new RegisterUser("success", token, username, group_number);
            }else{
                return new RegisterUser("error", "Возникла ошибка при регистрации пользователя", null, null);
            }
        }
    }

    public Boolean UserAlreadyExist(String username) throws SQLException{
        try{
            setConnection();
            PreparedStatement request = conn.prepareStatement("SELECT username FROM users WHERE username = ? ");
            request.setString(1, username);
            ResultSet resultSet = request.executeQuery();
            resultSet.last();
            if(resultSet.getRow() != 0){
                isExist = true;
            }else{
                isExist = false;
            }
        }catch (SQLException ex){
            System.out.println(ex);
        }finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
            return isExist;
        }

    }

    public String retrieveGroupNumber(String username){
        String result = null;
        try{
            setConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? ");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                result = rs.getString("group_number");
            }
        }catch (SQLException ex){
            System.out.println(ex);
        }finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
            return result;
        }
    }

    public static String md5Apache(String st) {
        String md5Hex = DigestUtils.md5Hex(st);
        return md5Hex;
    }
}
