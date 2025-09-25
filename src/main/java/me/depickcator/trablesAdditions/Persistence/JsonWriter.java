package me.depickcator.trablesAdditions.Persistence;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JsonWriter {
    private PrintWriter writer;
    private final String destination;

    public JsonWriter(String destination) {
        this.destination = destination;
    }

    /*Opens a new file at the destination*/
    public void open() throws FileNotFoundException {
//        writer = new PrintWriter(new File(destination));
        File file = new File(destination);
        File parentDir = file.getParentFile();


        // If the parent directory doesn't exist, create it
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();  // Creates the necessary directories if they don't exist
        }

        writer = new PrintWriter(file);
    }

    /*Writes jsonObject into the file*/
    public void write(JsonObject json) {
        saveToFile(json.toString());
    }

    /*Closes the writer*/
    public void close() {
        writer.close();
    }

    /*writes the string to file*/
    private void saveToFile(String json) {
        writer.print(json);
    }
}
