package ru.imperiamc.imperialitems.models;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.logging.Logger;

@Data
@Accessors(chain = true)
public class AttributeMo {
    @NotNull
    private Attribute type;
    @NotNull
    private EquipmentSlot slot;
    @NotNull
    private AttributeModifier.Operation operation;
    private double amount;

    public AttributeMo(@NotNull Attribute type, @NotNull EquipmentSlot slot,
                       AttributeModifier.@NotNull Operation operation, double amount) {
        this.type = type;
        this.slot = slot;
        this.operation = operation;
        this.amount = amount;
    }

    public boolean contained(Set<AttributeMo> attributeSet) {
        return attributeSet.stream().anyMatch(e -> e.equals(this));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AttributeMo attribute) {
            if (!this.type.name().equals(attribute.getType().name())) {
                Logger.getAnonymousLogger().info("attribute type not equals");
                Logger.getAnonymousLogger().info(type.name());
                Logger.getAnonymousLogger().info(attribute.getType().name());
                return false;
            }

            if (!this.slot.name().equals(attribute.getSlot().name())) {
                Logger.getAnonymousLogger().info("attribute slot not equals");
                return false;
            }

            if (!this.operation.name().equals(attribute.operation.name())) {
                Logger.getAnonymousLogger().info("attribute operation not equals");
                return false;
            }

            if (!(this.amount == attribute.amount)) {
                Logger.getAnonymousLogger().info("attribute amount not equals");
                return false;
            }
            return true;
        }
        return false;
    }
}
