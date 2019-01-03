package local.johnq.peer2peer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import local.johnq.libpeer2peer.TextMessage;

import local.johnq.libpeer2peer.*;

public final class PeerPresenter extends Observable implements Runnable,  PeerContractInterface.PeerPresenter{
    private static final PeerPresenter INSTANCE = new PeerPresenter();

    LinkedList<ClientConnection> mClientList;
    ListenerThread mListener;
    private Logger logger;
    Conversation mConversation;



    private PeerPresenter(){
        mConversation = new Conversation();
        mClientList = new LinkedList();
        mListener = new ListenerThread(12345);
        logger = Logger.getLogger("peer2peer");
        logger.addHandler(new ConsoleHandler());
    }

    public static PeerPresenter getInstance(){
        return INSTANCE;
    }

    public void StartListening() {
        new Thread(mListener).start();

    }

    public void StopListening() {
       // mListener.
        //mListener.destroy();
        mListener = null;
    }


    public ListenerThread GetListenerThread() throws NullPointerException {
        if (mListener == null)
            throw new NullPointerException("Listener May not have been started...");
        else
            return mListener;
    }

    public String AddNewConnection(String host) throws IOException{
        try {
            ClientConnection mConn = new ClientConnection(host,12345);
            new Thread(mConn).start();
            mClientList.add(mConn);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return mClientList.getLast().GetConnectionID();
    }

    public void DisconnectAll()throws IOException{
        // TODO:
        try {
            for (ClientConnection i : mClientList) {
                i.Disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public void Disconnect(String clientConnectionID) throws IOException, InterruptedException {
        throw new UnsupportedOperationException();
    }


    public TextMessage WaitForNewMessage() throws IOException, InterruptedException {
        return mListener.WaitForNewMessage();
    }

    public void AddMessage(TextMessage textMessage) throws IOException
    {
        mClientList.getLast().SendMessage(textMessage);
        GetActiveConversation().add(textMessage);

    }

    public Conversation GetActiveConversation() throws NullPointerException {
        if (mConversation == null)
            throw new NullPointerException("Conversation is NULL...");
        else
            return mConversation;
    }

    @Override
    public ClientConnection GetClientConnectionById(String id) {
        return null;
    }


    @Override
    public void run() {
        StartListening();
        while(true){
            try {
                // wait for new message and then notify observers
                TextMessage t = WaitForNewMessage();
                setChanged();
                notifyObservers(t);
            } catch (IOException e) {
                logger.log(Level.SEVERE ,e.getMessage());
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE ,e.getMessage());
            }

        }


    }
}
