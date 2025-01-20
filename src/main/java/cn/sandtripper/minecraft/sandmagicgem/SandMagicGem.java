package cn.sandtripper.minecraft.sandmagicgem;

import cn.sandtripper.minecraft.sandmagicgem.GemManager.GemManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class SandMagicGem extends JavaPlugin {

    private static final String[] enableTexts = {
            "\033[36m   _____                 _ __  __             _       _____                \033[0m",
            "\033[36m  / ____|               | |  \\/  |           (_)     / ____|               \033[0m",
            "\033[36m | (___   __ _ _ __   __| | \\  / | __ _  __ _ _  ___| |  __  ___ _ __ ___  \033[0m",
            "\033[36m  \\___ \\ / _` | '_ \\ / _` | |\\/| |/ _` |/ _` | |/ __| | |_ |/ _ \\ '_ ` _ \\ \033[0m",
            "\033[36m  ____) | (_| | | | | (_| | |  | | (_| | (_| | | (__| |__| |  __/ | | | | |\033[0m",
            "\033[36m |_____/ \\__,_|_| |_|\\__,_|_|  |_|\\__,_|\\__, |_|\\___|\\_____|\\___|_| |_| |_|\033[0m",
            "\033[36m                                         __/ |                             \033[0m",
            "\033[36m                                        |___/                              \033[0m",
            "\033[36m魔沙宝石 --by 沙酱紫漏\033[0m",
    };
    public MessageManager messageManager;
    public GemManager gemManager;
    public BackpackManager backpackManager;
    public GuiManager guiManager;
    public StationManager stationManager;

    @Override
    public void onEnable() {
        for (int i = 0; i < enableTexts.length; i++) {
            getLogger().info(enableTexts[i]);
        }

        saveDefaultConfig();

        this.messageManager = new MessageManager(this);
        this.gemManager = new GemManager(this);
        this.backpackManager = new BackpackManager(this);
        this.guiManager = new GuiManager(this);
        this.stationManager = new StationManager(this);

        getCommand("SandMagicGem").setExecutor(new CommandHandler(this));
        getCommand("SandMagicGem").setTabCompleter(new MyTabCompleter(this));
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);

        int pluginId = 20868;
        new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        stationManager.disable();
    }

    public void reload() {
        reloadConfig();
        messageManager.reload();
        gemManager.reload();
    }
}
