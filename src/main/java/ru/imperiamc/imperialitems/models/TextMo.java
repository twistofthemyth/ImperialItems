package ru.imperiamc.imperialitems.models;

import lombok.Data;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

@Data
@Accessors(chain = true)
public class TextMo {

    @NotNull
    private String hexColor;

    @NotNull
    private String text;

    public TextMo(Component component) {
        TextColor textColor = component.color();
        this.hexColor = textColor == null ? "#FFFFFF" : textColor.asHexString();
        this.text = PlainTextComponentSerializer.plainText().serialize(component);
        this.setHexColor(hexColor);
        this.setText(text);
    }

    @NotNull
    public Component toComponent() {
        Component component = Component.text(text).asComponent();
        component = component.decoration(TextDecoration.ITALIC, false);
        component = component.color(TextColor.fromHexString(hexColor));
        return component;
    }
}
