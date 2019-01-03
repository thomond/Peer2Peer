package local.johnq.peer2peer;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import local.johnq.libpeer2peer.ClientConnection;
import local.johnq.libpeer2peer.TextMessage;

public class MainActivity extends AppCompatActivity {
    PeerPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.conversation_view);
        mPresenter = PeerPresenter.getInstance();

        //Wrap around listener thread to avoid running on main thread

        new Thread(mPresenter).start();
        Toast.makeText(getApplicationContext(), "Listener Started", Toast.LENGTH_SHORT);


        // create connection
        try {
            mPresenter.AddNewConnection("localhost");

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
        }
        Toast.makeText(getApplicationContext(), "Connected to localhost", Toast.LENGTH_SHORT);


        // Observer to wait for any incoming messages
        mPresenter.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                TextMessage newMsg = (TextMessage)arg;
                mPresenter.GetActiveConversation().add(newMsg);
                ((WebView) (findViewById(R.id.webViewConversation))).loadData( mPresenter.GetActiveConversation().getHTML(), "text/html", null);
            }
        });

    }

    public void onBtnSendPressed(View view) {
        // Todo
        try {
            String input = ((TextInputEditText) findViewById(R.id.inputMessage)).getText().toString();

            TextMessage mMessage = new TextMessage("localhost", System.currentTimeMillis(), input);

            mPresenter.AddMessage(mMessage);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            e.printStackTrace();
        }


    }


    public void onNewConnection(View view) {
        // TODO inflate dialog view and invoke conversation view
        Intent intent = new Intent(this, ConnectDialog.class);
        intent.putExtra("test", new Bundle());
        startActivity(intent);
    }

    public void ShowConversationHistory() {
        // TODO: Implement Existing conversation  logic

    }



}