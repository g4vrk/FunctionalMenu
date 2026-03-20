package com.g4vrk.functionalMenu.view;

import com.g4vrk.functionalMenu.item.PacketMenuItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AbstractPacketMenuView implements PacketMenuView {

    private final List<PacketMenuItem> items;
    private final int size;

    public AbstractPacketMenuView(@NotNull List<PacketMenuItem> items, int size) {
        this.items = List.copyOf(items);
        this.size = size;
    }

    @Override
    public @Nullable PacketMenuItem getItem(int slot) {
        for (PacketMenuItem item : items) {
            for (int s : item.getSlots()) {
                if (s == slot) return item;
            }
        }
        return null;
    }

    @Override
    public @NotNull List<PacketMenuItem> getAllItems() {
        return items;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ItemStack getItemStack(int slot, @NotNull Player player) {
        PacketMenuItem item = getItem(slot);
        return item != null && item.isVisible(player) ? item.render(player) : null;
    }
}