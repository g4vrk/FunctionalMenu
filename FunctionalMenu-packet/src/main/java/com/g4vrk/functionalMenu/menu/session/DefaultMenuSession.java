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

public class DefaultMenuSession extends AbstractMenuSession<BukkitMenuContext> {

    private final MenuSessionManager<BukkitMenuContext> menuSessionManager = new SimpleMenuSessionManager<>();

    private final AtomicInteger renderVersion = new AtomicInteger();
    private final AtomicInteger stateCounter = new AtomicInteger();

    private int currentWindowId = -1;

    private volatile MenuView<BukkitMenuContext> cachedView;

    public DefaultMenuSession(
            @NotNull BukkitMenuContext context,
            @NotNull Menu<BukkitMenuContext> root
    ) {
        super(context, root);

        rebuildAndRender();
    }

    @Override
    public @NotNull CompletableFuture<Void> rebuildAndRender() {
        final Menu<BukkitMenuContext> menu = getCurrentMenu();
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
        final MenuView<BukkitMenuContext> view = cachedView;
        final Menu<BukkitMenuContext> menu = getCurrentMenu();

        if (menu == null || view == null) {
            return CompletableFuture.completedFuture(null);
        }

        if (slot < 0 || slot >= menu.getSize()) {
            return CompletableFuture.completedFuture(null);
        }

        final MenuItem<BukkitMenuContext> item = view.getItem(slot);

        final ItemStack itemStack =
                (item != null)
                        ? SpigotConversionUtil.fromBukkitItemStack(item.render(getContext()))
                        : ItemStack.EMPTY;

        sendSlot(slot, itemStack, nextState());

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public @NotNull CompletableFuture<Void> renderAllItems() {
        final MenuView<BukkitMenuContext> view = cachedView;

        if (view == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> renderAll(view, nextState()));
    }

    private void renderAll(
            final @NotNull MenuView<BukkitMenuContext> view,
            final int stateId
    ) {
        final Player player = getContext().getPlayer();

        for (final MenuItem<BukkitMenuContext> item : view.getAllItems()) {
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