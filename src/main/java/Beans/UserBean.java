package Beans;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Entity(name="users")
public class UserBean implements Serializable {
    @Getter
    @Setter
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "entity_id_seq"
    )
    @SequenceGenerator(
            name = "entity_id_seq",
            sequenceName = "global_id_sequence",
            allocationSize = 1
    )
    @Column(
            name = "id",
            unique = true,
            updatable = false,
            nullable = false
    )
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
