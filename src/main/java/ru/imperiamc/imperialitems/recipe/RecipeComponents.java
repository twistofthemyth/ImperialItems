package ru.imperiamc.imperialitems.recipe;

import lombok.Getter;

public enum RecipeComponents {
    BLOODY_MAP("BloodyMap"),
    BEAST_BLOOD("BeastBlood"),
    BLOODY_MAP_FRAGMENT("BloodyMapFragment");

    @Getter
    private final String name;

    RecipeComponents(String name) {
        this.name = name;
    }

    public String getKey() {
        return this.toString().toLowerCase();
    }
}
