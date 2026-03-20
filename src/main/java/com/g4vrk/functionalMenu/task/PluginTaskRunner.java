package com.g4vrk.functionalMenu.old.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

public class PluginTaskRunner implements TaskRunner {

    private final Plugin plugin;
    private final BukkitScheduler bukkitScheduler;

    public PluginTaskRunner(
            @NotNull Plugin plugin
    ) {
        this.plugin = plugin;
        this.bukkitScheduler = Bukkit.getScheduler();
    }

    @Override
    public void runSync(@NotNull Runnable runnable) {
        bukkitScheduler.runTask(plugin, runnable);
    }

    @Override
    public void runAsync(@NotNull Runnable runnable) {
        bukkitScheduler.runTaskAsynchronously(plugin, runnable);
    }
}
