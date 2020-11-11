package net.flawe.signgui;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.flawe.signgui.events.SignUpdateEvent;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayInUpdateSign;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketReader {
    Channel channel;
    public static Map<UUID, Channel> channels = new HashMap<>();

    public void inject(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        channel = entityPlayer.playerConnection.networkManager.channel;
        channels.put(player.getUniqueId(), channel);
        if (channel.pipeline().get("SimplePacketInjector") != null)
            return;
        channel.pipeline().addAfter("decoder", "SimplePacketInjector", new MessageToMessageDecoder<PacketPlayInUpdateSign>(){
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, PacketPlayInUpdateSign packet, List<Object> list) {
                list.add(packet);
                readPacket(player, packet);
            }
        });
    }

    public void uninject(Player player) {
        channel = channels.get(player.getUniqueId());
        if (channel.pipeline().get("SimplePacketInjector") != null)
            channel.pipeline().remove("SimplePacketInjector");
    }

    public void readPacket(Player player, PacketPlayInUpdateSign packet) {
        BlockPosition position = new BlockPosition(player.getLocation().getBlockX(), 1, player.getLocation().getBlockZ());
        if (packet.b().equals(position)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SignGUI.getInstance(), () -> Bukkit.getPluginManager().callEvent(new SignUpdateEvent(player, packet.c())), 0L);
        }
    }
}

