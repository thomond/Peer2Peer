package local.johnq.peer2peer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import local.johnq.libpeer2peer.ListenerThread;
import local.johnq.libpeer2peer.TextMessage;

public class ListenerService extends IntentService {
    static ListenerThread mListener;

    public ListenerService() {
        super("ListenerService");
    }


    public static void StopListening() {
        mListener.stop();
        mListener.destroy();
        mListener = null;
    }

    public static TextMessage OnRecieveMessage()throws Exception {
        // TODO:
        //mListener.OnRecieveMessage();
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public void onCreate() {

        mListener = new ListenerThread(9999);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        mListener.run();
        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            OnRecieveMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
