package cn.sandtripper.minecraft.sandmagicgem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackpackManager {
    private final SandMagicGem plugin;

    public BackpackManager(SandMagicGem plugin) {
        this.plugin = plugin;
    }

    // 该函数用于向玩家背包放入ItemStack
    public void giveItemsToPlayer(Player player, List<ItemStack> itemList) {
        if (itemList == null || player == null) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                //添加物品到背包
                HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(itemList.toArray(new ItemStack[0]));
                // 检查未能被添加的物品
                if (!remainingItems.isEmpty()) {
                    // 处理未能被添加的物品
                    for (Map.Entry<Integer, ItemStack> entry : remainingItems.entrySet()) {
                        ItemStack remainingItem = entry.getValue();
                        player.getWorld().dropItem(player.getLocation(), remainingItem);
                    }
                    // 告诉玩家背包已满
                    player.sendMessage(plugin.messageManager.backpackFullThingsFallOnGround);
                }
            }
        }, 1L);
    }
}
