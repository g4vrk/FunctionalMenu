package com.g4vrk.functionalMenu;

import com.g4vrk.functionalMenu.item.PacketMenuItem;
import com.g4vrk.functionalMenu.session.PacketMenuSession;
import com.g4vrk.functionalMenu.view.PacketMenuView;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PacketMenu {

    @NotNull PacketMenuSession newSession(@NotNull Player player);

    @NotNull Optional<PacketMenu> getParent();

    boolean canOpen(@NotNull Player player);

    int getSize();

    @NotNull Component getTitle();

    void render(@NotNull Player player, int windowId);

    void handleClick(@NotNull Player player, int slot);

    @NotNull CompletableFuture<PacketMenuView> build(@NotNull Player player);

    void addItem(@NotNull PacketMenuItem item);
}