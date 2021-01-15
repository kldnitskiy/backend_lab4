package Beans;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="points")
public class PointsBean {
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
    private float x;
    @Getter
    @Setter
    private float y;
    @Getter
    @Setter
    private float r;
    @Getter
    @Setter
    private boolean status;
    @Getter
    @Setter
    private String username;

    public PointsBean(){

    }
    public PointsBean(Float x, Float y, Float r, Boolean status, String username) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.status = status;
        this.username = username;
    }
}
