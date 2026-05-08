package com.g4vrk.functionalMenu;

import com.g4vrk.functionalMenu.click.InventoryClickType;
import com.g4vrk.functionalMenu.context.MenuContext;
import com.g4vrk.functionalMenu.item.MenuItem;
import com.g4vrk.functionalMenu.session.MenuSession;
import com.g4vrk.functionalMenu.view.MenuView;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Menu<C extends MenuContext> {

    @NotNull MenuSession<C> newSession(@NotNull C context);

    @NotNull Optional<Menu<C>> getParent();

    boolean canOpen(@NotNull C context);

    int getSize();

    @NotNull Component getTitle();

    void show(@NotNull C context, int windowId);

    void handleClick(@NotNull C context, int slot, @NotNull InventoryClickType clickType);

    @NotNull CompletableFuture<MenuView<C>> build(@NotNull C context);

    void addItem(@NotNull MenuItem<C> item);
}