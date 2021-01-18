package Beans;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class UserBean implements Serializable {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "usersProducer")
    @SequenceGenerator(name = "usersProducer", sequenceName = "users_id_seq", initialValue = 1, allocationSize = 1)
    @Column(nullable = false, name = "id")
    private Integer id;
    @Getter
    @Setter
    @Column(nullable = false, name = "username")
    private String username;
    @Getter
    @Setter
    @Column(nullable = false, name = "password")
    private String password;
    @Getter
    @Setter
    @Column(nullable = false, name = "isu")
    private Integer isu;
    @Getter
    @Setter
    @Column(nullable = false, name = "group_number")
    private String group_number;
    @Getter
    @Setter
    @Column(nullable = false, name = "email")
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
