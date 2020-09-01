package pers.wmx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * BIO CLIENT
 *
 * @author: wangmingxin03
 * @date: 2020-09-01
 */
public class BioClient {
    private static final String ADDRESS = "localhost";

    private static final int PORT = 8888;

    public static void main(String[] args) {
        Socket client = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        byte[] buffer = new byte[1024];
        Scanner scanner = new Scanner(System.in);
        try {
            client = new Socket(ADDRESS, PORT);
            inputStream = client.getInputStream();
            outputStream = client.getOutputStream();

            while (true) {
                String string = scanner.nextLine();
                if("end".equals(string)){
                    System.out.println("Client End");
                    break;
                }
                outputStream.write(string.getBytes());
                int length = 0;
                if ((length = inputStream.read(buffer)) != 0) {
                    System.out.println(new String(buffer, 0, length));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
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

            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
