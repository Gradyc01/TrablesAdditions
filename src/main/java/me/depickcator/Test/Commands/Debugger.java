package me.depickcator.Test.Commands;

import me.depickcator.trablesAdditions.Commands.TrablesCommands;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Debugger extends TrablesCommands {

    private static boolean debuggerState;
    public Debugger() {
        super(List.of("debugger"));
        debuggerState = false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        debuggerState = !debuggerState;
        TextUtil.broadcastMessage(TextUtil.makeText("Change the state of the debugger to "  + debuggerState + ".", TextUtil.DARK_GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }

    public static boolean getDebuggerState() {
        return debuggerState;
    }
}
