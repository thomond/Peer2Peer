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

import local.johnq.libpeer2peer.ClientConnection;
import local.johnq.libpeer2peer.TextMessage;

public class MainActivity extends AppCompatActivity {
    PeerPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.conversation_view);
        mPresenter = new PeerPresenter();

        //Wrap around listener thread to avoid running on main thread

        new Thread(mPresenter.GetListenerThread()).start();
        Toast.makeText(getApplicationContext(), "Listener Started", Toast.LENGTH_SHORT);


        // create connection
        try {
            mPresenter.AddNewConnection("localhost");

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
        }
        Toast.makeText(getApplicationContext(), "Connected to localhost", Toast.LENGTH_SHORT);

        /*
            Runnable to wait for new message and update UI/activity related elements
        */
        String webViewStr = "";
        //((WebView) (findViewById(R.id.webViewConversation))).loadData(webViewStr, "text/html", null);

        new Thread() {
            @Override
            public void run() {
                while(true){
                    try {
                        // Wait in BG for new message and add to current conversation
                        TextMessage msg = mPresenter.GetListenerThread().waitForNewMessage();
                        mPresenter.GetActiveConversation().add(msg);
                        // Update webview with conversation
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((WebView) (findViewById(R.id.webViewConversation))).loadData( mPresenter.GetActiveConversation().getHTML(), "text/html", null);
                            }
                        });
                    } catch (InterruptedException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                        e.printStackTrace();
                    }
                }

            }
        }.start();
        new Thread(){
            @Override
            public void run(){
                while(true){
                    try {// If there is clients and it has pending messages try to send them all
                        if(!mPresenter.mClientList.isEmpty()) {
                            for(ClientConnection c :mPresenter.mClientList){
                                if(c.HasPendingSends())
                                    c.SendAll();
                            }

                        }
                    }catch (IOException e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                        e.printStackTrace();
                    }

                }

            }
        }.start();

    }

    public void onBtnSendPressed(View view) {
        // Todo
        try {
            String input = ((TextInputEditText) findViewById(R.id.inputMessage)).getText().toString();

            TextMessage mMessage = new TextMessage("localhost", new Date(), input);

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