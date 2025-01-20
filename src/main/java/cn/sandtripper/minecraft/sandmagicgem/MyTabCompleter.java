package cn.sandtripper.minecraft.sandmagicgem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyTabCompleter implements TabCompleter {
    private final SandMagicGem plugin;

    public MyTabCompleter(SandMagicGem plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        // 在这里添加逻辑来决定哪些建议应该返回
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions = Arrays.asList("reload", "help", "give", "giveallgems", "open", "station");
        } else if (args[0].equals("give")) {
            if (args.length == 2) {
                List<String> onlinePlayers = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    onlinePlayers.add(player.getName());
                }
                suggestions = onlinePlayers;
            } else if (args.length == 3) {
                suggestions = plugin.gemManager.getGemDisplays();
            } else if (args.length == 4) {
                suggestions = Arrays.asList("0", "1", "2");
            }
        } else if (args[0].equals("giveallgems")) {
            if (args.length == 2) {
                List<String> onlinePlayers = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    onlinePlayers.add(player.getName());
                }
                suggestions = onlinePlayers;
            } else if (args.length == 3) {
                suggestions = Arrays.asList("0", "1", "2");
            }
        } else if (args[0].equals("open")) {
            if (args.length == 2) {
                suggestions = Arrays.asList("gemsetting");
            } else if (args.length == 3) {
                List<String> onlinePlayers = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    onlinePlayers.add(player.getName());
                }
                suggestions = onlinePlayers;
            }
        } else if (args[0].equals("station")) {
            if (args.length == 2) {
                suggestions = Arrays.asList("gemsetting");
            }
        }
        return suggestions;
    }
}