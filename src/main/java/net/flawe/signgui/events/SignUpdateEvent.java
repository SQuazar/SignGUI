package net.flawe.signgui.events;

import net.flawe.signgui.SignPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SignUpdateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String[] strings;

    public SignUpdateEvent(Player player, String[] strings) {
        this.player = player;
        this.strings = strings;
        SignPacket.sendDefaultBlockPacket(player);
    }

    public Player getPlayer() {
        return player;
    }

    public String[] getStrings() {
        return strings;
    }

    public String getLine(int line) {
        return strings[line];
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
