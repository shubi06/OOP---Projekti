package utils;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class FileUtil {

    public static void ensureFileExists(String filePath) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            System.out.println("Gabim gjatë krijimit të file: " + filePath);
            e.printStackTrace();
        }
    }

    public static synchronized void writeLine(String filePath, String line) {
        ensureFileExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Gabim gjatë shkrimit në file: " + filePath);
            e.printStackTrace();
        }
    }

    public static synchronized List<String> readAll(String filePath) {
        List<String> lines = new ArrayList<>();
        ensureFileExists(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Gabim gjatë leximit nga file: " + filePath);
            e.printStackTrace();
        }
        return lines;
    }
    
    public static synchronized void overwriteFile(String filePath, List<String> lines) {
        ensureFileExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Gabim gjatë overwrite në file: " + filePath);
            e.printStackTrace();
        }
    }
}
