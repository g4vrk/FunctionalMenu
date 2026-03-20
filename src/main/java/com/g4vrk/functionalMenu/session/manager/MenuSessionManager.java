package com.g4vrk.functionalMenu.session.manager;

import com.g4vrk.functionalMenu.session.PacketMenuSession;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class MenuSessionManager {

    private final Map<Player, AtomicInteger> counters = new HashMap<>();
    private final Map<Player, Map<Integer, PacketMenuSession>> sessions = new HashMap<>();

    public int next(@NotNull Player player, @NotNull PacketMenuSession session) {
        final int id = counters
                .computeIfAbsent(player, p -> new AtomicInteger(1))
                .getAndIncrement();

        sessions.computeIfAbsent(player, p -> new HashMap<>()).put(id, session);
        return id;
    }

    public PacketMenuSession getSession(@NotNull Player player, int windowId) {
        final Map<Integer, PacketMenuSession> map = sessions.get(player);

        if (map == null) return null;

        return map.get(windowId);
    }

    public void remove(@NotNull Player player, int windowId) {
        Map<Integer, PacketMenuSession> map = sessions.get(player);
        if (map != null) {
            map.remove(windowId);
            if (map.isEmpty()) sessions.remove(player);
        }
    }

    public void clear(@NotNull Player player) {
        counters.remove(player);
        sessions.remove(player);
    }
}