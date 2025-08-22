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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class RealmMeshReader {
    private final String destination;
    private JsonObject root;
    public RealmMeshReader(String destination) {
        this.destination = destination;
        CompletableFuture<JsonObject> rootFuture = read();
        rootFuture.thenAccept(jsonObject -> {
            if (jsonObject != null) {
                root = jsonObject;
            }
        });
    }

    private CompletableFuture<JsonObject> read() {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                TextUtil.debugText("Initializing Reader ");
                try {
                    String jsonData = readFile(destination);

                    TextUtil.debugText(jsonData);
                    JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
                    future.complete(jsonObject);
                } catch (IOException e) {
                    TextUtil.debugText(e.getMessage());
                    future.complete(new JsonObject());
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

    public LocationMesh getLocationsMesh(String meshLayerName, World world) throws IOException {
        JsonObject object = root.getAsJsonObject(meshLayerName);
        if (object == null) throw new IOException(meshLayerName + " not found in the mesh (Probably typed incorrectly)");
        JsonArray coordinateArray = object.getAsJsonArray("coordinates");
        if (coordinateArray == null ) throw new IOException(meshLayerName + " does not have a coordinates array");
        return new LocationMesh(world, coordinateArray);
    }
}
