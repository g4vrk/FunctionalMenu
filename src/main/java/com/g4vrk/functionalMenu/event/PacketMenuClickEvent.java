package com.g4vrk.functionalMenu.event;

import com.g4vrk.functionalMenu.PacketMenu;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class PacketMenuClickEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled = false;

    private final int slot;

    private final Player whoClicked;
    private final WrapperPlayClientClickWindow.WindowClickType windowClickType;

    private final PacketMenu packetMenu;

    private final ItemStack packetItem;
    private final org.bukkit.inventory.ItemStack bukkitItem;

    public PacketMenuClickEvent(
            int slot,
            Player whoClicked,
            WrapperPlayClientClickWindow.WindowClickType windowClickType,
            PacketMenu packetMenu, ItemStack packetItem,
            org.bukkit.inventory.ItemStack bukkitItem
    ) {
        this.slot = slot;
        this.whoClicked = whoClicked;
        this.windowClickType = windowClickType;
        this.packetMenu = packetMenu;
        this.packetItem = packetItem;
        this.bukkitItem = bukkitItem;
    }

    public PacketMenuClickEvent(
            int slot,
            Player whoClicked,
            WrapperPlayClientClickWindow.WindowClickType windowClickType,
            PacketMenu packetMenu,
            org.bukkit.inventory.ItemStack bukkitItem
    ) {
        this.slot = slot;
        this.whoClicked = whoClicked;
        this.windowClickType = windowClickType;
        this.packetMenu = packetMenu;
        this.bukkitItem = bukkitItem;
        this.packetItem = SpigotConversionUtil.fromBukkitItemStack(bukkitItem);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
