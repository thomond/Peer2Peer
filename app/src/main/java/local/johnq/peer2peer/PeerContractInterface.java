package local.johnq.peer2peer;

import java.io.IOException;

import local.johnq.libpeer2peer.ClientConnection;
import local.johnq.libpeer2peer.Conversation;
import local.johnq.libpeer2peer.ListenerThread;
import local.johnq.libpeer2peer.TextMessage;

public interface PeerContractInterface {
    interface PeerPresenter{

        void StartListening();
        void StopListening();
        ListenerThread GetListenerThread();
        TextMessage WaitForNewMessage() throws IOException, InterruptedException;

        Conversation GetActiveConversation();
        ClientConnection GetClientConnectionById(String id);

        String AddNewConnection(String host) throws IOException;

        void DisconnectAll()throws IOException;
        void Disconnect(String clientConnectionID)throws IOException, InterruptedException;

        void SendMessage(String clientConnectionID, TextMessage msg)throws IOException;

        void AddMessage(TextMessage textMessage) throws IOException;

    }
}
