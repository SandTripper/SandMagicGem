package cn.sandtripper.minecraft.sandmagicgem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor {
    private final SandMagicGem plugin;

    public CommandHandler(SandMagicGem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(plugin.messageManager.instructionFormatError);
            return true;
        } else if (args[0].equals("reload")) {
            if (args.length == 1) {
                long startTime = System.currentTimeMillis();
                plugin.reload();
                long endTime = System.currentTimeMillis();
                commandSender.sendMessage(String.format(plugin.messageManager.reloadSuccess, endTime - startTime));
                return true;
            }
        } else if (args[0].equals("help")) {
            if (args.length == 1) {
                for (String message : plugin.messageManager.helps) {
                    commandSender.sendMessage(message);
                }
                return true;
            }
        } else if (args[0].equals("give")) {
            if (args.length == 4) {
                Player player = plugin.getServer().getPlayer(args[1]);
                if (player == null) {
                    commandSender.sendMessage(plugin.messageManager.playerNotExist);
                    return true;
                }
                if (!player.isOnline()) {
                    commandSender.sendMessage(plugin.messageManager.playerNotOnline);
                    return true;
                }
                int level = 0;
                try {
                    level = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(plugin.messageManager.instructionFormatError);
                    return true;
                }
                String display = args[2];
                plugin.backpackManager.giveItemsToPlayer(player, Arrays.asList(plugin.gemManager.getGemItemStackByDisplay(display, level)));
                commandSender.sendMessage(plugin.messageManager.itemGiveSuccess);
                return true;
            }
        } else if (args[0].equals("giveallgems")) {
            if (args.length == 3) {
                Player player = plugin.getServer().getPlayer(args[1]);
                if (player == null) {
                    commandSender.sendMessage(plugin.messageManager.playerNotExist);
                    return true;
                }
                if (!player.isOnline()) {
                    commandSender.sendMessage(plugin.messageManager.playerNotOnline);
                    return true;
                }
                int level = 0;
                try {
                    level = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(plugin.messageManager.instructionFormatError);
                    return true;
                }
                List<ItemStack> items = plugin.gemManager.getAllGemItemStacks(level);
                plugin.backpackManager.giveItemsToPlayer(player, items);
                commandSender.sendMessage(plugin.messageManager.itemGiveSuccess);
                return true;
            }
        } else if (args[0].equals("station")) {

            if (args.length == 2) {
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    switch (args[1]) {
                        case "gemsetting":
                            plugin.backpackManager.giveItemsToPlayer(player, Arrays.asList(plugin.stationManager.getGemSettingItemStack()));
                            return true;
                    }
                } else {
                    commandSender.sendMessage(plugin.messageManager.requirePlayerExecutionIdentity);
                    return true;
                }

            }
        } else if (args[0].equals("open")) {
            if (args.length == 2) {
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    switch (args[1]) {
                        case "gemsetting":
                            plugin.guiManager.openGemstoneSettingInventory(player);
                            return true;
                    }
                } else {
                    commandSender.sendMessage(plugin.messageManager.requirePlayerExecutionIdentity);
                    return true;
                }

            } else if (args.length == 3) {
                Player player = plugin.getServer().getPlayer(args[2]);
                if (player == null) {
                    commandSender.sendMessage(plugin.messageManager.playerNotExist);
                    return true;
                }
                if (!player.isOnline()) {
                    commandSender.sendMessage(plugin.messageManager.playerNotOnline);
                    return true;
                }
                switch (args[1]) {
                    case "gemsetting":
                        plugin.guiManager.openGemstoneSettingInventory(player);
                        commandSender.sendMessage(plugin.messageManager.successOpenPlayerGui);
                        return true;
                }
            }
        }
        commandSender.sendMessage(plugin.messageManager.instructionFormatError);
        return true;
    }


}
