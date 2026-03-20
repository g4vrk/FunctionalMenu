package com.g4vrk.functionalMenu.session;

import com.g4vrk.functionalMenu.PacketMenu;
import com.g4vrk.functionalMenu.session.manager.WindowIdManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerPacketMenuSession implements PacketMenuSession {

    private final Player player;
    private final WindowIdManager windowIdManager;

    private final Deque<PacketMenu> stack = new ArrayDeque<>();
    private final AtomicInteger renderVersion = new AtomicInteger();

    public PlayerPacketMenuSession(@NotNull Player player, @NotNull PacketMenu root, @NotNull WindowIdManager windowIdManager) {
        this.player = player;
        this.windowIdManager = windowIdManager;
        stack.push(root);

        renderCurrentMenu();
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @Nullable PacketMenu getCurrentMenu() {
        return stack.peek();
    }
    @Override
    public void show(@NotNull PacketMenu menu) {
        stack.addFirst(menu);
        renderCurrentMenu();
    }

    @Override
    public void back() {
        if (stack.size() <= 1) return;
        stack.removeFirst();
        renderCurrentMenu();
    }

    @Override
    public void refreshCurrent() {
        renderCurrentMenu();
    }

    @NotNull
    @Override
    public CompletableFuture<Void> render() {
        return renderCurrentMenu();
    }

    private CompletableFuture<Void> renderCurrentMenu() {
        PacketMenu menu = getCurrentMenu();
        if (menu == null) return CompletableFuture.completedFuture(null);

        int version = renderVersion.incrementAndGet();

        return menu.build(player)
                .thenAccept(view -> {
                    if (renderVersion.get() != version) return;

                    int windowId = menu.windowIdCounter.getAndIncrement();

                    menu.render(player, windowId);
                });
    }
}