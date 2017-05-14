package client.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import client.model.Curve;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by madrimas on 09.05.2017.
 */
public class DrawingController {

    private Color color;
    private GraphicsContext graphicsContext;
    private double curveWidth;
    private List<Curve> curveList;
    private Connection connection;

    @FXML
    private javafx.scene.canvas.Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private void handleClear(){
        double canvasHeight = graphicsContext.getCanvas().getHeight();
        double canvasWidth = graphicsContext.getCanvas().getWidth();

        graphicsContext.setStroke(Color.AQUAMARINE);
        graphicsContext.setLineWidth(4);

        //graphicsContext.fill();
        graphicsContext.setFill(Color.GREY);
        graphicsContext.fillRect(0,0,canvasWidth, canvasHeight);
        graphicsContext.strokeRect(
                0,
                0,
                canvasWidth,
                canvasHeight
        );
        graphicsContext.setStroke(color);
        graphicsContext.setLineWidth(curveWidth);
    }
    @FXML
    private void handleConnect(){
        connection = new Connection(this);
    }
    @FXML
    private void handleRedraw(){
        for(Curve curve:curveList){
            drawCurve(curve);
        }
    }
    @FXML
    private void initialize(){
        curveList = new LinkedList<>();
        curveWidth = 10;
        graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);
        colorPicker.setOnAction(event -> {
            color = colorPicker.getValue();
            graphicsContext.setStroke(color);
            colorPicker.setValue(color);
        });

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    curveList.add(new Curve(curveWidth, fxToAwtColor(color)));
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(event.getX(), event.getY());
                    curveList.get(curveList.size()-1).addPoint(event.getX(), event.getY());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                    graphicsContext.lineTo(event.getX(), event.getY());
                    curveList.get(curveList.size()-1).addPoint(event.getX(), event.getY());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                event -> {
            if(curveList.get(curveList.size() - 1).getListOfPoints().size()>1) {
                connection.sendCurve(curveList.get(curveList.size() - 1));
            }
                });
    }
    public void drawCurve(Curve curve){
        if(curve != null) {
            graphicsContext.beginPath();
            graphicsContext.moveTo(curve.getListOfPoints().get(0).getX(), curve.getListOfPoints().get(0).getY());
            graphicsContext.setStroke(awtToFxColor(curve.getLineColor()));
            graphicsContext.stroke();
            for (java.awt.geom.Point2D point : curve.getListOfPoints()) {
                graphicsContext.lineTo(point.getX(), point.getY());
                graphicsContext.stroke();
            }
        }

    }
    private Color awtToFxColor(java.awt.Color color){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();
        double opacity = a / 255.0;

        Color colorRGB = Color.rgb(r, g, b, opacity);
        return colorRGB;
    }
    private java.awt.Color fxToAwtColor(Color color){
        java.awt.Color colorRGB = new java.awt.Color( (float) color.getRed(),
                (float) color.getGreen(),
                (float) color.getBlue(),
                (float) color.getOpacity());
        return colorRGB;
    }

    private void initDraw(GraphicsContext gc){
        handleClear();
    }
}
