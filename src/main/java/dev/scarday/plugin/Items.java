package dev.scarday.plugin;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.scarday.plugin.annotation.OnlyPlayer;
import dev.scarday.plugin.command.ItemsCommand;
import dev.scarday.plugin.config.Configuration;
import dev.scarday.plugin.handler.litecommands.InvalidUsageHandler;
import dev.scarday.plugin.validation.ValidationOnlyPlayer;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

import static dev.scarday.plugin.util.ColorUtil.*;

@Getter
public final class Items extends JavaPlugin {

    Configuration pluginConfig;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        loadConfig();
        registerCommands();
    }

    private void loadConfig() {
        try {
            this.pluginConfig = ConfigManager.create(Configuration.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
                it.withBindFile(new File(this.getDataFolder(), "config.yml"));
                it.saveDefaults();
                it.load(true);
            });
        } catch (Exception exception) {
            this.getLogger().log(Level.SEVERE, "Error loading 'config.yml'", exception);
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        getLogger().info("Configuration 'config.yml' loaded.");
    }

    private void registerCommands() {
        val messages = pluginConfig.getMessages();
        this.liteCommands = LiteBukkitFactory.builder("Items", this)
                .annotations(configuration -> configuration
                        .validator(CommandSender.class, OnlyPlayer.class, new ValidationOnlyPlayer()))
                .commands(new ItemsCommand(this))
                .message(LiteBukkitMessages.PLAYER_NOT_FOUND, colorize(messages.getPlayerNotFound()))
                .invalidUsage(new InvalidUsageHandler(pluginConfig))
                .build();
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }
    }

    public void reloadConfig() {
        this.pluginConfig = (Configuration) pluginConfig.load();
    }
}
