package local.johnq.libpeer2peer;

import java.util.Date;


public class TextMessage {
        public String sender;
        public Direction direction;
        public long sendTime;
        public long recvTime;
        public String text;

        public TextMessage(){};

        public TextMessage(String _name, long _time, String _text){
            sender = _name;
            sendTime = _time;
            text = _text;
            direction = null;
        }

    public static enum Direction {
            OUTGOING,INCOMING;
    }
}
