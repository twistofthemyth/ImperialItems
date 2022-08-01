package ru.imperiamc.imperialitems.models;

import lombok.Data;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class TextMo {

    @NotNull
    private String hexColor;

    private Map<String, Boolean> decorations;

    @NotNull
    private String text;

    public TextMo(Component component) {
        TextColor textColor = component.color();
        this.hexColor = textColor == null ? "#FFFFFF" : textColor.asHexString();
        this.text = PlainTextComponentSerializer.plainText().serialize(component);

        decorations = new HashMap<>();
        decorations.putAll(component.decorations().entrySet()
                .stream()
                .filter(e -> !TextDecoration.State.NOT_SET.equals(e.getValue()))
                .collect(Collectors.toMap(e -> e.getKey().name(), x -> TextDecoration.State.TRUE.equals(x.getValue()))));

        this.setHexColor(hexColor);
        this.setText(text);
    }

    @NotNull
    public Component toComponent() {
        Component component = Component.text(text).asComponent();
        for (String decName : decorations.keySet()) {
            component = component.decoration(TextDecoration.valueOf(decName), decorations.get(decName));
        }
        component = component.color(TextColor.fromHexString(hexColor));
        return component;
    }
}
