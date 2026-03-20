package com.g4vrk.functionalMenu.session.manager;

import com.g4vrk.functionalMenu.session.PacketPlayerMenuSession;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WindowIdManager {

    private final Map<Player, AtomicInteger> counters = new HashMap<>();

    private final Map<Player, Map<Integer, PacketPlayerMenuSession>> sessions = new HashMap<>();

    public int next(Player player, PacketPlayerMenuSession session) {
        int id = counters
                .computeIfAbsent(player, p -> new AtomicInteger(1))
                .getAndIncrement();

        sessions.computeIfAbsent(player, p -> new HashMap<>()).put(id, session);
        return id;
    }

    public PacketPlayerMenuSession getSession(Player player, int windowId) {
        Map<Integer, PacketPlayerMenuSession> map = sessions.get(player);
        if (map == null) return null;
        return map.get(windowId);
    }

    public void remove(Player player, int windowId) {
        Map<Integer, PacketPlayerMenuSession> map = sessions.get(player);
        if (map != null) {
            map.remove(windowId);
            if (map.isEmpty()) sessions.remove(player);
        }
    }

    public void clear(Player player) {
        counters.remove(player);
        sessions.remove(player);
    }
}