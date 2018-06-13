package local.johnq.peer2peer;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import local.johnq.libpeer2peer.Peer;

public class MainActivity extends AppCompatActivity {
    Peer peer;
    TextView textViewLog, textViewinput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Handle logging
        // Handle logging
//        Handler handler =new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                {
//                    textViewLog.setText(peer.getLog());
//                }
//            }},200);


    }

//public void updateLogUI(final String log){
//    runOnUiThread(new Runnable() {
//        @Override
//        public void run() {
//            {
//                textViewLog.setText(log);
//            }
//        }});
//    }
}