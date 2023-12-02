import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            while (true) {
                System.out.println("Ожидание подключения клиента");

                String word = readFromClient(serverSocket);

                System.out.println("Клиент успешно подключён");
                JsonAnswer jsonAnswer = new JsonAnswer(new File("src/main/resources/jsonAnswer.json"));

                BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
                jsonAnswer.writeJson(getPageEntries(engine, word));
                writeToClient(serverSocket, jsonAnswer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeToClient(ServerSocket serverSocket, JsonAnswer jsonAnswer) throws IOException {
        try (Socket socket = serverSocket.accept();
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            out.write(jsonAnswer.loadJson());
            out.flush();
        } catch (IOException e) {
            System.err.println("Main err");
            throw new RuntimeException(e);
        }
    }

    private static List<PageEntry> getPageEntries(BooleanSearchEngine engine, String word) {
        List<PageEntry> getterList;
        if (engine.search(word) == null) {
            getterList = new ArrayList<>();
        } else {
            getterList = engine.search(word);
            Collections.sort(getterList);
        }
        return getterList;
    }

    private static String readFromClient(ServerSocket serverSocket) throws IOException {
        String serverData;
        try (Socket socket = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            serverData = in.readLine();
        }
        return serverData;
    }
}