package pers.wmx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: wangmingxin03
 * @date: 2020-09-01
 */
public class BioMultiThreadServer {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(PORT);
            Socket socket = null;
            while (true){
                socket = serverSocket.accept();
                new Thread(new SocketHandler(socket)).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (serverSocket != null){
                try {
                    serverSocket.close();
                    serverSocket = null;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

}

class SocketHandler implements Runnable {
    private Socket socket;

    public SocketHandler(Socket socket) {
        this.socket=socket;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " handel ...");

        InputStream in = null;
        OutputStream out = null;

        try {
            in = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer)) != 0) {
                System.out.println(new String(buffer, 0, length));
                out = socket.getOutputStream();
                out.write("success".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (socket != null){
                try {
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}