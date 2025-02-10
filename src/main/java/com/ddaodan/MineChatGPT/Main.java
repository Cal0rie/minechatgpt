package com.ddaodan.MineChatGPT;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

import java.util.Objects;
import java.util.Set;

public final class Main extends JavaPlugin implements Listener {
    private ConfigManager configManager;
    private CommandHandler commandHandler;
    private MineChatGPTTabCompleter tabCompleter;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        commandHandler = new CommandHandler(this, configManager);
        tabCompleter = new MineChatGPTTabCompleter(configManager);
        Objects.requireNonNull(getCommand("chatgpt")).setExecutor(commandHandler);
        Objects.requireNonNull(getCommand("chatgpt")).setTabCompleter(tabCompleter);
        if (configManager.isDebugMode()) {
            getLogger().info( "DEBUG MODE IS TRUE!!!!!");
        }
        // Initialize bStats
        int pluginId = 22635;
        new Metrics(this, pluginId);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player who = event.getPlayer();
        if (message.toLowerCase().startsWith(configManager.getWakeUpWord() + ' ')) {
            // 处理以 配置中的唤醒词 为开头的消息
            commandHandler.handleChat(who, message);
            return;
        }
        return;
    }

}