package local.johnq.libpeer2peer;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

public class ClientConnection {
    String mHost;
    int mPort;
    private Packet connection;
    private String mConnectionID;
    private LinkedTransferQueue<TextMessage> outQueue; // Outgoing message queue
    public ClientConnection(String url, int port) throws IOException {
        mHost = url;
        mPort = port;
        outQueue = new LinkedTransferQueue();

        // Open new socket to listening server
        // Create and populate new Response
        try {
            connection = new Packet(new Socket(mHost,mPort));

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Generate a new UUID for the the connection
        mConnectionID = UUID.randomUUID().toString();
    }


    // Overrides the outqueue and send directly
    public void SendMessage(TextMessage textMessage) throws IOException {
            // Todo: implement protocol, maybe?
            connection.Write(textMessage.text);

    }

    public boolean HasPendingSends(){
        return !outQueue.isEmpty();
    }

   // Clears out the queue and sends each message
    public void SendAll() throws IOException{
        try {
           // TextMessage nextMsg;

            //while  (  (nextMsg = outQueue.poll())!= null){
               // SendMessage(nextMsg);
                connection.Flush();
            //}
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void AddToQueue(TextMessage textMessage) throws IOException{
        //outQueue.add(textMessage);
        connection.Write(textMessage.text);
    }

    public void Disconnect() throws IOException {
            // Todo: implement protocol, maybe?
            connection.Close();


    }

    public String GetConnectionID() {
        return mConnectionID;
    }

}
