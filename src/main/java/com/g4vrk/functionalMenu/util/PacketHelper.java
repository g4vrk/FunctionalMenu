package com.g4vrk.functionalMenu.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PacketHelper {

    private PacketHelper() {

    }

    public static void sendPacket(@NotNull Player player, @NotNull PacketWrapper<?> packetWrapper) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packetWrapper);
    }
}
