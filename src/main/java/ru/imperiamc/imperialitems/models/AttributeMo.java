package ru.imperiamc.imperialitems.models;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

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

    public boolean equals(Object obj) {
        if (obj instanceof AttributeMo attribute) {
            return this.type.getKey().getKey().equals(attribute.getType().getKey().getKey()) &&
                    this.slot.name().equals(attribute.getSlot().name()) &&
                    this.operation.name().equals(attribute.operation.name()) &&
                    this.amount == attribute.amount;
        }
        return false;
    }
}
