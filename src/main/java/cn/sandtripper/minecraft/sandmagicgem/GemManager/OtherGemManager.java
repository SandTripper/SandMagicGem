package cn.sandtripper.minecraft.sandmagicgem.GemManager;

import cn.sandtripper.minecraft.sandmagicgem.SandMagicGem;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

import static cn.sandtripper.minecraft.sandmagicgem.GemManager.OtherGemManager.OtherGemType.CLEAR_REPAIR_COST;
import static cn.sandtripper.minecraft.sandmagicgem.GemManager.OtherGemManager.OtherGemType.UNBREAKABLE;

public class OtherGemManager {
    private final String GEM_NAME_TEMP = "§7§l§k||{COLOR}§l{DISPLAY}宝石§7§l§k||";
    private final String[] GEM_UNBREAKABLE_LORES_TEMP = {
            "§7在宝石镶嵌台内将宝石放到装备上",
            "§7即可将装备设置成无法破坏",
    };

    private final String GEM_CLEAR_REPAIR_COST_NAME_TEMP = "§7§l§k||{COLOR}§l{DISPLAY}宝石§7§l§k||";
    private final String[] GEM_CLEAR_REPAIR_COST_LORES_TEMP = {
            "§7在宝石镶嵌台内将宝石放到装备上",
            "§7即可将清除装备铁砧惩罚",
    };
    private SandMagicGem plugin;
    private HashMap<OtherGemType, OtherGemConfig> otherGemConfigs;
    private HashMap<OtherGemType, OtherGemData> otherGemDatas;

    private HashMap<OtherGemType, ItemStack> otherGemItemStacks;
    private HashMap<String, OtherGemType> otherGemDisplay2id;
    private FileConfiguration config;

    public OtherGemManager(SandMagicGem plugin) {
        this.plugin = plugin;
        initData();
    }


    public void reload() {
        initData();
    }

    private void initData() {
        this.config = plugin.getConfig();
        this.otherGemConfigs = new HashMap<>();
        this.otherGemDatas = new HashMap<>();
        this.otherGemDisplay2id = new HashMap<>();
        this.otherGemItemStacks = new HashMap<>();
        if (config.getConfigurationSection("other-configs.unbreakable") != null) {
            OtherGemConfig otherGemconfig = new OtherGemConfig();
            otherGemconfig.color = colorFormat(config.getString("other-configs.unbreakable.color"));
            otherGemconfig.display = config.getString("other-configs.unbreakable.display");
            otherGemconfig.headUrl = config.getString("other-configs.unbreakable.head-url");
            otherGemConfigs.put(UNBREAKABLE, otherGemconfig);
            OtherGemData otherGemData = new OtherGemData();
            otherGemData.type = UNBREAKABLE;
            otherGemData.name = GEM_NAME_TEMP.replace("{COLOR}", otherGemconfig.color).replace("{DISPLAY}", otherGemconfig.display);
            otherGemData.lores = Arrays.asList(GEM_UNBREAKABLE_LORES_TEMP);
            otherGemData.headUrl = otherGemconfig.headUrl;
            otherGemDatas.put(UNBREAKABLE, otherGemData);
            otherGemDisplay2id.put(otherGemconfig.display, UNBREAKABLE);
            otherGemItemStacks.put(UNBREAKABLE, makeItemStack(otherGemData));
        }
        if (config.getConfigurationSection("other-configs.clear_repair_cost") != null) {
            OtherGemConfig otherGemconfig = new OtherGemConfig();
            otherGemconfig.color = colorFormat(config.getString("other-configs.clear_repair_cost.color"));
            otherGemconfig.display = config.getString("other-configs.clear_repair_cost.display");
            otherGemconfig.headUrl = config.getString("other-configs.clear_repair_cost.head-url");
            otherGemConfigs.put(CLEAR_REPAIR_COST, otherGemconfig);
            OtherGemData otherGemData = new OtherGemData();
            otherGemData.type = CLEAR_REPAIR_COST;
            otherGemData.name = GEM_NAME_TEMP.replace("{COLOR}", otherGemconfig.color).replace("{DISPLAY}", otherGemconfig.display);
            otherGemData.lores = Arrays.asList(GEM_CLEAR_REPAIR_COST_LORES_TEMP);
            otherGemData.headUrl = otherGemconfig.headUrl;
            otherGemDatas.put(CLEAR_REPAIR_COST, otherGemData);
            otherGemDisplay2id.put(otherGemconfig.display, CLEAR_REPAIR_COST);
            otherGemItemStacks.put(CLEAR_REPAIR_COST, makeItemStack(otherGemData));
        }
    }

    public OtherGemData getOtherGemData(ItemStack item) {
        if (item == null) {
            return null;
        }
        if (item.getType() != Material.PLAYER_HEAD) {
            return null;
        }

        for (Map.Entry<OtherGemType, OtherGemData> otherGemDataEntry : otherGemDatas.entrySet()) {
            OtherGemData otherGemData = otherGemDataEntry.getValue();
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(otherGemData.name) && item.getItemMeta() != null && item.getItemMeta().getLore().equals(otherGemData.lores)) {
                return otherGemData;
            }
        }
        return null;
    }

    public ItemStack GemstoneSetOther(ItemStack input1, ItemStack input2) {
        OtherGemData otherGemData = getOtherGemData(input2);
        if (otherGemData == null) {
            return null;
        }
        ItemStack newItemStack = input1.clone();
        ItemMeta meta = newItemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        switch (otherGemData.type) {
            case UNBREAKABLE:
                meta.setUnbreakable(true);
                break;
            case CLEAR_REPAIR_COST:
                if (meta instanceof Repairable) {
                    ((Repairable) meta).setRepairCost(0);
                }
                break;
        }
        newItemStack.setItemMeta(meta);
        return newItemStack;
    }

    public ItemStack getOtherGemItemStackByDisplay(String display) {
        OtherGemType type = otherGemDisplay2id.get(display);
        if (type == null) {
            return null;
        }
        ItemStack itemStack = otherGemItemStacks.get(type).clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public List<ItemStack> getAllGemItemStacks() {
        List<ItemStack> ans = new LinkedList<>();
        for (Map.Entry<OtherGemType, ItemStack> entry : otherGemItemStacks.entrySet()) {
            ans.add(entry.getValue().clone());
        }
        return ans;
    }

    public List<String> getOtherGemDisplays() {
        return new ArrayList<>(otherGemDisplay2id.keySet());
    }

    private String colorFormat(String content) {
        if (content == null) {
            return "";
        }
        return content.replace("&", "§");
    }

    private ItemStack makeItemStack(OtherGemData otherGemData) {
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);

        // 获取SkullMeta
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        // 创建GameProfile对象
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", otherGemData.headUrl));

        // 利用反射设置SkullMeta的GameProfile属性
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skullMeta.setDisplayName(otherGemData.name);
        skullMeta.setLore(otherGemData.lores);
        // 应用SkullMeta到ItemStack
        skullItem.setItemMeta(skullMeta);

        return skullItem;
    }

    public enum OtherGemType {
        UNBREAKABLE,
        CLEAR_REPAIR_COST,
    }

    private static class OtherGemConfig {
        public String display;

        public String color;

        public String headUrl;
    }

    public static class OtherGemData {
        public OtherGemType type;

        public String name;

        public List<String> lores;

        public String headUrl;
    }
}
