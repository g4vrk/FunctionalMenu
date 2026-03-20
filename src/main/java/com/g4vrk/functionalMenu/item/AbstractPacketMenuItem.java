package com.g4vrk.functionalMenu.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.CollationKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPacketMenuItem implements PacketMenuItem {

    private final ItemType type;
    private final int[] slots;

    private final Map<Player, ItemStack> cachedItems = new HashMap<>();

    public AbstractPacketMenuItem(@NotNull ItemType type, int... slots) {
        this.type = type;
        this.slots = slots;
    }

    public AbstractPacketMenuItem(@NotNull ItemType type, @NotNull Collection<Integer> slots) {
        this.type = type;
        this.slots = slots.stream().mapToInt(Integer::intValue).toArray();
    }


    @Override
    public int[] getSlots() {
        return slots;
    }

    @Override
    public @NotNull ItemStack render(@NotNull Player player) {
        if (type == ItemType.STATIC) {
            return cachedItems.computeIfAbsent(player, this::buildItem);
        }
        return buildItem(player);
    }

    @Override
    public abstract void onClick(@NotNull Player player, int slot);

    protected abstract @NotNull ItemStack buildItem(@NotNull Player player);
}