package com.g4vrk.functionalMenu.listener;

import com.g4vrk.functionalMenu.click.InventoryClickType;
import com.g4vrk.functionalMenu.context.BukkitMenuContext;
import com.g4vrk.functionalMenu.session.MenuSession;
import com.g4vrk.functionalMenu.session.manager.MenuSessionManager;
import com.g4vrk.functionalMenu.util.InventoryClickMapper;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import org.bukkit.entity.Player;

public class PacketClickListener extends PacketListenerAbstract {

    private final MenuSessionManager<BukkitMenuContext> menuSessionManager;

    public PacketClickListener(MenuSessionManager<BukkitMenuContext> menuSessionManager) {
        this.menuSessionManager = menuSessionManager;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getConnectionState() != ConnectionState.PLAY
                || event.getPacketType() != PacketType.Play.Client.CLICK_WINDOW
                || !(event.getPlayer() instanceof final Player player)) return;

        final WrapperPlayClientClickWindow click;

        try {
            click = new WrapperPlayClientClickWindow(event);
        } catch (Throwable t) {
            return;
        }

        final int windowId = click.getWindowId();
        final int slot = click.getSlot();

        final BukkitMenuContext context = new BukkitMenuContext(player);
        final MenuSession<BukkitMenuContext> session = menuSessionManager.getSession(context, windowId);
        if (session == null || session.getCurrentMenu() == null) return;

        event.setCancelled(true);

        session.renderAllItems();

        if (slot < 0 || slot >= session.getCurrentMenu().getSize()) return;

        try {
            final InventoryClickType clickType = InventoryClickMapper.map(click);

            session.getCurrentMenu().handleClick(session.getContext(), slot, clickType);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}