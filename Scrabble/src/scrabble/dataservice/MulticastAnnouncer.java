package scrabble.dataservice;

/**
 *
 * @author Peral
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;

public class MulticastAnnouncer
    implements Runnable {
  InetAddress discoveryGroup;
  MulticastSocket discoverySocket;
  List listeners = new ArrayList();
  Map timeTracker = new HashMap();
  
  private boolean done = false;
  private boolean announcer = false;
// half a second btw them
  private static final long HEARTBEAT_TIME = 2000;

  private DatagramPacket hbDatagram;
  private static final int DISCOVERY_PORT = 8976;
  String name;

  public MulticastAnnouncer(String _name) {
    try {
      discoveryGroup = InetAddress.getByName("228.9.8.7");
      discoverySocket = new MulticastSocket(DISCOVERY_PORT);
      discoverySocket.joinGroup(discoveryGroup);
      discoverySocket.setTimeToLive(1); // run on local only
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    name = _name;
  
    byte[] hbdata = null;
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(name);
      oos.flush();
      hbdata = baos.toByteArray();
      oos.close();
      baos.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    hbDatagram = new DatagramPacket(hbdata, hbdata.length,
                                    discoveryGroup, DISCOVERY_PORT);
    // create and start listen thread  on discovery socket
    // start heartbeating
    new Thread(this).start();
  }

  public void run() {
    while (!done) {
      try {
        if (announcer) {
          discoverySocket.send(hbDatagram);
        }
        Thread.sleep(HEARTBEAT_TIME);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}

