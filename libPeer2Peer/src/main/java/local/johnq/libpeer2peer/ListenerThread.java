package local.johnq.libpeer2peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

// Thread for incoming connections
public class ListenerThread implements Runnable {

    int port;
    private LinkedBlockingQueue<TextMessage> inQueue; // Incoming message queue
    ServerSocket serverSocket;
    Socket incoming;
    Logger logger;


    public ListenerThread(int _port){
        port = _port;
        try {
            serverSocket = new ServerSocket(port);

        incoming = new Socket();
        incoming.setReuseAddress(true);
        logger = Logger.getLogger("peer2peer");
        logger.addHandler(new ConsoleHandler());
        inQueue = new LinkedBlockingQueue();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    // TODO: test protocol handling some more
    public int ReadFromSocket() throws IOException {
        //Populate the request from the clientl
        int msgsAdded=0;
        byte[] buffer = new byte[512];
        int ret = incoming.getInputStream().read(buffer);
        if( ret == -1) {// EOF returned
            incoming.close();
            return -1;
        }


        // Multiple messages split by double newline
        // with message text prefixed by 'msg:' which denotes a paramter
        String newmsgs[] = (new String(buffer)).split("\r\n\r\n");
        for(String newmsg : newmsgs){
            TextMessage mTextMessage = new TextMessage();
            if (newmsg.isEmpty()) continue;// don't process empty strings
            String lines[] = newmsg.split("\n");
            for(String line : lines) {
                String field = line.split(": ")[0];// split by colon which denotes parameter
                switch(field){
                    // Process msg parameter
                    case "msg":{
                        // Sanitize message and add to our object
                        String msg = line.split(": ")[1];
                        msg = msg.replaceAll("\\:",":");
                        mTextMessage.text = msg;
                        continue;
                    }
                    case "date":{
                        try {
                            mTextMessage.sendTime = Long.valueOf(line.split(": ")[1]);
                        }catch (NumberFormatException e){
                            logger.log(Level.SEVERE , e.getMessage());
                        }
                        continue;
                    }
                    case "from":{
                        String from = line.split(": ")[1];
                        from = from.replaceAll("\\:",":");
                        mTextMessage.sender = from;
                        continue;
                    }
                    default:
                        continue;
                }
            }
            if(!mTextMessage.text.isEmpty() && !mTextMessage.sender.isEmpty() && mTextMessage.sendTime!=0) {
                inQueue.add(mTextMessage);
                msgsAdded++;
            }else{
                logger.log(Level.SEVERE ,"Required attributes not found: "+mTextMessage.text +" "+ mTextMessage.sender +" "+mTextMessage.sendTime );
            }


        }



        // Number of messages processed
        return msgsAdded;

    }

    /*
        waits for new message to be present in queue and then returns it immediately
     */
    public TextMessage WaitForNewMessage() throws InterruptedException {
        return inQueue.take();
    }

    public boolean IsSocketOpen(){
        if(incoming.isConnected() && !incoming.isClosed())
            return true;
        else
            return false;
    }

    @Override
    public void run() {
        try {

            while(true){
                incoming = new Socket();
                if(serverSocket.isClosed())// serverSocket has been closed, break of out loop
                    break;
                incoming = serverSocket.accept();
                logger.log(Level.INFO,"Connection From: " + incoming.getInetAddress() + " Port: " + incoming.getPort() + "\n");
                while(!incoming.isClosed()){ // This will close depending on whats read from socket
                    ReadFromSocket();
                }
                logger.log(Level.INFO,"Connection " + incoming.getInetAddress() + " is now CLOSED\n");

              }
        }catch (IOException e) {
            logger.log(Level.SEVERE ,e.getMessage());

        }
     }

    public void Disconnect() throws IOException {
        if(serverSocket != null && !serverSocket.isClosed())
            serverSocket.close();
        if ((incoming != null) && (incoming.isClosed()))
            incoming.close();
    }
}

