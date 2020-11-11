package net.flawe.signgui;

import net.flawe.signgui.events.SignUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class SignGUI extends JavaPlugin implements CommandExecutor, Listener {

    private static SignGUI instance;

    @Override
    public void onEnable() {
        setInstance(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginCommand("sign").setExecutor(this);
        if (Bukkit.getOnlinePlayers().size() == 0)
            return;
        PacketReader reader = new PacketReader();
        Bukkit.getOnlinePlayers().forEach(reader::inject);
    }

    @Override
    public void onDisable() {
        if (Bukkit.getOnlinePlayers().size() == 0)
            return;
        PacketReader reader = new PacketReader();
        Bukkit.getOnlinePlayers().forEach(reader::uninject);
    }

    public static SignGUI getInstance() {
        return instance;
    }

    private static void setInstance(SignGUI instance) {
        SignGUI.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            SignPacket.sendPacket(p);
            return true;
        }
        sender.sendMessage("[SignGUI] Only players can use this command!");
        return true;
    }

    @EventHandler
    void onJoin(PlayerJoinEvent e) {
        new PacketReader().inject(e.getPlayer());
    }

    @EventHandler
    void onQuit(PlayerQuitEvent e) {
        new PacketReader().uninject(e.getPlayer());
    }

    @EventHandler
    void onUpdate(SignUpdateEvent e) {
        System.out.println(e.getLine(0));
        //TODO
    }
}
