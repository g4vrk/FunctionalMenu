package com.g4vrk.functionalMenu.test;

import com.g4vrk.functionalMenu.item.PacketMenuItem;
import com.g4vrk.functionalMenu.session.manager.MenuSessionManager;
import com.g4vrk.functionalMenu.types.DefaultPacketMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {

    private final MenuSessionManager menuSessionManager;

    public TestCommand(MenuSessionManager manager) {
        this.menuSessionManager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        DefaultPacketMenu menu = new DefaultPacketMenu(
                Component.text("TEST MENU"),
                27,
                menuSessionManager
        );

        menu.addItem(new PacketMenuItem() {

            @Override
            public int[] getSlots() {
                return new int[]{13};
            }

            @Override
            public void onClick(@NotNull Player player, int slot) {
                player.sendMessage("CLICK WORKS: slot=" + slot);
            }

            @Override
            public @NotNull ItemStack render(@NotNull Player player) {
                return new ItemStack(Material.DIAMOND);
            }
        });

        menu.newSession(player).show(menu);

        return true;
    }
}