package server;

import client.model.Curve;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by madrimas on 09.05.2017.
 */
public class Handler extends Thread {
    private Server server;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Handler(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
            while(true) {
                try {
                    Object object = inputStream.readObject();
                    if (object instanceof Curve) {
                        server.broadcast(this, (Curve) object);
                    }
                }catch (SocketException se){
                    try {
                        getSocket().close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
    }
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }
}
