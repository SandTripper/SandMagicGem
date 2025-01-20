package cn.sandtripper.minecraft.sandmagicgem;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import static cn.sandtripper.minecraft.sandmagicgem.GuiListener.InvType.*;


public class GuiListener implements Listener {
    private final SandMagicGem plugin;

    public GuiListener(SandMagicGem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        HumanEntity entity = event.getWhoClicked();
        if (!(entity instanceof Player)) {
            return;
        }
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) {
            return;
        }

        InvType invType = getInvType(inventory);
        switch (invType) {
            case GEMSTONE_SETTING:
                plugin.guiManager.clickGemstoneSetting(event);
                break;
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity entity = event.getWhoClicked();
        if (!(entity instanceof Player)) {
            return;
        }
        Inventory inventory = event.getInventory();

        InvType invType = getInvType(inventory);
        switch (invType) {
            case GEMSTONE_SETTING:
                plugin.guiManager.dragGemstoneSetting(event);
                break;
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity entity = event.getPlayer();
        if (!(entity instanceof Player)) {
            return;
        }
        Inventory inventory = event.getInventory();
        InvType invType = getInvType(inventory);
        if (invType != UNKNOWN) {
            Player player = (Player) entity;
            switch (invType) {
                case GEMSTONE_SETTING:
                    plugin.guiManager.handleGemstoneSettingInventoryClosed(player, inventory);
                    break;
            }
        }
    }


    InvType getInvType(Inventory inventory) {
        if (inventory.getHolder() instanceof Player) {
            return BACKPACK;
        } else if (inventory.getHolder() instanceof GuiManager.GemstoneSettingInventoryHolder) {
            return GEMSTONE_SETTING;
        }
        return UNKNOWN;
    }

    enum InvType {
        UNKNOWN,
        BACKPACK,
        GEMSTONE_SETTING,
    }
}