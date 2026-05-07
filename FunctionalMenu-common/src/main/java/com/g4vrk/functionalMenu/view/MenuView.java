package com.g4vrk.functionalMenu.view;

import com.g4vrk.functionalMenu.context.MenuContext;
import com.g4vrk.functionalMenu.item.MenuItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MenuView<C extends MenuContext> {

    @Nullable MenuItem<C> getItem(int slot);

    @NotNull List<MenuItem<C>> getAllItems();

    int size();
}