package me.depickcator.trablesAdditions.Persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class PlayerDataReader {
    private final String destination;
    public PlayerDataReader(UUID playerUUID) {
        this.destination = "./plugins/TrablesAdditions/PlayerData/" + playerUUID + ".json";
    }

    public CompletableFuture<JsonObject> read() {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                TextUtil.debugText("PlayerDataReader", "Initializing Reader ");
                try {
                    String jsonData = readFile(destination);
                    TextUtil.debugText(jsonData);
                    JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
                    future.complete(jsonObject);
                } catch (IOException e) {
                    future.completeExceptionally(new IOException("Error reading file: " + destination));
                }
            }
        }.runTaskAsynchronously(TrablesAdditions.getInstance());
        return future;
    }

    private String readFile(String saved) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(saved), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }
}
