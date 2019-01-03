package local.johnq.peer2peer;

import android.text.style.TtsSpan;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

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
    PeerPresenter presenter;

    @Test
    public void mvp_test() throws InterruptedException, IOException {
        presenter = PeerPresenter.getInstance();
        new Thread(presenter).start();
        presenter.AddNewConnection("localhost");
        presenter.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                TextMessage newMsg = (TextMessage) arg;
                presenter.GetActiveConversation().add(newMsg);
            }
        });
        Thread.sleep(5000);
        for (int i = 0; i < 3; i++) {
            try {
                presenter.AddMessage(new TextMessage("me", System.currentTimeMillis(), "Hello Server"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Thread.sleep(5000);
        presenter.DisconnectAll();
        presenter.StopListening();


        System.out.println(presenter.GetActiveConversation().getHTML());
    }

    @Test
    public void peer_send() throws IOException, InterruptedException {
        mConversation = new Conversation();
        mListener = new ListenerThread(4444);

        new Thread(mListener).start();
        mClient = new ClientConnection("localhost", 4444);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    TextMessage newMsg = null;// Blocking
                    try {
                        newMsg = mListener.WaitForNewMessage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mConversation.add(newMsg);

                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    try {

                        TextMessage mMessage = new TextMessage("localhost", System.currentTimeMillis(), "Hello Server");
                        mConversation.add(mMessage);
                        mClient.SendMessage(mMessage);
                        //mClient.SendAll();


                    } catch (IOException e) {
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

        Thread.sleep(2000);
        mClient.Disconnect();
        mListener.Disconnect();

        System.out.println(mConversation.getHTML());
    }


    @Test
    public void date_tests(){
        long now = System.currentTimeMillis();

        /*try {
            df.parse(new Date().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        System.out.println(now);
        System.out.println(new Date(now));

    }
}











