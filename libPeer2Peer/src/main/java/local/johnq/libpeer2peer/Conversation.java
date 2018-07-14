package local.johnq.libpeer2peer;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;


// TODO:
public class Conversation extends ConcurrentLinkedQueue<TextMessage> {

    synchronized public String getHTML(){

        String mHTMLtext = "<HTML><BODY>\n";
        for (TextMessage mMsg : this) {
            mHTMLtext += "<p>" + mMsg.sender + " " + mMsg.sender + ": " + mMsg.text + "</p>\n";
        }
        mHTMLtext += "</BODY></HTML>\n";

        return mHTMLtext;
    }


}
