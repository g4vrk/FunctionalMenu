package com.g4vrk.functionalMenu.view;

import com.g4vrk.functionalMenu.item.PacketMenuItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PacketMenuView {

    @Nullable PacketMenuItem getItem(int slot);

    @NotNull List<PacketMenuItem> getAllItems();

    int size();

    @Nullable
    default ItemStack getItemStack(int slot, @NotNull Player player) {
        PacketMenuItem item = getItem(slot);
        return item != null && item.isVisible(player) ? item.render(player) : null;
    }
}