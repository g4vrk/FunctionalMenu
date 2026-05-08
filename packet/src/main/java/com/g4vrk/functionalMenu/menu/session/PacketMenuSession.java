package com.g4vrk.functionalMenu.menu.session;

import com.g4vrk.functionalMenu.Menu;
import com.g4vrk.functionalMenu.context.BukkitMenuContext;
import com.g4vrk.functionalMenu.item.MenuItem;
import com.g4vrk.functionalMenu.session.AbstractMenuSession;
import com.g4vrk.functionalMenu.session.manager.MenuSessionManager;
import com.g4vrk.functionalMenu.session.manager.SimpleMenuSessionManager;
import com.g4vrk.functionalMenu.util.PacketHelper;
import com.g4vrk.functionalMenu.view.MenuView;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketMenuSession<C extends BukkitMenuContext> extends AbstractMenuSession<C> {

    private final MenuSessionManager<C> menuSessionManager = new SimpleMenuSessionManager<>();

    private final AtomicInteger renderVersion = new AtomicInteger();
    private final AtomicInteger stateCounter = new AtomicInteger();

    private int currentWindowId = -1;

    private volatile MenuView<C> cachedView;

    public PacketMenuSession(
            @NotNull C context,
            @NotNull Menu<C> root
    ) {
        super(context, root);

        rebuildAndRender();
    }

    @Override
    public @NotNull CompletableFuture<Void> rebuildAndRender() {
        final Menu<C> menu = getCurrentMenu();
        if (menu == null) return CompletableFuture.completedFuture(null);

        final int version = renderVersion.incrementAndGet();

        return menu.build(getContext()).thenAccept(view -> {
            if (renderVersion.get() != version) return;

            cachedView = view;

            currentWindowId = menuSessionManager.next(getContext(), this);

            int stateId = nextState();

            menu.show(getContext(), currentWindowId);

            renderAll(view, stateId);
        });
    }

    @Override
    public @NotNull CompletableFuture<Void> renderItem(int slot) {
        final MenuView<C> view = cachedView;
        final Menu<C> menu = getCurrentMenu();

        if (menu == null || view == null) {
            return CompletableFuture.completedFuture(null);
        }

        if (slot < 0 || slot >= menu.getSize()) {
            return CompletableFuture.completedFuture(null);
        }

        final MenuItem<C> item = view.getItem(slot);

        final ItemStack itemStack =
                (item != null)
                        ? SpigotConversionUtil.fromBukkitItemStack(item.render(getContext()))
                        : ItemStack.EMPTY;

        sendSlot(slot, itemStack, nextState());

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public @NotNull CompletableFuture<Void> renderAllItems() {
        final MenuView<C> view = cachedView;

        if (view == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> renderAll(view, nextState()));
    }

    private void renderAll(
            final @NotNull MenuView<C> view,
            final int stateId
    ) {
        final Player player = getContext().getPlayer();

        for (final MenuItem<C> item : view.getAllItems()) {
            for (final int slot : item.getSlots()) {
                sendSlot(
                        player,
                        slot,
                        SpigotConversionUtil.fromBukkitItemStack(item.render(getContext())),
                        stateId
                );
            }
        }
    }

    private void sendSlot(
            final int slot,
            final @NotNull ItemStack itemStack,
            final int stateId
    ) {
        sendSlot(getContext().getPlayer(), slot, itemStack, stateId);
    }

    private void sendSlot(
            final @NotNull Player player,
            final int slot,
            final @NotNull ItemStack itemStack,
            final int stateId
    ) {
        PacketHelper.sendPacket(
                player,
                new WrapperPlayServerSetSlot(
                        currentWindowId,
                        stateId,
                        slot,
                        itemStack
                )
        );
    }

    private int nextState() {
        return stateCounter.incrementAndGet();
    }
}