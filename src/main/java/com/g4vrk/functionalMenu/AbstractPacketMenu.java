package com.g4vrk.functionalMenu;

import com.g4vrk.functionalMenu.item.PacketMenuItem;
import com.g4vrk.functionalMenu.session.PacketMenuSession;
import com.g4vrk.functionalMenu.session.PacketPlayerMenuSession;
import com.g4vrk.functionalMenu.session.manager.MenuSessionManager;
import com.g4vrk.functionalMenu.view.AbstractPacketMenuView;
import com.g4vrk.functionalMenu.view.PacketMenuView;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractPacketMenu implements PacketMenu {

    private final Component title;
    private final int size;
    private final List<PacketMenuItem> items = new ArrayList<>();
    private final PacketMenu parent;

    protected final MenuSessionManager menuSessionManager;

    protected AbstractPacketMenu(@NotNull Component title, int size, @NotNull MenuSessionManager menuSessionManager) {
        this(title, size, menuSessionManager, null);
    }

    protected AbstractPacketMenu(@NotNull Component title, int size, @NotNull MenuSessionManager menuSessionManager, @Nullable PacketMenu parent) {
        this.title = title;
        this.size = size;
        this.parent = parent;
        this.menuSessionManager = menuSessionManager;
    }

    @Override
    public @NotNull Optional<PacketMenu> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public boolean canOpen(@NotNull Player player) {
        return true;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public void addItem(@NotNull PacketMenuItem item) {
        items.add(item);
    }

    @NotNull
    public List<PacketMenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public abstract void render(@NotNull Player player, int windowId);

    @Override
    public void handleClick(@NotNull Player player, int slot) {
        for (final PacketMenuItem item : items) {
            for (final int s : item.getSlots()) {
                if (s == slot) {
                    item.onClick(player, slot);
                    return;
                }
            }
        }
    }

    @Override
    public @NotNull PacketMenuSession newSession(@NotNull Player player) {
        return new PacketPlayerMenuSession(player, this, menuSessionManager);
    }

    @Override
    public @NotNull CompletableFuture<PacketMenuView> build(@NotNull Player player) {
        return CompletableFuture.completedFuture(new AbstractPacketMenuView(getItems(), getSize()));
    }
}