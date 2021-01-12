package Model;

import Beans.PointsBean;
import Beans.UserBean;
import Responses.LoginUser;
import Responses.RegisterUser;
import Token.Token;
import org.apache.activemq.leveldb.replicated.dto.Login;
import org.apache.activemq.store.kahadb.disk.page.Transaction;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.dbutils.DbUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Stateless
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


    public void savePoint(Float x, Float y, Float r, Boolean status, String username){
        PointsBean points = new PointsBean(x,y,r,status,username);
        entityManager.persist(points);
    }
    @Transactional
    public List<PointsBean> getPoint(String username) throws SQLException {
        Query query = entityManager.createQuery("SELECT a FROM points a where a.username = :username", PointsBean.class);
        query.setParameter("username" , username);
        List<PointsBean> list = query.getResultList();
        return list;
    }

    @Transactional
    public LoginUser loginUser(String username, String pwd) {
        Query query = entityManager.createQuery("SELECT a FROM users a where a.username = :username and a.password = :pwd", UserBean.class);
        query.setParameter("username" , username);
        query.setParameter("pwd", md5Apache(pwd));
        List<UserBean> list = query.getResultList();
        System.out.println(list.size());
        if(list.size() != 0){
            String token = Token.createToken(username);
            return new LoginUser("success", token, username, "Университет ИТМО");
        }
        return new LoginUser("error", "Введённые данные не соответствуют ни одному пользователю", username, "Университет ИТМО");
    }
    @Transactional
    public RegisterUser registerUser(String username, String pwd, Integer isu, String group_number, String email) throws SQLException {
        UserBean user = new UserBean(username, md5Apache(pwd), isu, group_number, email);
        entityManager.persist(user);
        String token = Token.createToken(username);
        return new RegisterUser("success", token, username, "Университет ИТМО");
    }
    public Boolean userAlreadyExisted(String username) {
        Query query = entityManager.createQuery("SELECT a FROM users a where a.username = :username", UserBean.class);
        query.setParameter("username" , username);
        List<UserBean> list = query.getResultList();
        System.out.println(list.size());

        if(list.size() != 0){
            return false;
        }
        return true;
    }

    public void retrieveGroupNumber(String username){
//        String result = null;
//        try{
//            setConnection();
//            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? ");
//            ps.setString(1, username);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                result = rs.getString("group_number");
//            }
//        }catch (SQLException ex){
//            System.out.println(ex);
//        }finally {
//            DbUtils.closeQuietly(rs);
//            DbUtils.closeQuietly(ps);
//            DbUtils.closeQuietly(conn);
//            return result;
//        }
    }

    public static String md5Apache(String st) {
        String md5Hex = DigestUtils.md5Hex(st);
        return md5Hex;
    }
}
