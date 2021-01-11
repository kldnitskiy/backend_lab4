package Beans;

import Entities.UserProfile;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.persistence.*;
import java.io.Serializable;

@Entity(name="username")
public class UserBean implements Serializable {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private Integer isu;
    @Getter
    @Setter
    private String group_number;
    @Getter
    @Setter
    private String email;

    public UserBean(){

    }

    public UserBean(String username, String pwd, Integer isu, String group_number, String email){
        this.username = username;
        this.password = pwd;
        this.isu = isu;
        this.group_number = group_number;
        this.email = email;
    }

}
