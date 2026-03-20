package com.g4vrk.functionalMenu;

import com.g4vrk.functionalMenu.listener.MenuListener;
import com.g4vrk.functionalMenu.session.manager.WindowIdManager;
import com.g4vrk.functionalMenu.task.PluginTaskRunner;
import com.g4vrk.functionalMenu.task.TaskRunner;
import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionalMenuPlugin extends JavaPlugin {

    private static FunctionalMenuPlugin instance;
    private static Logger logger;

    private static TaskRunner taskRunner;

    private WindowIdManager windowIdManager;
    private MenuListener menuListener;

    public FunctionalMenuPlugin() {
        instance = this;
        logger = LoggerFactory.getLogger(getName());

        taskRunner = new PluginTaskRunner(this);
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();

        this.windowIdManager = new WindowIdManager();
        this.menuListener = new MenuListener(windowIdManager);

        PacketEvents.getAPI().getEventManager().registerListener(menuListener);
    }

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        PacketEvents.getAPI().init();

        long startupTime = System.currentTimeMillis() - startTime;

        logger.info("Плагин {} был успешно включен! ({} мс)", getName() + " v" + getDescription().getVersion(), startupTime);
        logger.info("Приятного использования! ~{}", String.join(", ", getDescription().getAuthors()));
    }

    @Override
    public void onDisable() {
        long startTime = System.currentTimeMillis();

        PacketEvents.getAPI().getEventManager().unregisterListener(menuListener);

        PacketEvents.getAPI().terminate();

        long endTime = System.currentTimeMillis() - startTime;

        logger.info("Плагин {} был успешно выключен! ({} мс)", getName() + " v" + getDescription().getVersion(), endTime);
        logger.info("Спасибо за использование! ~{}", String.join(", ", getDescription().getAuthors()));
    }

    public static FunctionalMenuPlugin getInstance() {
        return instance;
    }

    public static TaskRunner getTaskRunner() {
        return taskRunner;
    }
}
