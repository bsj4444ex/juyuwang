package com.test.bsj444.juyuwang;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Administrator on 2016/5/31.
 */
public class sendTest extends Thread {
    String txt;
    String IpAddress;
    String ipLocal;
    byte[] data=new byte[8192];
    private InetAddress group=null;
    private MulticastSocket socket=null;
    int port=6186;
    //private DatagramPacket packet;

    public sendTest(String txt,String IpAddress,String ipLocal){
        this.txt=txt;
        this.IpAddress=IpAddress;
        this.ipLocal=ipLocal;
        try{
            group= InetAddress.getByName("239.255.9.0");//注意
            socket=new MulticastSocket(port);
            socket.setTimeToLive(1);
            socket.setLoopbackMode(true);
            socket.joinGroup(group);
            Log.d("ddd","doing");
        }
        catch(Exception e){}
    }
    @Override
    public void run(){

        try{
            //Log.d("ddd1","doing");
            String ss="1"+"|"+IpAddress+"|"+txt+"|"; //封装聊天数据
            //Log.d("sss",ss);
            data =ss.getBytes();
            DatagramPacket packMail=new DatagramPacket(data,data.length,group,port);
            //Log.d("ipad",IpAddress);
            socket.send(packMail);
            Log.d("zzz",IpAddress);
            sleep(1000);


            //socket.close();
        }
        catch (Exception e){}
    }
}
