package com.g4vrk.functionalMenu.view;

import com.g4vrk.functionalMenu.context.MenuContext;
import com.g4vrk.functionalMenu.item.MenuItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleMenuView<C extends MenuContext> implements MenuView<C> {

    private final List<MenuItem<C>> items;
    private final int size;

    public SimpleMenuView(@NotNull List<MenuItem<C>> items, int size) {
        this.items = List.copyOf(items);
        this.size = size;
    }

    @Override
    public @Nullable MenuItem<C> getItem(int slot) {
        for (MenuItem<C> item : items) {
            for (int s : item.getSlots()) {
                if (s == slot) return item;
            }
        }
        return null;
    }

    @Override
    public @NotNull List<MenuItem<C>> getAllItems() {
        return items;
    }

    @Override
    public int size() {
        return size;
    }
}