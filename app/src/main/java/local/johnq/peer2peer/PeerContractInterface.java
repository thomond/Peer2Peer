package local.johnq.peer2peer;

import java.io.IOException;
import java.util.Observable;

import local.johnq.libpeer2peer.ClientConnection;
import local.johnq.libpeer2peer.Conversation;
import local.johnq.libpeer2peer.ListenerThread;
import local.johnq.libpeer2peer.TextMessage;

public interface PeerContractInterface {
    interface PeerPresenter  {

        void StartListening();
        void StopListening();
        ListenerThread GetListenerThread()  throws NullPointerException;


        Conversation GetActiveConversation();
        ClientConnection GetClientConnectionById(String id);

        String AddNewConnection(String host) throws IOException;

        void DisconnectAll()throws IOException;
        void Disconnect(String clientConnectionID)throws IOException, InterruptedException;

        TextMessage WaitForNewMessage() throws IOException, InterruptedException;

        void AddMessage(TextMessage textMessage) throws IOException;

    }
}
