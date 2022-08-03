package ru.imperiamc.imperialitems.models;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

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
            return type.name().equals(attribute.getType().name()) &&
                    slot.name().equals(attribute.getSlot().name()) &&
                    operation.name().equals(attribute.operation.name()) &&
                    amount == attribute.amount;
        }
        return false;
    }
}
