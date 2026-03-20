package com.g4vrk.functionalMenu.item;

import org.jetbrains.annotations.NotNull;

public enum MenuItemType {
    STATIC,
    DYNAMIC,
    TICKING(20);

    private int tickingTime;

    MenuItemType() {
        this.tickingTime = -1;
    }

    MenuItemType(int tickingTime) {
        this.tickingTime = tickingTime;
    }

    public int getTickingTime() {
        return tickingTime;
    }

    public void setTickingTime(int tickingTime) {
        if (this != MenuItemType.TICKING) return;

        this.tickingTime = tickingTime;
    }

    public boolean isTickingType() {
        return getTickingTime() != -1;
    }

    public static MenuItemType safelyMatch(@NotNull String menuItemTypeStr, @NotNull MenuItemType def) {
        try {
            return MenuItemType.valueOf(menuItemTypeStr);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }

    public static MenuItemType safelyMatch(@NotNull String menuItemTypeStr) {
        return safelyMatch(menuItemTypeStr, MenuItemType.DYNAMIC);
    }
}
