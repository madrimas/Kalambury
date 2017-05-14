package client.controller;

import client.model.Curve;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by madrimas on 09.05.2017.
 */
public class Connection{
    private Socket clientSocket;
    private final String addressIP = "localhost";
    private final int PORT = 1234;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private DrawingController drawingCtrl;

    public Connection(DrawingController drawingController){
        this.drawingCtrl = drawingController;
        InetAddress host = null;
        try {
            host = InetAddress.getByName(addressIP);
        } catch (Exception e){
            e.printStackTrace();
        }
        if(host != null){
            try {
                clientSocket = new Socket(host, PORT);
                clientSocket.setReuseAddress(true);
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
                Thread thread = new Thread(() -> {
                    try {
                        while (true){
                            Object object = inputStream.readObject();
                            if(object instanceof Curve){
                                Curve curve = (Curve) object;
                                drawingCtrl.drawCurve(curve);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.setDaemon(true);
                thread.start();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void sendCurve(Curve curve){
        try {
            outputStream.writeObject(curve);
            outputStream.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
