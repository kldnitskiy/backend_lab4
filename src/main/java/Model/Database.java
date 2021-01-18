package Model;

import Beans.PointsBean;
import Beans.UserBean;
import Responses.LoginUser;
import Responses.RegisterUser;
import Token.Token;
import org.apache.commons.codec.digest.DigestUtils;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
public class Database {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory( "sample" );
    EntityManager entityManager = emf.createEntityManager();

    @Transactional
    public void savePoint(Float x, Float y, Float r, Boolean status, String username){
        PointsBean points = new PointsBean(x,y,r,status,username);
        System.out.println(x + "/" + y + "/" + r + "/" + status);
        entityManager.persist(points);
    }
    @Transactional
    public List<PointsBean> getPoint(String username){
        Query query = entityManager.createQuery("SELECT a FROM PointsBean a where a.username = :username", PointsBean.class);
        query.setParameter("username" , username);
        List<PointsBean> list = query.getResultList();
        return list;
    }

    @Transactional
    public LoginUser loginUser(String username, String pwd) {
        Query query = entityManager.createQuery("SELECT a FROM UserBean a where a.username = :username and a.password = :pwd", UserBean.class);
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
    public RegisterUser registerUser(String username, String pwd, Integer isu, String group_number, String email){
        UserBean user = new UserBean(username, md5Apache(pwd), isu, group_number, email);
        entityManager.persist(user);
        String token = Token.createToken(username);
        return new RegisterUser("success", token, username, "Университет ИТМО");
    }
    public Boolean userAlreadyExisted(String username) {
        Query query = entityManager.createQuery("SELECT a FROM UserBean a where a.username = :username", UserBean.class);
        query.setParameter("username" , username);
        List<UserBean> list = query.getResultList();
        System.out.println(list.size());

        if(list.size() != 0){
            return false;
        }
        return true;
    }


    public static String md5Apache(String st) {
        String md5Hex = DigestUtils.md5Hex(st);
        return md5Hex;
    }
}
