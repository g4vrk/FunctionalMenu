package com.g4vrk.functionalMenu;

import com.g4vrk.functionalMenu.click.InventoryClickType;
import com.g4vrk.functionalMenu.context.MenuContext;
import com.g4vrk.functionalMenu.item.MenuItem;
import com.g4vrk.functionalMenu.session.MenuSession;
import com.g4vrk.functionalMenu.view.MenuView;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractMenu<C extends MenuContext> implements Menu<C> {

    private final Component title;
    private final int size;

    private final Map<Integer, MenuItem<C>> items = new Int2ObjectOpenHashMap<>();

    private final Menu<C> parent;

    protected AbstractMenu(
            @NotNull Component title,
            int size
    ) {
        this(title, size, null);
    }

    protected AbstractMenu(
            @NotNull Component title,
            int size,
            @Nullable Menu<C> parent
    ) {
        this.title = title;
        this.size = size;
        this.parent = parent;
    }

    @Override
    public @NotNull Optional<Menu<C>> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public boolean canOpen(@NotNull C context) {
        return true;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public void addItem(@NotNull MenuItem<C> item) {
        for (int slot : item.getSlots()) {
            items.put(slot, item);
        }
    }

    @NotNull
    public List<MenuItem<C>> getItems() {
        return new ObjectArrayList<>(items.values());
    }

    public abstract void show(@NotNull C context, int windowId);

    @Override
    public void handleClick(@NotNull C context, int slot, @NotNull InventoryClickType clickType) {
        final MenuItem<C> item = items.get(slot);

        if (item != null) {
            item.onClick(context, slot, clickType);
        }
    }

    @Override
    public abstract @NotNull MenuSession<C> newSession(@NotNull C context);

    @Override
    public abstract @NotNull CompletableFuture<MenuView<C>> build(@NotNull C context);
}