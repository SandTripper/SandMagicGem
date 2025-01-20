package cn.sandtripper.minecraft.sandmagicgem.GemManager;

import cn.sandtripper.minecraft.sandmagicgem.SandMagicGem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.LinkedList;
import java.util.List;

import static cn.sandtripper.minecraft.sandmagicgem.GemManager.GemManager.GemType.*;

public class GemManager {
    // public final static String GEM_UNBREAKABLE_NAME_TEMP =
    // "§7§l§k||{OTHER_COLOR}§l{OTHER_NAME}宝石§7§l§k||";
    //
    // public final static String[] GEM_UNBREAKABLE_LORES_TEMP = {
    // "§7在宝石镶嵌台内将宝石放到装备上",
    // "§7即可将装备设置成{OTHER_NAME}",
    // };

    public final static String[] GEM_LEVEL_NAME = {
            "§f普通",
            "§b稀有",
            "§d史诗",
    };

    public final static String[] GEM_LEVEL_DISPLAY = {
            "",
            "幻·",
            "圣·",
    };

    private EnchantGemManager enchantGemManager;

    private OtherGemManager otherGemManager;

    // private AttributeGemManager attributeGemManager;

    public GemManager(SandMagicGem plugin) {
        this.enchantGemManager = new EnchantGemManager(plugin);
        // this.attributeGemManager = new AttributeGemManager(plugin);
        this.otherGemManager = new OtherGemManager(plugin);
        initData();
    }

    private void initData() {
    }

    public void reload() {
        initData();
        enchantGemManager.reload();
        // attributeGemManager.reload();
        otherGemManager.reload();
    }

    public ItemStack GemstoneSet(ItemStack input1, ItemStack input2) {
        GemType gemType = getGemType(input2);
        if (gemType == UNKNOWN) {
            return null;
        }
        switch (gemType) {
            case ENCHANT:
                return enchantGemManager.GemstoneSetEnchant(input1, input2);
            // case ATTRIBUTE:
            // return attributeGemManager.GemstoneSetAttribute(input1, input2);
            case OTHER:
                return otherGemManager.GemstoneSetOther(input1, input2);
        }
        return null;
    }

    @NonNull
    public GemType getGemType(ItemStack item) {
        if (item == null) {
            return UNKNOWN;
        }
        if (item.getType() != Material.PLAYER_HEAD) {
            return UNKNOWN;
        }
        EnchantGemManager.EnchantGemData enchantGemData = enchantGemManager.getEnchantGemData(item);
        if (enchantGemData != null) {
            return ENCHANT;
        }
        // AttributeGemManager.AttributeGemData attributeGemData =
        // attributeGemManager.getAttributeGemData(item);
        // if (attributeGemData != null) {
        // return ATTRIBUTE;
        // }
        OtherGemManager.OtherGemData otherGemData = otherGemManager.getOtherGemData(item);
        if (otherGemData != null) {
            return OTHER;
        }
        return UNKNOWN;
    }

    public ItemStack getGemItemStackByDisplay(String display, int level) {
        if (level >= 3) {
            return null;
        }
        ItemStack itemStack;
        itemStack = enchantGemManager.getEnchantGemItemStackByDisplay(display, level);
        if (itemStack != null) {
            return itemStack;
        }
        // itemStack = attributeGemManager.getAttributeGemItemStackByDisplay(display,
        // level);
        // if (itemStack != null) {
        // return itemStack;
        // }
        itemStack = otherGemManager.getOtherGemItemStackByDisplay(display);
        if (itemStack != null) {
            return itemStack;
        }
        return null;
    }

    public List<ItemStack> getAllGemItemStacks(int level) {
        List<ItemStack> ans = new LinkedList<>();
        ans.addAll(enchantGemManager.getAllGemItemStacks(level));
        // ans.addAll(attributeGemManager.getAllGemItemStacks(level));
        ans.addAll(otherGemManager.getAllGemItemStacks());
        return ans;
    }

    public List<String> getGemDisplays() {
        List<String> lst = new LinkedList<>();
        lst.addAll(enchantGemManager.getEnchantGemDisplays());
        // lst.addAll(attributeGemManager.getAttributeGemDisplays());
        lst.addAll(otherGemManager.getOtherGemDisplays());
        return lst;
    }

    public enum GemType {
        ENCHANT,
        ATTRIBUTE,
        OTHER,
        BUFF,
        UNKNOWN,
    }

}
