/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.subsystems;

import edu.wilsonhs.toby.network.Packet;
import edu.wilsonhs.toby.network.PacketListener;
import edu.wpi.first.wpilibj.command.Subsystem;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author Toby
 */
public class ServerSubsystem extends Subsystem {

    private Vector listeners = new Vector();
    ServerSocketConnection ssc;
    StreamConnection sc;
    InputStream is;
    OutputStream os;

    protected void initDefaultCommand() {
    }

    public void recieveData() {
        try {
            String msg = "";
            char c;
            while ((c = (char) is.read()) != -1) {
                if (c == '\n') {
                    parseDataAndSendToListeners(msg);
                    break;
                }
                msg += c;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void openServer() {
        try {
            ssc = (ServerSocketConnection) Connector.open("socket://:2914");
            System.out.println("Ready for connect");
            sc = null;
            is = null;
            os = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void instantiateServer() {
        try {
            sc = ssc.acceptAndOpen();
            is = sc.openInputStream();
            os = sc.openOutputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void resetConnetion() {
        try {
            sc.close();
            is.close();
            os.close();
            instantiateServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeServer() {
        try {
            sc.close();
            is.close();
            os.close();
            ssc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addListener(PacketListener packetListener) {
        listeners.addElement(packetListener);
    }

    public void sendPacket(Packet toSend) {
        sendData(toSend.getType() + toSend.getUnparsedBody() + '\n');
    }

    private void sendData(String msg) {
        try {
            for (int i = 0; i < msg.length(); i++) {
                os.write((int) msg.charAt(i));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void parseDataAndSendToListeners(String msg) {
        Packet recieved = new Packet(msg);
        for (int i = 0; i < listeners.size(); i++) {
            PacketListener listener = (PacketListener) listeners.elementAt(i);
            listener.onReceivePacket(recieved);
        }
    }
}
