package com.example.demo.ServerInteract.Client;


import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServerClient {//进行Channel连接，获取内网服务
    private int Port;
    private static boolean check = false;
    public static String res = "";
    public static ServerSocket serverSocket;
    public static Socket socket;

    MyServerClient(int accept) throws IOException, IOException {//初始化Channel
        this.Port = accept;
        serverSocket = new ServerSocket(Port);
        System.out.println(Port + "隧道等待连接");
        socket = serverSocket.accept();
        System.out.println(Port + "隧道连接成功");
    }

    private final String NotFoundPage = "<!DOCTYPE html>" + "<html lang=\"en\">" + "<head>" + "    <meta charset=\"UTF-8\">" + "    <meta http-equiv=\"Cache-Control\" content=\"max-age=7200\" />" + "    <meta http-equiv=\"Expires\" content=\"Mon, 20 Jul 2013 23:00:00 GMT\" />" + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" + "    <title>404 not found</title>" + "</head>" + "<body style=\"display: flex;min-width: 300px;\">" + "    <div style=\"margin-top: 0%;    width: 60%;    padding-left: 20%;\" >" + "        <img style=\"width: 100%;\" src=\"404-l.jpg\" alt=\"\" srcset=\"\"/>" + "        <div style=\"display: flex;\">" + "            <span style=\"line-height: 64px;font-size: xxx-large;width: 50%;text-align: right;\">" + "                404</span>" + "                <img  style=\"width: 50%;max-width: 64px;\" src=\"404-r.png\" alt=\"\" srcset=\"\"/> </div>" + "    </div>" + "</body>" + "</html>";

    //Channel核心:与内网服务通讯
    private static void GetData(String param, OutputStream os) throws IOException {
        System.out.println("param" + param);
        //发送参数
        new DataOutputStream(socket.getOutputStream()).writeUTF(param);
        //线程创建
        ReceiveData receiveData = new ReceiveData(os);
        receiveData.run();//进行线程run
        while (check) {// 等待堵塞
            try {
                Thread.sleep(10);
                if (receiveData.error) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //接受Channel回调的线程，与本地socket对接
    static class ReceiveData extends Thread {
        public boolean error = false;
        OutputStream os;

        ReceiveData(OutputStream os) {
            this.os = os;
        }

        @Override
        public void run() {
            try {
                Socket s = serverSocket.accept();
                check = true;
                res = "";
                int len = s.getInputStream().read();
                while (len != -1) {
                    os.write(len);
                    len = s.getInputStream().read();
                }
                s.close();
                check = false;
            } catch (IOException e) {
                error = true;
//                e.printStackTrace();
            }

        }
    }

    public void GetAndSend(String param, OutputStream os) throws IOException {//发送参数且返回到os中
        if (param.equals("/404-l.jpg") || param.equals("/404-r.png")) {
            FileInputStream fis = new FileInputStream(param.substring(1));
            int len = fis.read();
            while (len != -1) {
                os.write(len);
                len = fis.read();
            }
            fis.close();

        } else if (param.endsWith("favicon.ico")) {
            FileInputStream fis = new FileInputStream("favicon.ico");
            int len = fis.read();
            while (len != -1) {
                os.write(len);
                len = fis.read();
            }
            fis.close();
        } else {
            GetData(param, os);//这里为就可以，上面内容可以DIY设置
        }
    }


    //保持socket长连
    public void KeepAlice() {
        //创建Future非阻塞线程
        ExecutorService es = Executors.newCachedThreadPool();
        es.submit(() -> {
            while (true) {
                Thread.sleep(1000);
                try {
                    //发送空字符判断socket是否断开
                    new DataOutputStream(socket.getOutputStream()).writeUTF("");
                } catch (Exception e) {
                    System.err.println("内网服务异常断开,重连中");
                    socket = serverSocket.accept();//重连
                    System.err.println("内网服务恢复0");
                }

            }
        });
    }
}
