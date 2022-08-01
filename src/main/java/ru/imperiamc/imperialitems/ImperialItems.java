package ru.imperiamc.imperialitems;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.imperiamc.imperialitems.commands.AddItemCommand;
import ru.imperiamc.imperialitems.commands.UpdateItemCommand;
import ru.imperiamc.imperialitems.listeners.ItemListener;
import ru.imperiamc.imperialitems.managers.JsonRuleManager;
import ru.imperiamc.imperialitems.managers.RuleManager;

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
        this.getCommand("addrule").setExecutor(new AddItemCommand());
        this.getCommand("updateitem").setExecutor(new UpdateItemCommand());
        ruleManager = new JsonRuleManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void createDataFolder() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }
}
