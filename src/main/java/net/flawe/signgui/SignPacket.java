package net.flawe.signgui;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SignPacket {

    private static PacketPlayOutOpenSignEditor openSignEditor(Player p) {
        return new PacketPlayOutOpenSignEditor(new BlockPosition(p.getLocation().getBlockX(), 1, p.getLocation().getBlockZ()));
    }

    private static PacketPlayOutBlockChange blockChange(Player p) {
        PacketPlayOutBlockChange change = new PacketPlayOutBlockChange(((CraftWorld) p.getWorld()).getHandle(), new BlockPosition(p.getLocation().getBlockX(), 1, p.getLocation().getBlockZ()));
        change.block = Blocks.OAK_SIGN.getBlockData();
        return change;
    }

    private static PacketPlayOutBlockChange defaultBlockChange(Player p) {
        PacketPlayOutBlockChange change = new PacketPlayOutBlockChange(((CraftWorld) p.getWorld()).getHandle(), new BlockPosition(p.getLocation().getBlockX(), 1, p.getLocation().getBlockZ()));
        change.block = ((CraftBlock) p.getWorld().getBlockAt(p.getLocation().getBlockX(), 1, p.getLocation().getBlockZ())).getNMS();
        return change;
    }

    public static void sendPacket(Player p) {
        EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
        entityPlayer.playerConnection.sendPacket(blockChange(p));
        entityPlayer.playerConnection.sendPacket(openSignEditor(p));
    }

    public static void sendDefaultBlockPacket(Player p) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(defaultBlockChange(p));
    }

}
