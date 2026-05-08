package com.g4vrk.functionalMenu.session;

import com.g4vrk.functionalMenu.Menu;
import com.g4vrk.functionalMenu.context.MenuContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface MenuSession<C extends MenuContext> {

    @NotNull C getContext();

    @Nullable Menu<C> getCurrentMenu();

    void show(@NotNull Menu<C> menu);

    void back();

    @NotNull CompletableFuture<Void> rebuildAndRender();

    @NotNull CompletableFuture<Void> renderItem(int slot);

    @NotNull CompletableFuture<Void> renderAllItems();
}