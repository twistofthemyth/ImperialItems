package ru.imperiamc.imperialitems.models;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class ItemMo {
    @NotNull
    private Material material;
    @NotNull
    private List<TextMo> lore;
    @NotNull
    private Set<AttributeMo> attributes;
    @Nullable
    private Integer customModelData;
    @NotNull
    private Boolean isUnbreakable;

    public ItemMo(ItemStack itemStack) {
        Integer modelData = itemStack.getItemMeta().hasCustomModelData() ?
                itemStack.getItemMeta().getCustomModelData() :
                null;
        Set<AttributeMo> attributes = itemStack.getItemMeta().hasAttributeModifiers() ?
                toAttributeModel(itemStack.getItemMeta().getAttributeModifiers()) :
                new HashSet<>();
        List<TextMo> lore = itemStack.getItemMeta().hasLore() ?
                itemStack.getItemMeta().lore().stream().map(TextMo::new).collect(Collectors.toList()) :
                new ArrayList<>();

        this.setMaterial(itemStack.getType())
                .setLore(lore)
                .setAttributes(attributes)
                .setCustomModelData(modelData)
                .setIsUnbreakable(itemStack.getItemMeta().isUnbreakable());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemMo itemMo) {

            //lore
            if (lore.size() != itemMo.getLore().size()) {
                Logger.getAnonymousLogger().info("lore size not equal");
                return false;
            }
            for (int i = 0; i < lore.size(); i++) {
                if (!lore.get(i).getText().equals(itemMo.getLore().get(i).getText())) {
                    Logger.getAnonymousLogger().info("lore content not equal");
                    return false;
                }
            }

            //attributes
            if (attributes.size() != itemMo.getAttributes().size()) {
                Logger.getAnonymousLogger().info("attributes size equal");
                return false;
            }

            if (!attributes.stream().allMatch(e -> e.contained(itemMo.getAttributes()))) {
                Logger.getAnonymousLogger().info("attributes content not equal");
                return false;
            }

            //customModelData
            if (customModelData != null && itemMo.getCustomModelData() != null) {
                if (!this.customModelData.equals(itemMo.getCustomModelData())) {
                    Logger.getAnonymousLogger().info("CMD not equal");
                    return false;
                }
            } else if (customModelData == null && itemMo.getCustomModelData() != null) {
                Logger.getAnonymousLogger().info("CMD not equal (new is unset)");
                return false;
            } else if (customModelData != null) {
                Logger.getAnonymousLogger().info("CMD not equal (new is set)");
                return false;
            }
            return true;
        }
        return false;
    }

    @NotNull
    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (customModelData != null) {
            itemMeta.setCustomModelData(customModelData);
        }
        itemMeta.setAttributeModifiers(fromAttributeModel(attributes));
        itemMeta.lore(lore.stream().map(TextMo::toComponent).collect(Collectors.toList()));
        itemMeta.setUnbreakable(isUnbreakable);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean hasLore(String str) {
        return this.getLore().stream().anyMatch(e -> e.getText().contains(str));
    }

    private static AttributeMo toAttributeModel(Attribute attribute, AttributeModifier modifier) {
        return new AttributeMo(attribute, modifier.getSlot() == null ? EquipmentSlot.HAND : modifier.getSlot(),
                modifier.getOperation(), modifier.getAmount());
    }

    private static AttributeModifier fromAttributeModel(AttributeMo attributeMo) {
        return new AttributeModifier(UUID.randomUUID(), attributeMo.getType().name(), attributeMo.getAmount(),
                attributeMo.getOperation(), attributeMo.getSlot());
    }

    private static Set<AttributeMo> toAttributeModel(Multimap<Attribute, AttributeModifier> attributeMap) {
        Set<AttributeMo> attributeMoList = new HashSet<>();
        attributeMap.forEach((attribute, modifier) -> attributeMoList.add(toAttributeModel(attribute, modifier)));
        return attributeMoList;
    }

    private static Multimap<Attribute, AttributeModifier> fromAttributeModel(Set<AttributeMo> attributeMoList) {
        Map<Attribute, AttributeModifier> attributeAttributeModifierMap = new HashMap<>();
        attributeMoList.forEach(e -> attributeAttributeModifierMap.put(e.getType(), fromAttributeModel(e)));
        return Multimaps.forMap(attributeAttributeModifierMap);
    }
}
