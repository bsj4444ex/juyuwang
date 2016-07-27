package group;

/**
 * Created by Administrator on 2016/5/31.
 */

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2016/5/29.
 */
public class Group extends Thread {
    private InetAddress group=null;
    private MulticastSocket socket=null;
    private DatagramPacket packet;
    private String Ip;
    byte[] data=new byte[8192];
    int port=6186;

    public Group(String Ip){
        this.Ip=Ip;
        try{
            group= InetAddress.getByName("239.255.9.0");
            socket=new MulticastSocket(port);
            socket.setTimeToLive(1);
            socket.setLoopbackMode(true);
            socket.joinGroup(group);
        }
        catch(Exception e){}
    }
    public void run(){
            while(true) {
                try {
                    String sendpack = new String("0" + "|" + Ip + "|" + " ");      //广播包
                    data = sendpack.getBytes();
                    packet = new DatagramPacket(data, data.length, group, port);
                    socket.send(packet);
                    sleep(5000);
                } catch (Exception e) {
                }
            }

    }
}
