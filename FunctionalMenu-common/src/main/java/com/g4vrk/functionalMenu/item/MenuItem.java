package com.g4vrk.functionalMenu.item;

import com.g4vrk.functionalMenu.click.InventoryClickType;
import com.g4vrk.functionalMenu.context.MenuContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface MenuItem<C extends MenuContext> {

    @NotNull ItemStack render(@NotNull C context);

    @NotNull ItemType getType();

    void onClick(@NotNull C context, int slot, @NotNull InventoryClickType clickType);

    int[] getSlots();

    default boolean isVisible(@NotNull C context) {
        return true;
    }
}