package local.johnq.libpeer2peer;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection implements Runnable{
    String mHost;
    int mPort;
    private Socket connection;
    private String mConnectionID;
    private LinkedTransferQueue<TextMessage> outQueue; // Outgoing message queue
    Socket incoming;
    Logger logger;

    public ClientConnection(String url, int port)  throws IOException {
        mHost = url;
        mPort = port;
        outQueue = new LinkedTransferQueue();
        logger = Logger.getLogger("peer2peer");
        logger.addHandler(new ConsoleHandler());

    }


    // Overrides the outqueue and send directly
    public void SendMessage(TextMessage textMessage) throws IOException {
        // Todo: test and extend protocol
        // Considering some value like : are used in protocol sanitize input to avoid mismatches
        textMessage.text = textMessage.text.replaceAll(":","\\:");
        String sendText = "date: " +textMessage.sendTime+"\n" +
                "from: " +textMessage.sender+"\n" +
                "msg: "+textMessage.text +
                "\n"+"\n";
        connection.getOutputStream().write(sendText.getBytes());

    }

    public boolean HasPendingSends(){
        return !outQueue.isEmpty();
    }

   // Clears out the queue and sends each message
    public void SendAll() throws IOException{
        try {
                connection.getOutputStream().flush();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void AddToQueue(TextMessage textMessage) throws IOException{
        //outQueue.add(textMessage);
        connection.getOutputStream().write(Byte.valueOf(textMessage.text));
    }

    public void Disconnect() throws IOException {
            connection.close();


    }

    public String GetConnectionID() {
        return mConnectionID;
    }

    @Override
    public void run() {
        // Open new socket to listening server
        // Create and populate new Response
        try {
            connection = new Socket(mHost,mPort);

        } catch (IOException e) {
            logger.log(Level.SEVERE ,e.getMessage() );
        }
        // Generate a new UUID for the the connection
        mConnectionID = UUID.randomUUID().toString();
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
