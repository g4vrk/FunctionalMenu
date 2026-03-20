package com.g4vrk.functionalMenu;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import org.bukkit.entity.Player;

public class MenuListener extends PacketListenerAbstract {

    private final MenuSessionManager menuSessionManager;

    public MenuListener(MenuSessionManager menuSessionManager) {
        this.menuSessionManager = menuSessionManager;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getConnectionState() != ConnectionState.PLAY && event.getPacketType() != PacketType.Play.Client.CLICK_WINDOW) return;

        Player player = event.getPlayer();

        WrapperPlayClientClickWindow click = new WrapperPlayClientClickWindow(event);

        int windowId = click.getWindowId();
        int slot = click.getSlot();

        PacketMenu menu = menuSessionManager.getMenu(player, windowId);
        if (menu == null) return;

        event.setCancelled(true);

        handleClick(player, menu, slot);
    }

    private void handleClick(Player player, PacketMenu menu, int slot) {
        player.sendMessage("Ты кликнул слот " + slot + " в меню " + menu.getTitle());
    }
}