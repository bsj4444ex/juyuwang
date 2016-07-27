package com.test.bsj444.juyuwang;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;


import android.util.Log;

import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.StringTokenizer;

import group.Group;


public class MainActivity extends Activity {

//    String messages=null;
//    InetAddress group=null;
//    MulticastSocket socket=null;
//    int port=6186;

    boolean flag=true;//接收广播标志
    TextView test1;
    TextView test2;
    TextView test3;
    Button button1;
    Button button2;
    Button button3;
    Button flash;
    Button group;
    DatagramPacket packet;
    String IP;
    String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent =getIntent();
        UserName=intent.getStringExtra("User");
        TextView test=(TextView)MainActivity.this.findViewById(R.id.test);
        test.setText(UserName);

        test1 = (TextView) MainActivity.this.findViewById(R.id.test1);
        test2 = (TextView) MainActivity.this.findViewById(R.id.test2);
        test3= (TextView) MainActivity.this.findViewById(R.id.test3);
        button1=(Button)MainActivity.this.findViewById(R.id.button1);
        button2=(Button)MainActivity.this.findViewById(R.id.button2);
        button3=(Button)MainActivity.this.findViewById(R.id.button3);
        flash=(Button)MainActivity.this.findViewById(R.id.flash);
        group=(Button)MainActivity.this.findViewById(R.id.group);
        IP=getLocalIpAddress(MainActivity.this);    //获得本机地址


        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        flash.setOnClickListener(listener);
        group.setOnClickListener(listener);
        new GroupReceiver().start();
        Group g = new Group(UserName);
        g.start();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(flag==false)
            flag=true;
        new GroupReceiver().start();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        if(flag==true)
            flag=false;
    }

    //侦听事件
    View.OnClickListener listener =new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(MainActivity.this, TalkActivity.class);
            Intent intent2 = new Intent(MainActivity.this,GroupActivity.class);
            intent.putExtra("mine", UserName);
            intent2.putExtra("mine", UserName);
            String IpAddress;
            //startActivity(intent);
            switch(v.getId()){
                case R.id.button1:
                    IpAddress=test1.getText().toString();
                    if(IpAddress.equals(""))
                        Toast.makeText(MainActivity.this,"还没有人",Toast.LENGTH_LONG).show();
                    else {
                        intent.putExtra("ip",IpAddress);
                        startActivity(intent);
                    }

                    break;
                case R.id.button2:
                    IpAddress=test2.getText().toString();
                    if(IpAddress.equals("")){
                        Toast.makeText(MainActivity.this,"还没有人",Toast.LENGTH_LONG).show();
                    }
                    else {
                        intent.putExtra("ip",IpAddress);
                        startActivity(intent);
                    }
                    break;
                case R.id.button3:
                    IpAddress=test3.getText().toString();
                    if(IpAddress.equals(""))
                        Toast.makeText(MainActivity.this,"还没有人",Toast.LENGTH_LONG).show();
                    else {
                        intent.putExtra("ip",IpAddress);
                        startActivity(intent);
                    }
                    break;
                case R.id.flash:
                    Group g = new Group(UserName);
                    g.start();
                    break;
                case R.id.group:
                    startActivity(intent2);
                default:break;
            }
        }
    };

    //获取本地ip
    public static String getLocalIpAddress(Context context) {
        try {

            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" ;
        }
    }
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
    //接收进程
    public class GroupReceiver extends Thread {
        String messages=null;
        String message1=null;
        InetAddress group=null;
        MulticastSocket socket=null;
        int port=6186;

        @Override
        public void run(){
            byte[] data=new byte[8192];
            try{
                group=InetAddress.getByName("239.255.9.0");
                socket=new MulticastSocket(port);
                socket.joinGroup(group);
            }
            catch(Exception e){}
            while(flag) {
                try {
                    packet = new DatagramPacket(data, data.length);
                    socket.receive(packet);

                } catch (Exception e) {}


                //messages=null;
                String getmessages = new String(packet.getData(), 0, packet.getLength()); //获得user名

                StringTokenizer kanip = new StringTokenizer(getmessages, "|");
                message1 = kanip.nextToken();
                messages = kanip.nextToken();

                //Log.d("qq", messages);
                if (message1.equals("0")) {
                if (messages != null && test1.getText().equals("")) {
                    test1.post(new Runnable() {
                        @Override
                        public void run() {
                            test1.setText(messages);
                            //Log.d("ss", messages);

                        }
                    });
                } else if (messages != null && test2.getText().equals("") && !(messages.equals(test1.getText().toString()))) {
                    test2.post(new Runnable() {
                        @Override
                        public void run() {
                            test2.setText(messages);
                            //Log.d("ss", messages);

                        }
                    });
                } else if (messages != null && test3.getText().equals("") && !(messages.equals(test1.getText().toString())) && !(messages.equals(test2.getText().toString()))) {
                    test3.post(new Runnable() {
                        @Override
                        public void run() {
                            test3.setText(messages);
                            //Log.d("ss", messages);

                        }
                    });
                }
            }

                //socket.close();

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

        }
    }

}
