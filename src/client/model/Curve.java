package client.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
/**
 * Created by madrimas on 09.05.2017.
 */
public class Curve implements Serializable {
    private List<java.awt.geom.Point2D.Double> listOfPoints;
    private double curveWidth;
    private java.awt.Color curveColor;

    public void addPoint(double x, double y){
        listOfPoints.add(new java.awt.geom.Point2D.Double(x, y));
    }

    public Curve(double curveWidth, java.awt.Color curveColor) {
        super();
        listOfPoints = new LinkedList<>();
        this.curveWidth = curveWidth;
        this.curveColor = curveColor;
    }

    public double getLineWidth() {
        return curveWidth;
    }

    public java.awt.Color getLineColor() {
        return curveColor;
    }

    public List<java.awt.geom.Point2D.Double> getListOfPoints(){
        return listOfPoints;
    }
}
