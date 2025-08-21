package me.depickcator.trablesAdditions.Util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionBuilder;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorldEditUtil {
    public static void pasteSchematic(File schemFile, Location loc) {
        ClipboardFormat format = ClipboardFormats.findByFile(schemFile);
        if (format == null) {
            TextUtil.debugText("WorldEdit Util", "Unsupported schematic format for file " + schemFile.getName());
            return;
        }

        try (ClipboardReader reader = format.getReader(new FileInputStream(schemFile))) {
            Clipboard clipboard = reader.read();
//
            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                    .world(BukkitAdapter.adapt(loc.getWorld()))
                    .maxBlocks(-1)
                    .build();

            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
            editSession.flushSession();
        } catch (IOException | com.sk89q.worldedit.WorldEditException e) {
            TextUtil.debugText("WorldEdit Util", e.getMessage());
        }
    }
}
