package cn.sandtripper.minecraft.sandmagicgem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuiManager {
    public static final int GEMSTONE_SETTING_INV_INPUT_SLOT = 13;
    public static final Material[] GEMSTONE_SETTING_INV_MATERIALS = {
            Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE, Material.AIR, Material.GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
    };

    public static final String[] GEMSTONE_SETTING_INV_NAMES = {
            "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌",
            "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌",
            "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌",
            "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌",
            "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", null, "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌",
            "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌",
            "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌",
            "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌",
            "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌", "§e将装备放入中间，再将宝石移动到装备上左键，即可完成镶嵌",
    };

    public static final String GemstoneSettingInvTitle = "§d§l宝石镶嵌台";
    private SandMagicGem plugin;

    public GuiManager(SandMagicGem plugin) {
        this.plugin = plugin;
    }

    public void openGemstoneSettingInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(new GemstoneSettingInventoryHolder(),
                GEMSTONE_SETTING_INV_MATERIALS.length, GemstoneSettingInvTitle);
        for (int i = 0; i < GEMSTONE_SETTING_INV_MATERIALS.length; i++) {
            inventory.setItem(i, getGemstoneSettingInventoryItem(i));
        }
        player.openInventory(inventory);
    }

    public void handleGemstoneSettingInventoryClosed(Player player, Inventory inventory) {
        // 将输入槽的物品放回玩家背包
        ItemStack item = inventory.getItem(GEMSTONE_SETTING_INV_INPUT_SLOT);
        if (item != null && item.getType() != Material.AIR) {
            inventory.setItem(GEMSTONE_SETTING_INV_INPUT_SLOT, null); // 清空容器中的物品
            plugin.backpackManager.giveItemsToPlayer(player, Arrays.asList(item));
        }
    }

    public void clickGemstoneSetting(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        int slot = event.getSlot();
        if (slot != GEMSTONE_SETTING_INV_INPUT_SLOT) {
            event.setCancelled(true);
            return;
        }
        if (!event.isLeftClick() || event.isShiftClick() || event.getAction().name().equals("COLLECT_TO_CURSOR")) {
            return;
        }

        ItemStack clickedItem = inventory.getItem(slot);
        ItemStack cursorItem = event.getCursor();
        if (clickedItem == null || clickedItem.getType() == Material.AIR || cursorItem == null
                || cursorItem.getType() == Material.AIR) {
            return;
        }
        if (clickedItem.getAmount() != 1 || cursorItem.getAmount() != 1) {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage(plugin.messageManager.amountError);
            event.setCancelled(true);
            return;
        }
        ItemStack newItem = plugin.gemManager.GemstoneSet(clickedItem, cursorItem);
        if (newItem == null) {
            return;
        }
        event.setCancelled(true);
        event.setCursor(null);
        inventory.setItem(slot, newItem);
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
    }

    public void dragGemstoneSetting(InventoryDragEvent event) {
        int slot = event.getRawSlots().stream().findFirst().orElse(-1);
        // 背包拖拽，不管
        if (slot < 0 || slot >= GEMSTONE_SETTING_INV_MATERIALS.length) {
            return;
        }
        if (event.getInventorySlots().size() != 1 || slot != GEMSTONE_SETTING_INV_INPUT_SLOT) {
            event.setCancelled(true);
            return;
        }
        Inventory inventory = event.getInventory();
        ItemStack cursorItem = event.getOldCursor();
        ItemStack clickedItem = inventory.getItem(slot);

        if (clickedItem == null || clickedItem.getType() == Material.AIR || cursorItem == null
                || cursorItem.getType() == Material.AIR) {
            return;
        }
        ItemStack newItem = plugin.gemManager.GemstoneSet(clickedItem, cursorItem);
        if (newItem == null) {
            return;
        }
        event.setCancelled(true);
        event.setCursor(null);
        inventory.setItem(slot, newItem);
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
    }

    private ItemStack getGemstoneSettingInventoryItem(int slot) {
        ItemStack itemStack = new ItemStack(GEMSTONE_SETTING_INV_MATERIALS[slot]);
        if (!GEMSTONE_SETTING_INV_MATERIALS[slot].equals(Material.AIR)) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(GEMSTONE_SETTING_INV_NAMES[slot]);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static class GemstoneSettingInventoryHolder implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
