package me.depickcator.Test.Commands;

import me.depickcator.trablesAdditions.Commands.TrablesCommands;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CreateWorld extends TrablesCommands {

    public CreateWorld() {
        super(List.of("create-world"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 3) {
            UUID randomUUID = UUID.randomUUID();
            String worldName = args[0];
            String env = args[1];
            String type = args[2];


            WorldCreator worldCreator = new WorldCreator("./worlds/" + worldName + "_" + randomUUID);
            worldCreator.environment(findEnvironment(env));
            worldCreator.type(findWorldType(type));
            worldCreator.createWorld();
        } else if (args.length == 1) {
            WorldCreator worldCreator = new WorldCreator("./worlds/" + args[0]);
            worldCreator.createWorld();
        }

        return true;
    }

    private World.Environment findEnvironment(String envName) {
        return switch (envName.toLowerCase()) {
            case "end" -> World.Environment.THE_END;
            case "nether" -> World.Environment.NETHER;
            default -> World.Environment.NORMAL;
        };
    }

    private WorldType findWorldType(String typeName) {
        return switch (typeName.toLowerCase()) {
            case "amplified" -> WorldType.AMPLIFIED;
            case "large" -> WorldType.LARGE_BIOMES;
            case "flat" -> WorldType.FLAT;
            default -> WorldType.NORMAL;
        };
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
