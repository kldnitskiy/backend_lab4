package Beans;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PointsBean {
    private double x;
    private double y;
    private double r;
    private boolean res;
    @Id
    private Integer id;

    public double getY() { return y; }
    public double getX() { return x; }
    public double getR() { return r; }
    public boolean getRes() { return res; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setR(double r) { this.r = r; }
    public void setRes(boolean res){ this.res = res; }
}
