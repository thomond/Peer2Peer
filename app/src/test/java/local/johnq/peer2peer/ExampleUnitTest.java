package local.johnq.peer2peer;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

import local.johnq.libpeer2peer.ClientConnection;
import local.johnq.libpeer2peer.Conversation;
import local.johnq.libpeer2peer.ListenerThread;
import local.johnq.libpeer2peer.TextMessage;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    Conversation mConversation;
    ListenerThread mListener;
    ClientConnection mClient;
    @Test
    public void peer_send() throws IOException {
        mConversation = new Conversation();
        mListener =  new ListenerThread(9999);

        mListener.start();
        mClient = new ClientConnection("localhost", 9999);
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 2; i++) {
                    try {
                        Thread.sleep(1000);

                        TextMessage mMessage = new TextMessage("localhost", new Date(), "Hello Server");
                        mConversation.add(mMessage);
                        mClient.SendMessage(mMessage);
                        //mClient.SendAll();


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    mClient.Disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    while(true){
                        Thread.sleep(6000);
                        System.out.println(mConversation.getHTML());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        while(true){
            try {
                    TextMessage newMsg = mListener.OnRecieveMessage();// Blocking
                    //System.out.println(newMsg.sendTime + ": " + newMsg.text + " ... " +newMsg.sender);
                    mConversation.add(newMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

}











