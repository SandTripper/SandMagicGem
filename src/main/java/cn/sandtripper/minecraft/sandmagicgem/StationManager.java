package cn.sandtripper.minecraft.sandmagicgem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class StationManager {

    private final String GEM_SETTING_NAME = "§d§l宝石镶嵌台";
    private final String[] GEM_SETTING_LORES = {
            "§7右键放置",
    };

    ConfigReader stationConfigReader;

    ItemStack gemSettingItemStack;
    private HashSet<String> locationSet;

    StationManager(SandMagicGem plugin) {
        stationConfigReader = new ConfigReader(plugin, "station.yml");
        stationConfigReader.saveDefaultConfig();
        locationSet = new HashSet<>(stationConfigReader.getConfig().getStringList("locations"));

        gemSettingItemStack = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta itemMeta = gemSettingItemStack.getItemMeta();
        itemMeta.setDisplayName(GEM_SETTING_NAME);
        itemMeta.setLore(Arrays.asList(GEM_SETTING_LORES));
        gemSettingItemStack.setItemMeta(itemMeta);

    }

    public void disable() {
        saveData();
    }

    ItemStack getGemSettingItemStack() {
        return gemSettingItemStack.clone();
    }

    boolean isGemSettingBlock(Block block) {
        if (!block.getType().equals(Material.CRAFTING_TABLE)) {
            return false;
        }
        Location location = block.getLocation();
        String locationKey = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY()
                + "," + location.getBlockZ();
        return locationSet.contains(locationKey);
    }

    void handleGemSettingPlace(Block block) {
        Location location = block.getLocation();
        String locationKey = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY()
                + "," + location.getBlockZ();
        locationSet.add(locationKey);
        saveData();
    }

    void handleGemSettingBreak(Block block) {
        Location location = block.getLocation();
        String locationKey = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY()
                + "," + location.getBlockZ();
        locationSet.remove(locationKey);
        saveData();
    }

    boolean isGemSettingItemStack(ItemStack itemStack) {
        return itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(GEM_SETTING_NAME)
                && itemStack.getItemMeta().getLore().equals(Arrays.asList(GEM_SETTING_LORES));
    }

    private void saveData() {
        stationConfigReader.getConfig().set("locations", new ArrayList<>(locationSet));
        stationConfigReader.saveConfig();
    }
}
