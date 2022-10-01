package com.example.demo.ServerInteract.Server;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

//进行Channel连接，提供内网服务
public class LocalServer {
    Socket socket;
    //    static int SPort=8888;
    static int SPort = 7456;
    static int CPort = 5555;
    static boolean check = false;
    static String ip = "172.81.247.205";

    //    static String ip="127.0.0.1";
    LocalServer(int sport, int cport, String ip) throws Exception {
        SPort = sport;
        CPort = cport;
        this.ip = ip;
        init();
    }

    LocalServer() throws Exception {
        init();
    }

    //进行Channel连接
    public void init() throws Exception {
        while (true) {
            try {
                socket = new Socket(ip, SPort);
                break;
            } catch (ConnectException CE) {
                continue;
            }
        }
        System.out.println("隧道已连接");
        while (true) {
//            接受Channel参数请求
            String param = new DataInputStream(socket.getInputStream()).readUTF();
            if (param.trim().equals("")) {//保持连接
                continue;
            }
            System.out.println("param" + param);
            new SendData(GetOneStream(param)).run();//开启线程回应Channel并且阻塞
            while (check) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    InputStream GetOneStream(String param) throws Exception {//获取Stream流
        URL url = new URL("http://127.0.0.1:" + CPort + param);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(6 * 1000);
        return conn.getInputStream();
    }

    class SendData extends Thread {
        InputStream is;

        SendData(InputStream is) {
            this.is = is;
        }

        //与内网服务对接传输
        @Override
        public void run() {
            try {
                Socket s = new Socket(ip, SPort);
                check = true;
                int len = is.read();
                while (len != -1) {
                    s.getOutputStream().write(len);
                    len = is.read();
                }
                s.close();
                check = false;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("参数不足使用默认端口号和地址");
            System.out.println("SPort:" + SPort + ",CPort:" + CPort + ",ip:" + ip);
            while (true) {
                try {
                    new LocalServer();
                } catch (Exception e) {
                    System.out.println("隧道断开，重新连接");
                }
            }

        } else {
            while (true) {
                try {
                    new LocalServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2]);
                } catch (Exception e) {
                    System.out.println("隧道断开，重新连接");
                }
            }
        }


    }
}


