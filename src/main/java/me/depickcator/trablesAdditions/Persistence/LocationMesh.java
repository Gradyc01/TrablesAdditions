package me.depickcator.trablesAdditions.Persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocationMesh {
    private final World world;
    private final List<Pair<Location, Integer>> locations;
    private int totalWeight;
    public LocationMesh(World world, JsonArray locationMesh) {
        this.world = world;
        this.locations = parseLocationMesh(locationMesh);
    }

    private List<Pair<Location, Integer>> parseLocationMesh(JsonArray locationMesh) {
        List<Pair<Location, Integer>> list = new ArrayList<>();
        if (locationMesh == null) return list;
        for (JsonElement element : locationMesh) {
            if (element.isJsonArray()) {
                JsonArray arr = element.getAsJsonArray();
                double x = arr.get(0).getAsDouble();
                double y = arr.get(1).getAsDouble();
                double z = arr.get(2).getAsDouble();
                int weight = arr.get(3).getAsInt();
                list.add(Pair.of(new Location(world, x, y, z), weight));
                totalWeight += weight;
            };
        }
        return list;
    }

    /* Gets a number of locations specified in the count and whether they will be unique
     * Returns a Collection of Locations*/
    public List<Location> getRandomLocationsFromMesh(Random r, int count, boolean uniqueItems) {
//        int totalWeight = this.totalWeight;
//        List<Pair<Location, Integer>> locations = new ArrayList<>(this.locations);
        List<Location> ans = new ArrayList<>();
//        if (uniqueItems && count > locations.size()) {
//            throw new IllegalArgumentException("Not enough unique locations to satisfy the count.");
//        }
//        for (int i = 0; i < count; i++) {
//            int weight = r.nextInt(totalWeight);
//            TextUtil.debugText("Random Weight number is " + weight);
//            Pair<Location, Integer> pair = getWeightedLocation(locations, weight);
//            if (pair == null) throw new IllegalArgumentException("Illegal argument too much weight");
//
//            Location location = pair.getLeft();
//            ans.add(location);
//
//            if (uniqueItems) {
//                totalWeight -= pair.getRight(); // Update totalWeight
//                locations.remove(pair); // Remove selected item to prevent reuse
//            }
//        }
        for (Pair<Location, Integer> pair : getRandomLocationsWeightedFromMesh(r, count, uniqueItems)) {
            ans.add(pair.getLeft());
        }
        return ans;
    }

    public List<Pair<Location, Integer>> getRandomLocationsWeightedFromMesh(Random r, int count, boolean uniqueItems) {
        int totalWeight = this.totalWeight;
        List<Pair<Location, Integer>> locations = new ArrayList<>(this.locations);
        List<Pair<Location, Integer>> ans = new ArrayList<>();
        if (uniqueItems && count > locations.size()) {
            throw new IllegalArgumentException("Not enough unique locations to satisfy the count.");
        }
        for (int i = 0; i < count; i++) {
            int weight = r.nextInt(totalWeight);
            TextUtil.debugText("Random Weight number is " + weight);
            Pair<Location, Integer> pair = getWeightedLocation(locations, weight);
            if (pair == null) throw new IllegalArgumentException("Illegal argument too much weight");

            Location location = pair.getLeft();
            ans.add(Pair.of(location, pair.getRight()));

            if (uniqueItems) {
                totalWeight -= pair.getRight(); // Update totalWeight
                locations.remove(pair); // Remove selected item to prevent reuse
            }
        }
        return ans;
    }

    public List<Location> getAllLocations() {
        List<Location> list = new ArrayList<>();
        for (Pair<Location, Integer> pair : locations) {
            list.add(pair.getLeft());
        }
        return list;
    }

    public List<Pair<Location, Integer>> getAllLocationsWeighted() {
        return new ArrayList<>(locations);
    }

    /*Get the location that falls into the weight specified
     * Returns the location or null if it doesn't find one*/
    private Pair<Location, Integer> getWeightedLocation(List<Pair<Location, Integer>> locations, int weight) {
        int indexWeight = 0;
        String debugString = "Obtaining Process: ";
        for (Pair<Location, Integer> pair : locations) {
            indexWeight += pair.getRight();
            debugString += indexWeight + " --> ";
            if (indexWeight > weight) {
                Location l = pair.getLeft();
                TextUtil.debugText(debugString + "Location is " + l.getX() + ", " + l.getY() + ", " + l.getZ());
                return pair;
            }
        }
        return null;
    }
}
