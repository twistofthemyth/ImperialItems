package ru.imperiamc.imperialitems.managers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.models.ItemMo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JsonRuleManager implements RuleManager {

    private final static Logger LOG = Logger.getLogger(JsonRuleManager.class.getName());
    private final Gson gson;

    public JsonRuleManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void addRule(@NotNull String loreSign, @NotNull ItemMo item) {
        Map<String, ItemMo> localData = getData(item.getMaterial());
        localData.put(loreSign, item);
        saveData(item.getMaterial(), localData);
    }

    @Override
    public @NotNull ItemMo getRuleItem(@NotNull Material material, @NotNull String loreSign) {
        return getData(material).get(loreSign);
    }

    @Override
    public String getLoreSign(@NotNull Material material, @NotNull List<Component> lore) {
        Map<String, ItemMo> localData = getData(material);
        String loreSign = null;
        for (Component loreString : lore) {
            String text = ((TextComponent) loreString).content();
            LOG.info("Ищем по '" + text + "'");
            loreSign = checkLoreComponent(localData, text);
            if (loreSign != null) {
                break;
            }
        }
        return loreSign;
    }

    @Nullable
    private String checkLoreComponent(@NotNull Map<String, ItemMo> localdata, @NotNull String lore) {
        return localdata.keySet().stream().filter(lore::contains).findFirst().orElse(null);
    }

    private void saveData(Material material, Map<String, ItemMo> localData) {
        try (FileWriter fw = new FileWriter(getFilePath(material))) {
            fw.write(gson.toJson(localData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, ItemMo> getData(Material material) {
        try (FileReader fr = new FileReader(getFilePath(material))) {
            return (gson.fromJson(fr, new TypeToken<Map<String, ItemMo>>() {
            }.getType()));
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private File getFilePath(Material material) {
        return new File(ImperialItems.getInstance().getDataFolder() + "\\" + material.name() + ".json");
    }
}
