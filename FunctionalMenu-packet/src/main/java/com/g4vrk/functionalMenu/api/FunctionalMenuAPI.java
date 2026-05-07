package com.g4vrk.functionalMenu.api;

import com.g4vrk.functionalMenu.listener.MenuListener;
import com.g4vrk.functionalMenu.menu.session.manager.BukkitMenuSessionManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FunctionalMenuAPI {

    public static final FunctionalMenuAPI INSTANCE = new FunctionalMenuAPI();
    private static Plugin plugin;

    private MenuListener menuListener;

    private FunctionalMenuAPI() {
    }

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    public void load(final @NotNull Plugin plugin) {
        FunctionalMenuAPI.plugin = plugin;

        final PacketEventsAPI<?> packetEventsAPI = PacketEvents.getAPI();

        if (!packetEventsAPI.isLoaded()) {
            PacketEvents.setAPI(SpigotPacketEventsBuilder.build(plugin));

            packetEventsAPI.getSettings()
                    .checkForUpdates(false)
                    .bStats(false)
                    .debug(false);

            packetEventsAPI.load();
        }

        this.menuListener = new MenuListener(BukkitMenuSessionManager.INSTANCE);

        packetEventsAPI.getEventManager().registerListener(menuListener);
    }

    public void init() {
        final PacketEventsAPI<?> packetEventsAPI = PacketEvents.getAPI();

        if (!packetEventsAPI.isInitialized()) packetEventsAPI.init();
    }

    public void terminate() {
        final PacketEventsAPI<?> packetEventsAPI = PacketEvents.getAPI();

        if (!packetEventsAPI.isTerminated()) {
            packetEventsAPI.getEventManager().unregisterListener(menuListener);

            packetEventsAPI.terminate();
        }

    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
