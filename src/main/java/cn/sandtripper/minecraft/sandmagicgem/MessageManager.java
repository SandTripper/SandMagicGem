package cn.sandtripper.minecraft.sandmagicgem;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MessageManager {

    public String backpackFullThingsFallOnGround;
    public String instructionFormatError;
    public String itemGiveSuccess;
    public String successOpenPlayerGui;
    public String requirePlayerExecutionIdentity;
    public String playerNotOnline;
    public String playerNotExist;
    public String reloadSuccess;
    public String amountError;

    public String noVerification;

    public List<String> helps;
    ConfigReader messageConfigReader;
    FileConfiguration messageConfig;
    private String prefix;

    MessageManager(JavaPlugin plugin) {
        this.messageConfigReader = new ConfigReader(plugin, "message.yml");
        messageConfigReader.saveDefaultConfig();
        initTmp();
    }

    private void initTmp() {
        this.messageConfig = messageConfigReader.getConfig();
        this.prefix = colorFormat(messageConfig.getString("prefix"));
        this.backpackFullThingsFallOnGround = format(messageConfig.getString("backpack-full-things-fall-on-ground"));
        this.instructionFormatError = format(messageConfig.getString("instruction-format-error"));
        this.itemGiveSuccess = format(messageConfig.getString("item-give-success"));
        this.successOpenPlayerGui = format(messageConfig.getString("success-open-player-gui"));
        this.requirePlayerExecutionIdentity = format(messageConfig.getString("require-player-execution-identity"));
        this.playerNotOnline = format(messageConfig.getString("player-not-online"));
        this.playerNotExist = format(messageConfig.getString("player-not-exist"));
        this.reloadSuccess = format(messageConfig.getString("reload-success"));
        this.helps = messageConfig.getStringList("helps");
        this.helps.replaceAll(s -> colorFormat(s));
        this.amountError = format(messageConfig.getString("amount-error"));
        this.noVerification = format(messageConfig.getString("no-verification"));
    }

    public void reload() {
        this.messageConfigReader.reloadConfig();
        initTmp();
    }

    private String format(String message) {
        return this.prefix + colorFormat(message);
    }

    private String colorFormat(String message) {
        if (message == null) {
            return "";
        }
        return message.replace("&", "§");
    }

}
