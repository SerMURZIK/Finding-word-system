import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.List;

public class JsonAnswer {
    private final File file;

    public JsonAnswer(File file) {
        this.file = file;
    }

    public void writeJson(List<PageEntry> list) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            fileWriter.write(gson.toJson(list));
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println("saveJson err");
            throw new RuntimeException(e);
        }
    }

    public String loadJson() {
        String answer = null;
        try (InputStream inputStream = new FileInputStream(file)) {
            int current;
            StringBuilder sb = new StringBuilder();
            while ((current = inputStream.read()) != -1) {
                sb.append(Character.toChars(current));
            }
            answer = sb.toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return answer;
    }
}
