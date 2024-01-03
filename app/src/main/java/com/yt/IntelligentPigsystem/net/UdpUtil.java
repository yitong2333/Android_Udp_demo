package com.yt.IntelligentPigsystem.net;

import android.app.Activity;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpUtil {
    private DatagramSocket socket;
    private DatagramPacket packet;

    // 构造函数
    public UdpUtil() throws SocketException {
        // 实例化socket
        socket = new DatagramSocket();
        // 设置地址重用（如果需要）
        socket.setReuseAddress(true);
    }

    // 发送方法
    public void sendMsg(final Activity activity, final String ip, final int port, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] buf = msg.getBytes();
                    InetAddress address = InetAddress.getByName(ip);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                    socket.send(packet);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "消息已发送", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //接收方法
    public String revMsg(int port){
        String result = "";
        try {
            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            //绑定ip和端口
            socket.bind(new InetSocketAddress(port));

            byte[] bytes = new byte[128];
            packet = new DatagramPacket(bytes,bytes.length);
            socket.receive(packet);

            result = new String(packet.getData(),packet.getOffset(),packet.getLength());

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    // 关闭socket
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
