package local.johnq.peer2peer;

import android.app.Activity;
import android.content.Intent;
import android.net.IpPrefix;
import android.net.Network;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectDialog extends Activity {
    PeerPresenter peerPre; // The peer presenter obj
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_dialog);
    }

    protected void  onCancelBtnPressed(View view){
        //TODO:
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

    }

    protected void  onConnectBtnPressed(View view){
       // Intent intent = new Intent(this, ConversationActivity.class);
        TextInputEditText inputURL = findViewById(R.id.inputURL);
        InetAddress addr;

        try {
             addr = InetAddress.getByName(inputURL.getText().toString());
        } catch (UnknownHostException e) {
            // todo logger
            e.printStackTrace();
            return;
        }

       // intent.putExtra("host",addr.getHostAddress());

        //startActivity(intent);
    }
}
