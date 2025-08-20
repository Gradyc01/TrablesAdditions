package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public abstract class TrablesListeners implements Listener {
    public TrablesListeners() {
        Server server = Bukkit.getServer();
        PluginManager pm = server.getPluginManager();
        pm.registerEvents(this, TrablesAdditions.getInstance());
    }
}
