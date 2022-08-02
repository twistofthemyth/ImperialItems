package ru.imperiamc.imperialitems;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.imperiamc.imperialitems.listeners.ItemListener;
import ru.imperiamc.imperialitems.managers.JsonRuleManager;
import ru.imperiamc.imperialitems.managers.RuleManager;

import java.util.Objects;

public final class ImperialItems extends JavaPlugin {

    @Getter
    private static ImperialItems instance;

    @Getter
    private RuleManager ruleManager;

    @Override
    public void onEnable() {
        instance = this;
        this.createDataFolder();
        this.getServer().getPluginManager().registerEvents(new ItemListener(), this);
        Objects.requireNonNull(this.getCommand("ii")).setExecutor(new CommandExecutorImpl());
        ruleManager = new JsonRuleManager();
    }

    @Override
    public void onDisable() {
        ruleManager.save();
    }

    private void createDataFolder() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }
}
