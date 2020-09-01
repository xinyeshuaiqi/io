package pers.wmx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * BIO SERVER
 *
 * @author: wangmingxin03
 * @date: 2020-09-01
 */
public class BioServer {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket client = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            serverSocket = new ServerSocket(PORT);
            client = serverSocket.accept();         //阻塞点1
            inputStream = client.getInputStream();

            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != 0) {   //阻塞点2
                System.out.println(new String(buffer, 0, length));
                outputStream = client.getOutputStream();

                //沉睡5s模拟处理业务逻辑
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));

                outputStream.write("success".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
