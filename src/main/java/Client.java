import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        writeToServer();
        System.out.println(readFromServer());
    }

    private static void writeToServer() {
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine();

        try (Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8989);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
            out.write(inputLine);
            out.flush();
        } catch (Exception e) {
            System.err.println("Client err");
            throw new RuntimeException(e);
        }
    }

    private static String readFromServer() {
        String serverData = null;
        try (Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8989);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            serverData = in.readLine();
        } catch (Exception e) {
            System.err.println("Client err");
            throw new RuntimeException(e);
        }
        return serverData;
    }
}
