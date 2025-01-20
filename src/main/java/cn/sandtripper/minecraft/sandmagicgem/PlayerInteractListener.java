package cn.sandtripper.minecraft.sandmagicgem;

import cn.sandtripper.minecraft.sandmagicgem.GemManager.GemManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class PlayerInteractListener implements Listener {
    private final SandMagicGem plugin;

    public PlayerInteractListener(SandMagicGem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        ItemStack item = event.getItemInHand();

        // 检查放置的方块是否是宝石镶嵌台
        if (block.getType() == Material.CRAFTING_TABLE && plugin.stationManager.isGemSettingItemStack(item)) {
            plugin.stationManager.handleGemSettingPlace(block);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        // 检查放置的方块是否是宝石镶嵌台
        if (plugin.stationManager.isGemSettingBlock(block)) {
            plugin.stationManager.handleGemSettingBreak(block);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // 检查玩家右键点击时手持的物品
        if (event.getItem() != null && plugin.gemManager.getGemType(event.getItem()) != GemManager.GemType.UNKNOWN) {
            event.setCancelled(true);
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            Block block = event.getClickedBlock();
            if (plugin.stationManager.isGemSettingBlock(block)) {
                // 阻止默认的交互行为（打开工作台界面）
                event.setCancelled(true);
                // 获取玩家
                Player player = event.getPlayer();
                plugin.guiManager.openGemstoneSettingInventory(player);
            }
        }
    }
}