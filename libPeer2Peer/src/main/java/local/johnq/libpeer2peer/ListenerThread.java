package local.johnq.libpeer2peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdk.nashorn.internal.runtime.options.LoggingOption;

// Thread for incoming connections
public class ListenerThread extends Thread {

    int port;
    private LinkedBlockingQueue<TextMessage> inQueue; // Incoming message queue
    ServerSocket serverSocket;
    Packet incomingPacket;

    Logger logger;
    public ListenerThread(int _port){
        super("Listener");
        port = _port;
        logger = Logger.getLogger("peer2peer");
        logger.addHandler(new ConsoleHandler());
        inQueue = new LinkedBlockingQueue();
    }

    public TextMessage OnRecieveMessage() throws IOException {
        // TODO: return buffer of -1 if EOF and return NULL textmesgae
        //Populate the request from the clientl
        byte buffer[] = incomingPacket.Read();

        //logger.log(Level.INFO,new Timestamp(System.currentTimeMillis()) +" | TextMessage from "+ packet.() + ": " + new String(buffer));
        return new TextMessage(incomingPacket.getRemoteAddr().toString(), new Date(), new String(buffer) );

    }

    /*
        waits for new message to be present in queue and then returns it immediately
     */
    public TextMessage waitForNewMessage() throws InterruptedException {
        return inQueue.take();
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while(true){

                Socket incoming = serverSocket.accept();
                incomingPacket = new Packet(incoming);
                while(!incoming.isClosed()){
                    //TextMessage incomingData = OnRecieveMessage(incomingPacket);
                }
               // logger.log(Level.INFO,"Connection From: " + incoming.getInetAddress() + " Port: " + incoming.getPort() + "\n");

               // while ())!= null){
                    // Add message to queue
                 //   inQueue.add(incomingData) ;
               // }

                //incoming.close();
              }


        }catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE ,e.getMessage());

        }
     }
}

