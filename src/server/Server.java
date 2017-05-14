package server;

import client.model.Curve;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by madrimas on 09.05.2017.
 */
public class Server implements Runnable {
    private final static int PORT = 1234;
    private ServerSocket serverSocket;
    private Set<Handler> clientSet;

    @Override
    public void run(){
        while (true){
            try{
                Socket clientSocket = serverSocket.accept();
                Handler clientHandler = new Handler(clientSocket, this);
                clientSet.add(clientHandler);
                clientHandler.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private Server(){
        try{
            serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);
            clientSet = Collections.synchronizedSet(new HashSet<Handler>());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void broadcast(Handler clientHandlerSource, Curve curve) throws Exception {
        for(Handler client: clientSet){
            if(client != clientHandlerSource && !client.getSocket().isClosed()){
                ObjectOutputStream outputStream = client.getOutputStream();
                outputStream.writeObject(curve);
                outputStream.flush();
            }
        }
    }
    public static void main(String[] args){
        new Server().run();
    }
}
