package local.johnq.peer2peer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import local.johnq.libpeer2peer.TextMessage;

import local.johnq.libpeer2peer.*;

public class PeerPresenter implements PeerContractInterface.PeerPresenter{
    LinkedList<ClientConnection> mClientList;
    ListenerThread mListener;
    private Logger logger;
    Conversation mConversation;



    PeerPresenter(){
        mConversation = new Conversation();
        mClientList = new LinkedList();
        mListener = new ListenerThread(9999);
    }

    public void StartListening() {
        mListener.run();

    }

    public void StopListening() {
        mListener.stop();
        mListener.destroy();
        mListener = null;
    }

    @Override
    public ListenerThread GetListenerThread() throws NullPointerException {
        if (mListener == null)
            throw new NullPointerException("Listener May not have been started...");
        else
            return mListener;
    }

    public String AddNewConnection(String host) throws IOException{
        try {
            ClientConnection mConn = new ClientConnection(host,9999);
            //new Thread(mConn).start();
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


    public void SendMessage(String clientConnectionID, TextMessage msg) throws IOException {
        throw new UnsupportedOperationException();
    }


    public TextMessage WaitForNewMessage() throws IOException, InterruptedException {
        return mListener.waitForNewMessage();
    }

    public void AddMessage(TextMessage textMessage) throws IOException
    {
        mClientList.getLast().AddToQueue(textMessage);
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


}
