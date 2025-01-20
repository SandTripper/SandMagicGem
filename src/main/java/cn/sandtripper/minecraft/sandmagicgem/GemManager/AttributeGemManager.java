package cn.sandtripper.minecraft.sandmagicgem.GemManager;

import cn.sandtripper.minecraft.sandmagicgem.SandMagicGem;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

import static cn.sandtripper.minecraft.sandmagicgem.GemManager.GemManager.GEM_LEVEL_DISPLAY;
import static cn.sandtripper.minecraft.sandmagicgem.GemManager.GemManager.GEM_LEVEL_NAME;

public class AttributeGemManager {
    private final String GEM_NAME_TEMP = "§7§l§k||{ENCHANT_COLOR}§l{LEVEL_DISPLAY}{ENCHANT_NAME}宝石§7§l§k||";
    private final String[] GEM_LORES_TEMP = {
            "§7品质: {LEVEL_NAME}",
            "§7在宝石镶嵌台内将宝石放到装备上",
            "§7即可将装备{ENCHANT_NAME}属性+1",
            "§7上限 {MAX_LEVEL}",
    };

    private HashMap<String, Attribute> id2Attribute;

    private SandMagicGem plugin;
    private HashMap<String, AttributeGemConfig> attributeGemConfigs;
    private HashMap<String, List<AttributeGemData>> attributeGemDatas;
    private HashMap<String, List<ItemStack>> attributeGemItemStacks;
    private HashMap<String, String> attributeGemDisplay2id;
    private FileConfiguration config;


    public AttributeGemManager(SandMagicGem plugin) {
        initConst();
        this.plugin = plugin;
        initData();
    }

