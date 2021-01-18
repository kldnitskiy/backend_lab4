package Beans;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "points")
public class PointsBean {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pointsProducer")
    @SequenceGenerator(name = "pointsProducer", sequenceName = "points_id_seq", initialValue = 1, allocationSize = 1)
    @Column(nullable = false, name = "id")
    private Integer id;
    @Getter
    @Setter
    @Column(nullable = false, name = "x")
    private float x;
    @Getter
    @Setter
    @Column(nullable = false, name = "y")
    private float y;
    @Getter
    @Setter
    @Column(nullable = false, name = "r")
    private float r;
    @Getter
    @Setter
    @Column(nullable = false, name = "status")
    private boolean status;
    @Getter
    @Setter
    @Column(nullable = false, name = "username")
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
