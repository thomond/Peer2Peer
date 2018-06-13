package local.johnq.libpeer2peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static java.lang.Thread.*;

public class Peer2Peer {

    public static void main(String[] args) throws InterruptedException, IOException {
        Peer peer = new Peer();
        peer.StartListening(9999);
        peer.Connect("localhost",9999);
        peer.SendMessage("Hello Server!!");


    }
}
