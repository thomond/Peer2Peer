package local.johnq.libpeer2peer;

import java.util.Date;


public class TextMessage {
        public String sender;
        public Direction direction;
        public Date sendTime;
        public Date recvTime;
        public String text;

        public TextMessage(String _name, Date _time, String _text){
            sender = _name;
            sendTime = _time;
            text = _text;
            direction = null;
        }

    public static enum Direction {
            OUTGOING,INCOMING;
    }
}
