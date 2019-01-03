package local.johnq.libpeer2peer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

// obj for outgoing connections
public class Packet {
    private Socket packSock;
    private OutputStream ostream;
    private InputStream istream;
    private InetAddress local;
    private InetAddress remote;


    public Packet(Socket _packSock) throws IOException {
        packSock = _packSock;
        local = packSock.getLocalAddress();
        remote = packSock.getInetAddress();
        // Get output stream and write to it
        ostream = packSock.getOutputStream();
        istream = packSock.getInputStream();

    }

    public byte[] Read() throws IOException {

        byte[] buff = new byte[256];
        int ret = istream.read(buff);
        // Check for EOF
        if (ret == -1)
            buff[0]=(byte)ret;
        return buff;
    }

    /*
        Write to the socket's output stream
     */
    public void Write(String msg) throws IOException {
        ostream.write(msg.getBytes());

    }

    public void Flush() throws IOException {
        ostream.flush();
    }

    public InetAddress getLocalAddr() {
        return local;
    }

    public InetAddress getRemoteAddr() {
        return remote;
    }

    public void Close() throws IOException {

        packSock.shutdownInput();
        packSock.shutdownOutput();
        packSock.close();
    }
}
