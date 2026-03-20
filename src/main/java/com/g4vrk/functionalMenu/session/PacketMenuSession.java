package com.g4vrk.functionalMenu.session;

import com.g4vrk.functionalMenu.PacketMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface PacketMenuSession {

    @NotNull Player getPlayer();

    @Nullable PacketMenu getCurrentMenu();

    void show(@NotNull PacketMenu menu);

    void back();

    @NotNull CompletableFuture<Void> render();
    @NotNull CompletableFuture<Void> renderItem(int slot);
}