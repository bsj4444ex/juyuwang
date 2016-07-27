package com.test.bsj444.juyuwang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2016/5/30.
 */
public class GroupActivity extends Activity {


    EditText toSend;
    TextView text_view;
    Button send;
    Boolean flag1=true;
    DatagramPacket pack;
    String GroupIP="255.255.255.255";
    String myName;
    private ListView msgListView;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grouptalk);
        send=(Button)GroupActivity.this.findViewById(R.id.send_group);
        toSend=(EditText) GroupActivity.this.findViewById(R.id.edit_text_group);
        //text_view=(TextView)GroupActivity.this.findViewById(R.id.text_view_group);
        //text_view.setMovementMethod(new ScrollingMovementMethod());

        adapter = new MsgAdapter(GroupActivity.this, R.layout.itemip, msgList);
        msgListView = (ListView) findViewById(R.id.list_view_ground);
        msgListView.setAdapter(adapter);

        Intent intent =getIntent();
        myName=intent.getStringExtra("mine");
        new txtReceiver().start();
        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String txt=toSend.getText().toString();
                        //sendTest(txt,ipAddress);
                        flag1=false;
                        if(txt.equals("")){
                            Toast.makeText(GroupActivity.this,"Nothing",Toast.LENGTH_LONG).show();
                        }
                        else {
                            sendTest ss = new sendTest(txt, GroupIP, myName);
                            ss.start();
                            //text_view.append(myName + ":" + "\n" + txt + "\n");
                            //Log.d("ssss",txt);
                            Msg msg = new Msg(txt, Msg.TYPE_SENT);
                            msgList.add(msg);
                            adapter.notifyDataSetChanged();
                            msgListView.setSelection(msgList.size());
                            toSend.setText("");
                            flag1 = true;
                        }
                    }
                }
        );

    }

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

    //接收数据
    public class txtReceiver extends Thread {
        String str1=null;
        String str2=null;
        String str3=null;

        MulticastSocket socket=null;

        int port=6186;

        //String LocalIp=getLocalIpAddress(TalkActivity.this);

        @Override
        public void run(){
            byte[] data=new byte[8192];
            try{
                InetAddress group=InetAddress.getByName("239.255.9.0");
                socket=new  MulticastSocket(port);
                socket.joinGroup(group);

                //socket.joinGroup(group);
            }
            catch(Exception e){}
            while(true) {

                try {
                    pack = new DatagramPacket(data, data.length);
                    Log.d("RRd1", "doing");
                    socket.receive(pack);
                    Log.d("RRd", "doing");

                }
                catch (Exception e) {}

                //Log.d("str1",str1);
                //Log.d("str2",str2);
                String getMessage=new String(pack.getData(),0,pack.getLength());

                //Log.d("getmessage",getMessage);
                StringTokenizer fenxi=new StringTokenizer(getMessage,"|");
                try{str1=fenxi.nextToken();  //判断位
                    str2= fenxi.nextToken();
                    str3= fenxi.nextToken();}catch (Exception e){}

                if(str1.equals("1")&&str2.equals(GroupIP)){
                    msgListView.post(new Runnable() {
                        @Override
                        public void run() {
                            //text_view.append(str3);
                            //Log.d("ss", str3);
                            Msg msg = new Msg(str3, Msg.TYPE_RECEIVED);
                            msgList.add(msg);
                            adapter.notifyDataSetChanged();
                            msgListView.setSelection(msgList.size());
                        }
                    });}
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
