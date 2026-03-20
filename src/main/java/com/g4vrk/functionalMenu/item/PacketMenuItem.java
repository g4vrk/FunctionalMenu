package com.g4vrk.functionalMenu.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface PacketMenuItem {

    @NotNull ItemStack render(@NotNull Player player);

    void onClick(@NotNull Player player, int slot);

    int[] getSlots();

    default boolean isVisible(@NotNull Player player) {
        return true;
    }
}