    void initConst() {
        id2Attribute = new HashMap<>();
        id2Attribute.put("generic_max_health", Attribute.GENERIC_MAX_HEALTH);
        id2Attribute.put("generic_follow_range", Attribute.GENERIC_FOLLOW_RANGE);
        id2Attribute.put("generic_knockback_resistance", Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        id2Attribute.put("generic_movement_speed", Attribute.GENERIC_MOVEMENT_SPEED);
        id2Attribute.put("generic_flying_speed", Attribute.GENERIC_FLYING_SPEED);
        id2Attribute.put("generic_attack_damage", Attribute.GENERIC_ATTACK_DAMAGE);
        id2Attribute.put("generic_attack_knockback", Attribute.GENERIC_ATTACK_KNOCKBACK);
        id2Attribute.put("generic_attack_speed", Attribute.GENERIC_ATTACK_SPEED);
        id2Attribute.put("generic_armor", Attribute.GENERIC_ARMOR);
        id2Attribute.put("generic_armor_toughness", Attribute.GENERIC_ARMOR_TOUGHNESS);
        id2Attribute.put("generic_luck", Attribute.GENERIC_LUCK);
        id2Attribute.put("generic_horse_jump_strength", Attribute.HORSE_JUMP_STRENGTH);
        id2Attribute.put("zombie_spawn_reinforcements", Attribute.ZOMBIE_SPAWN_REINFORCEMENTS);
    }

    public void reload() {
        initData();
    }

    private void initData() {
        this.config = plugin.getConfig();
        this.attributeGemConfigs = new HashMap<>();
        this.attributeGemDatas = new HashMap<>();
        this.attributeGemDisplay2id = new HashMap<>();
        this.attributeGemItemStacks = new HashMap<>();
        ConfigurationSection attributeConfigsSection = config.getConfigurationSection("attribute-configs");
        if (attributeConfigsSection != null) {
            for (String gemId : attributeConfigsSection.getKeys(false)) {
                ConfigurationSection gemSection = attributeConfigsSection.getConfigurationSection(gemId);
                if (gemSection != null) {
                    AttributeGemConfig attributeGemConfig = new AttributeGemConfig();
                    attributeGemConfig.attribute = id2Attribute.get(gemId);
                    attributeGemConfig.maxNormalValue = gemSection.getInt("max-normal-value", 0);
                    attributeGemConfig.maxRareValue = gemSection.getInt("max-rare-value", 0);
                    attributeGemConfig.maxEpicValue = gemSection.getInt("max-epic-value", 0);
                    attributeGemConfig.step = gemSection.getInt("step", 1);
                    attributeGemConfig.display = gemSection.getString("display", "");
                    attributeGemConfig.color = colorFormat(gemSection.getString("color", "&f"));
                    attributeGemConfig.headUrl = gemSection.getString("head-url", "");
                    attributeGemConfigs.put(gemId, attributeGemConfig);

                    AttributeGemData attributeGemDataNormal = new AttributeGemData();
                    AttributeGemData attributeGemDataRare = new AttributeGemData();
                    AttributeGemData attributeGemDataEpic = new AttributeGemData();

                    attributeGemDataNormal.attribute = attributeGemDataRare.attribute = attributeGemDataEpic.attribute = attributeGemConfig.attribute;
                    attributeGemDataNormal.step = attributeGemDataRare.step = attributeGemDataEpic.step = attributeGemConfig.step;
                    attributeGemDataNormal.headUrl = attributeGemDataRare.headUrl = attributeGemDataEpic.headUrl = attributeGemConfig.headUrl;

                    attributeGemDataNormal.name = GEM_NAME_TEMP.replace("{LEVEL_DISPLAY}", GEM_LEVEL_DISPLAY[0]).replace("{ENCHANT_COLOR}", attributeGemConfig.color).replace("{ENCHANT_NAME}", attributeGemConfig.display);
                    attributeGemDataRare.name = GEM_NAME_TEMP.replace("{LEVEL_DISPLAY}", GEM_LEVEL_DISPLAY[1]).replace("{ENCHANT_COLOR}", attributeGemConfig.color).replace("{ENCHANT_NAME}", attributeGemConfig.display);
                    attributeGemDataEpic.name = GEM_NAME_TEMP.replace("{LEVEL_DISPLAY}", GEM_LEVEL_DISPLAY[2]).replace("{ENCHANT_COLOR}", attributeGemConfig.color).replace("{ENCHANT_NAME}", attributeGemConfig.display);

                    attributeGemDataNormal.lores = new ArrayList<>(Arrays.asList(GEM_LORES_TEMP));
                    attributeGemDataRare.lores = new ArrayList<>(Arrays.asList(GEM_LORES_TEMP));
                    attributeGemDataEpic.lores = new ArrayList<>(Arrays.asList(GEM_LORES_TEMP));

                    for (int i = 0; i < attributeGemDataNormal.lores.size(); i++) {
                        attributeGemDataNormal.lores.set(i, attributeGemDataNormal.lores.get(i).replace("{LEVEL_NAME}", GEM_LEVEL_NAME[0]).replace("{ENCHANT_NAME}", attributeGemConfig.display).replace("{MAX_LEVEL}", String.valueOf(attributeGemConfig.maxNormalValue)));
                    }
                    for (int i = 0; i < attributeGemDataRare.lores.size(); i++) {
                        attributeGemDataRare.lores.set(i, attributeGemDataRare.lores.get(i).replace("{LEVEL_NAME}", GEM_LEVEL_NAME[1]).replace("{ENCHANT_NAME}", attributeGemConfig.display).replace("{MAX_LEVEL}", String.valueOf(attributeGemConfig.maxRareValue)));
                    }
                    for (int i = 0; i < attributeGemDataEpic.lores.size(); i++) {
                        attributeGemDataEpic.lores.set(i, attributeGemDataEpic.lores.get(i).replace("{LEVEL_NAME}", GEM_LEVEL_NAME[2]).replace("{ENCHANT_NAME}", attributeGemConfig.display).replace("{MAX_LEVEL}", String.valueOf(attributeGemConfig.maxEpicValue)));
                    }
                    attributeGemDataNormal.maxValue = attributeGemConfig.maxNormalValue;
                    attributeGemDataRare.maxValue = attributeGemConfig.maxRareValue;
                    attributeGemDataEpic.maxValue = attributeGemConfig.maxEpicValue;

                    attributeGemDatas.put(gemId, Arrays.asList(attributeGemDataNormal, attributeGemDataRare, attributeGemDataEpic));
                    attributeGemDisplay2id.put(attributeGemConfig.display, gemId);
                    attributeGemItemStacks.put(gemId, Arrays.asList(makeItemStack(attributeGemDataNormal), makeItemStack(attributeGemDataRare), makeItemStack(attributeGemDataEpic)));
                }
            }
        }
    }

    public AttributeGemData getAttributeGemData(ItemStack item) {
        if (item == null) {
            return null;
        }
        if (item.getType() != Material.PLAYER_HEAD) {
            return null;
        }

        for (Map.Entry<String, List<AttributeGemData>> gemDataList : attributeGemDatas.entrySet()) {
            for (AttributeGemData attributeGemData : gemDataList.getValue()) {
                if (item.getItemMeta().getDisplayName().equalsIgnoreCase(attributeGemData.name) && item.getItemMeta() != null && item.getItemMeta().getLore().equals(attributeGemData.lores)) {
                    return attributeGemData;
                }
            }
        }
        return null;
    }

    public ItemStack GemstoneSetAttribute(ItemStack input1, ItemStack input2) {
        AttributeGemData attributeGemData = getAttributeGemData(input2);
        if (attributeGemData == null) {
            return null;
        }
        ItemStack newItemStack = input1.clone();
        ItemMeta meta = newItemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        // 获取原来的属性值
        double originalAttributeValue = 0;
        if (meta.hasAttributeModifiers()) {
            for (AttributeModifier modifier : meta.getAttributeModifiers(attributeGemData.attribute)) {
                originalAttributeValue += modifier.getAmount();
            }
        }

        // 创建一个新的AttributeModifier，增加属性值
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), attributeGemData.attribute.name(), originalAttributeValue + attributeGemData.step, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);

