package me.depickcator.trablesAdditions.Commands;

import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.List;


public abstract class TrablesCommands implements CommandExecutor, TabCompleter {
    public TrablesCommands(List<String> commandStrings) {
        if (commandStrings.isEmpty()) throw new IllegalArgumentException("The command argument cannot be empty!");
        for (String s : commandStrings) {
            TrablesAdditions.getInstance().getCommand(s).setExecutor(this);
        }
    }
}

