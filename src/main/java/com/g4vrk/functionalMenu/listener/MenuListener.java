package com.g4vrk.functionalMenu.listener;

import com.g4vrk.functionalMenu.session.PacketMenuSession;
import com.g4vrk.functionalMenu.session.manager.MenuSessionManager;
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
        if (event.getConnectionState() != ConnectionState.PLAY || event.getPacketType() != PacketType.Play.Client.CLICK_WINDOW) return;

        final Player player = event.getPlayer();

        final WrapperPlayClientClickWindow click;

        try {
            click = new WrapperPlayClientClickWindow(event);
        } catch (Throwable t) {
            return;
        }

        final int windowId = click.getWindowId();
        final int slot = click.getSlot();

        final PacketMenuSession session = menuSessionManager.getSession(player, windowId);
        if (session == null || session.getCurrentMenu() == null) return;

        event.setCancelled(true);

        session.render();

        if (slot < 0 || slot >= session.getCurrentMenu().getSize()) return;

        try {
            session.getCurrentMenu().handleClick(player, slot);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}