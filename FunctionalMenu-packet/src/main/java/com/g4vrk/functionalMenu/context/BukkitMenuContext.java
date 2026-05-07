package com.g4vrk.functionalMenu.context;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitMenuContext implements MenuContext {

    private final Player player;

    public BukkitMenuContext(@NotNull Player player) {
        this.player = player;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull Object uniqueIdentifier() {
        return player.getUniqueId();
    }
}