package com.g4vrk.functionalMenu.types;

import com.g4vrk.functionalMenu.AbstractPacketMenu;
import com.g4vrk.functionalMenu.PacketMenu;
import com.g4vrk.functionalMenu.session.manager.MenuSessionManager;
import com.g4vrk.functionalMenu.util.PacketHelper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultPacketMenu extends AbstractPacketMenu {

    public DefaultPacketMenu(@NotNull Component title, int size, @NotNull MenuSessionManager menuSessionManager) {
        super(title, size, menuSessionManager);
    }

    public DefaultPacketMenu(@NotNull Component title, int size, @NotNull MenuSessionManager menuSessionManager, @Nullable PacketMenu parent) {
        super(title, size, menuSessionManager, parent);
    }

    @Override
    public void render(@NotNull Player player, int windowId) {
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

        PacketHelper.sendPacket(player, openWindow);
    }
}
