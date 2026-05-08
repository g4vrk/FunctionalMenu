package com.g4vrk.functionalMenu.item;

import com.g4vrk.functionalMenu.click.InventoryClickType;
import com.g4vrk.functionalMenu.context.MenuContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class AbstractMenuItem<C extends MenuContext> implements MenuItem<C> {

    private final ItemType type;
    private final int[] slots;

    public AbstractMenuItem(@NotNull ItemType type, int... slots) {
        this.type = type;
        this.slots = slots;
    }

    public AbstractMenuItem(@NotNull ItemType type, @NotNull Collection<Integer> slots) {
        this.type = type;
        this.slots = slots.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public @NotNull ItemType getType() {
        return type;
    }

    @Override
    public int[] getSlots() {
        return slots;
    }

    @Override
    public abstract @NotNull ItemStack render(@NotNull C context);

    @Override
    public abstract void onClick(@NotNull C context, int slot, @NotNull InventoryClickType clickType);
}