        // 移除旧的属性修饰符
        meta.removeAttributeModifier(attributeGemData.attribute);

        // 添加新的属性修饰符
        meta.addAttributeModifier(attributeGemData.attribute, modifier);

        // 应用更改
        newItemStack.setItemMeta(meta);
        return newItemStack;
    }

    public ItemStack getAttributeGemItemStackByDisplay(String display, int level) {
        if (level >= 3) {
            return null;
        }
        String gemId = attributeGemDisplay2id.get(display);
        if (gemId == null) {
            return null;
        }
        List<ItemStack> lst = attributeGemItemStacks.get(gemId);
        ItemStack itemStack = lst.get(level).clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public List<ItemStack> getAllGemItemStacks(int level) {
        List<ItemStack> ans = new LinkedList<>();
        for (Map.Entry<String, List<ItemStack>> entry : attributeGemItemStacks.entrySet()) {
            ans.add(entry.getValue().get(level).clone());
        }
        return ans;
    }

    public List<String> getAttributeGemDisplays() {
        return new ArrayList<>(attributeGemDisplay2id.keySet());
    }

    private String colorFormat(String content) {
        if (content == null) {
            return "";
        }
        return content.replace("&", "§");
    }

    private ItemStack makeItemStack(AttributeGemData attributeGemData) {
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);

        // 获取SkullMeta
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        // 创建GameProfile对象
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", attributeGemData.headUrl));

        // 利用反射设置SkullMeta的GameProfile属性
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skullMeta.setDisplayName(attributeGemData.name);
        skullMeta.setLore(attributeGemData.lores);
        // 应用SkullMeta到ItemStack
        skullItem.setItemMeta(skullMeta);

        return skullItem;
    }

    private static class AttributeGemConfig {
        public Attribute attribute;

        public int step;
        public int maxNormalValue;

        public int maxRareValue;

        public int maxEpicValue;

        public String display;


        public String color;

        public String headUrl;
    }

    public static class AttributeGemData {
        public Attribute attribute;

        public int maxValue;

        public int step;

        public String name;

        public List<String> lores;

        public String headUrl;
    }
}
