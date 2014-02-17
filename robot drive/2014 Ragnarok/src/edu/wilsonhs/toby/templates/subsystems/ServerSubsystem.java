/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wilsonhs.toby.templates.subsystems;

import edu.wilsonhs.toby.network.Packet;
import edu.wilsonhs.toby.network.NetworkListener;
import edu.wilsonhs.toby.templates.commands.NetworkCommands;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables2.util.List;
import java.io.DataOutputStream;
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

    private List listeners = new List();
    ServerSocketConnection ssc;
    StreamConnection sc;
    InputStream is;
    DataOutputStream os;

    protected void initDefaultCommand() {
//        Command netCommand = new NetworkCommands();
//        netCommand.setRunWhenDisabled(true);
//        setDefaultCommand(netCommand);
    }

    public void recieveData() throws IOException {
        System.out.println("recieving");
            String msg = "";
            char c;
            while ((c = (char) is.read()) != -1) {
                if (c == '\n') {
                    parseDataAndSendToListeners(msg);
                    break;
                }
                msg += c;
            }
        System.out.println("finished recieve");
    }

    public void openServer() {
        try {
            ssc = (ServerSocketConnection) Connector.open("socket://:2914");
            System.out.println("Ready for connect");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void instantiateServer() {
        try {
            sc = ssc.acceptAndOpen();
            System.out.println("Connected to client");
            System.out.println(listeners.size() + " listeners");
            is = sc.openInputStream();
            os = sc.openDataOutputStream();
            for (int i = 0; i < listeners.size(); i++) {
                NetworkListener listener = (NetworkListener) listeners.get(i);
                System.out.println("notifying " + listener.getClass().getName() + " of client connect");
                listener.onConnectToClient();
            }
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
            System.out.println("Connection reset");
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
            System.out.println("Server Closed");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isConnectedToClient() {
        return sc != null;
    }

    public void addListener(NetworkListener packetListener) {
        System.out.println("adding " + packetListener.getClass().getName() + " to listeners list");
        listeners.add(packetListener);
    }
    
    public void removeListener(NetworkListener packetListener){
        if(listeners.contains(packetListener)){
            listeners.remove(packetListener);
        }
    }

    public void sendPacket(Packet toSend) {
        System.out.print(toSend.getType() + toSend.getUnparsedBody() + '\n');
        sendData(toSend.getType() + toSend.getUnparsedBody() + '\n');
    }

    private void sendData(String msg) {
        try {
            for (int i = 0; i < msg.length(); i++) {
                os.write((int) msg.charAt(i));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            resetConnetion();
        }
    }

    public void startServerLoop() {
        new Thread() {
            public void run() {
                while (true) {
                    try{
                    recieveData();
                    Thread.sleep(20);
                    }catch(IOException ex){
                        ex.printStackTrace();
                        resetConnetion();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        for (int i = 0; i < listeners.size(); i++) {
                            NetworkListener listener = (NetworkListener) listeners.get(i);
                            listener.update();
                        }
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }.start();
    }

    private void parseDataAndSendToListeners(String msg) {
        System.out.println(msg);
        Packet recieved = new Packet(msg);
        for (int i = 0; i < listeners.size(); i++) {
            NetworkListener listener = (NetworkListener) listeners.get(i);
            listener.onReceivePacket(recieved);
        }
    }
}
