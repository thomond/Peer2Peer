package local.johnq.libpeer2peer;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.sql.Timestamp;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Peer {
    private final int port = 9999;
    private final String TAG = "local.johnq.peer2peer";
    Logger logger;


    // Threa
    // d for incoming connections
    private class ListeningThread extends Thread {
        int port;

        ListeningThread(int _port){

            port = _port;
        }


        @Override
        public void run(){
            try{
                ServerSocket serverSocket = new ServerSocket(port);
                while(true){
                    Socket incoming = serverSocket.accept();

                    logger.log(Level.WARNING,"\n Connection From: " + incoming.getInetAddress() + " Port: " + incoming.getPort() + "\n");
                    // Move to UI threadand setlog text there
                    //Populate the request from the client
                    Packet clientReq = new Packet(incoming);
                    byte buffer[] = clientReq.Read();
                    logger.log(Level.INFO,new Timestamp(System.currentTimeMillis()) +" | Message from "+ incoming.getInetAddress() + ": " + new String(buffer));

                    // write back
                    //clientReq.Write("Hello client!/n!");


                    incoming.close();

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class Client {
        Socket socket;
        Packet connection;

        public Client(String url, int port) {
            try{
                // Open new socket to listening server
                socket = new Socket(url,port);
                // Create and populate new Response
                connection = new Packet(socket);

            }catch (Exception e){
                e.printStackTrace();
            }
        }



        public void SendMessage(String message){
            try {
                connection.Write(message);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }


    }

    // Thread for outgoing connections
   private class Packet {
        private Socket packSock;
        private OutputStream ostream;
        private InputStream istream;
        public Packet(Socket _packSock){
            try{
                packSock = _packSock;
                // Get output stream and write to it
                ostream = packSock.getOutputStream();
                istream = packSock.getInputStream();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        public byte[] Read(){
            try {
                byte[] buff = new byte[256];
                istream.read(buff);
                return buff;
            }catch(Exception e) {
                logger.log(Level.WARNING,e.toString());
            }
            return null;
        }

        /*
            Write to the socket's output stream
         */
        public void Write(String msg){
            try {
                ostream.write(msg.getBytes());
                ostream.flush();
                //logger.log(Level.FINER,"Wrote "+ msg.length() + " bytes");

            }catch(Exception e){
                logger.log(Level.WARNING,e.toString());
            }
        }
    }

    Thread listeningThread;
    Client client;
   public Peer(){
        logger = Logger.getLogger("Peer2Peer");
        logger.setLevel(Level.ALL);
        logger.addHandler(new ConsoleHandler());
    }

    public void StartListening(int listenport){
        listeningThread = new Thread(new ListeningThread(listenport));
        listeningThread.start();
//         logger.log(Level.FINER,   "Listening on port " + listenport);
    }
    
    public void Connect(String url, int port){
        client = new Client(url, port);


    }

    public void SendMessage(String Message){
           client.SendMessage(Message);
    }
    
}
