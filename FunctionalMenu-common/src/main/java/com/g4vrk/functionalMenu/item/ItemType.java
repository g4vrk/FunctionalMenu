package com.g4vrk.functionalMenu.item;

import org.jetbrains.annotations.NotNull;

public enum ItemType {
    STATIC,
    DYNAMIC,

    ;

    ItemType() {
    }

    public static @NotNull ItemType safelyMatch(@NotNull String menuItemTypeStr, @NotNull ItemType def) {
        try {
            return ItemType.valueOf(menuItemTypeStr);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }

    public static @NotNull ItemType safelyMatch(@NotNull String menuItemTypeStr) {
        return safelyMatch(menuItemTypeStr, ItemType.DYNAMIC);
    }
}
