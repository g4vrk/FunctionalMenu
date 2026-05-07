package com.g4vrk.functionalMenu.menu;

import com.g4vrk.functionalMenu.AbstractMenu;
import com.g4vrk.functionalMenu.Menu;
import com.g4vrk.functionalMenu.context.BukkitMenuContext;
import com.g4vrk.functionalMenu.menu.session.DefaultMenuSession;
import com.g4vrk.functionalMenu.menu.view.DefaultMenuView;
import com.g4vrk.functionalMenu.session.MenuSession;
import com.g4vrk.functionalMenu.util.PacketHelper;
import com.g4vrk.functionalMenu.view.MenuView;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DefaultMenu extends AbstractMenu<BukkitMenuContext> {

    public DefaultMenu(
            @NotNull Component title,
            int size
    ) {
        super(title, size);
    }

    public DefaultMenu(
            @NotNull Component title,
            int size,
            @Nullable Menu<BukkitMenuContext> parent
    ) {
        super(title, size, parent);
    }

    @Override
    public void show(@NotNull BukkitMenuContext context, int windowId) {
        final int type = switch (getSize()) {
            case 9, 1 -> 0;
            case 18, 2 -> 1;
            case 27, 3 -> 2;
            case 36, 4 -> 3;
            case 45, 5 -> 4;
            case 54, 6 -> 5;
            default -> 5;
        };

        final WrapperPlayServerOpenWindow openWindow = new WrapperPlayServerOpenWindow(
                windowId,
                type,
                getTitle()
        );
        openWindow.setLegacySlots(getSize());

        PacketHelper.sendPacket(context.getPlayer(), openWindow);
    }

    @Override
    public @NotNull CompletableFuture<MenuView<BukkitMenuContext>> build(@NotNull BukkitMenuContext context) {
        return CompletableFuture.completedFuture(new DefaultMenuView(getItems(), getSize()));
    }

    @Override
    public @NotNull MenuSession<BukkitMenuContext> newSession(@NotNull BukkitMenuContext context) {
        return new DefaultMenuSession(context, this);
    }
}
