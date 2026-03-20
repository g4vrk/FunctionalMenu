package com.g4vrk.functionalMenu.session;

import com.g4vrk.functionalMenu.PacketMenu;
import com.g4vrk.functionalMenu.item.PacketMenuItem;
import com.g4vrk.functionalMenu.session.manager.MenuSessionManager;
import com.g4vrk.functionalMenu.util.PacketHelper;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketPlayerMenuSession implements PacketMenuSession {

    private final Player player;
    private final MenuSessionManager menuSessionManager;

    private final Deque<PacketMenu> stack = new ArrayDeque<>();
    private final AtomicInteger renderVersion = new AtomicInteger();
    private final AtomicInteger stateCounter = new AtomicInteger();

    private int currentWindowId = -1;

    public PacketPlayerMenuSession(
            @NotNull Player player,
            @NotNull PacketMenu root,
            @NotNull MenuSessionManager menuSessionManager
    ) {
        this.player = player;
        this.menuSessionManager = menuSessionManager;
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

        if (getCurrentMenu() != null || getCurrentMenu().getParent().isPresent()) {
            stack.removeFirst();
            stack.addFirst(getCurrentMenu().getParent().get());
            return;
        }

        stack.removeFirst();
        renderCurrentMenu();
    }

    @Override
    public @NotNull CompletableFuture<Void> render() {
        return renderCurrentMenu();
    }

    @Override
    public @NotNull CompletableFuture<Void> renderItem(int slot) {
        PacketMenu menu = getCurrentMenu();
        if (menu == null) return CompletableFuture.completedFuture(null);

        int version = renderVersion.get();

        return menu.build(player).thenAccept(view -> {
            if (renderVersion.get() != version || (slot < 0 || slot >= menu.getSize())) return;

            final PacketMenuItem item = view.getItem(slot);
            final ItemStack itemStack = item != null ? SpigotConversionUtil.fromBukkitItemStack(item.render(player))
                    : ItemStack.EMPTY;

            int stateId = stateCounter.incrementAndGet();

            final WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(
                    currentWindowId,
                    stateId,
                    slot,
                    itemStack
            );

            PacketHelper.sendPacket(player, packet);
        });
    }

    private CompletableFuture<Void> renderCurrentMenu() {
        final PacketMenu menu = getCurrentMenu();
        if (menu == null) return CompletableFuture.completedFuture(null);

        int version = renderVersion.incrementAndGet();

        return menu.build(player).thenAccept(view -> {
            if (renderVersion.get() != version) return;

            currentWindowId = menuSessionManager.next(player, this);
            int stateId = stateCounter.incrementAndGet();

            menu.render(player, currentWindowId);

            for (PacketMenuItem item : view.getAllItems()) {
                for (int slot : item.getSlots()) {
                    final ItemStack itemStack =  SpigotConversionUtil.fromBukkitItemStack(item.render(player));

                    final WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(
                            currentWindowId,
                            stateId,
                            slot,
                            itemStack
                    );

                    PacketHelper.sendPacket(player, packet);
                }
            }
        });
    }
}