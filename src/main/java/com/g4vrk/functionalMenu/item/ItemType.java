package com.g4vrk.functionalMenu.item;

import org.jetbrains.annotations.NotNull;

public enum ItemType {
    STATIC,
    DYNAMIC,
    TICKING(20);

    private int tickingTime;

    ItemType() {
        this.tickingTime = -1;
    }

    ItemType(int tickingTime) {
        this.tickingTime = tickingTime;
    }

    public int getTickingTime() {
        return tickingTime;
    }

    public void setTickingTime(int tickingTime) {
        if (this != ItemType.TICKING) return;

        this.tickingTime = tickingTime;
    }

    public boolean isTickingType() {
        return getTickingTime() != -1;
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
