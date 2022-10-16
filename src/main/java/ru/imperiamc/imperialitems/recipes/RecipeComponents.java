package ru.imperiamc.imperialitems.recipes;

import lombok.Getter;

public enum RecipeComponents {

    BLESSED_AMETHYST_SHARD("BlessedAmethystShard"),
    CURSED_AMETHYST_SHARD("CursedAmethystShard"),
    ENCHANTED_AMETHYST_SHARD("EnchantedAmethystShard"),
    REDSTONE_AMETHYST_SHARD("RedstoneAmethystShard"),
    BLOODY_MAP("BloodyMap"),
    BEAST_BLOOD("BeastBlood"),
    BLOODY_MAP_FRAGMENT("BloodyMapFragment"),
    BEWITCHED_SWORD_SHARD("BewitchedSwordShard"),
    BEWITCHED_AMELIA_SWORD("BewitchedAmeliaSword"),
    CRYSTAL_SWORD("CrystalSword");

    @Getter
    private final String name;

    RecipeComponents(String name) {
        this.name = name;
    }

    public String getKey() {
        return this.toString().toLowerCase();
    }
}
