package com.g4vrk.functionalMenu.session;

import com.g4vrk.functionalMenu.Menu;
import com.g4vrk.functionalMenu.context.MenuContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractMenuSession<C extends MenuContext> implements MenuSession<C> {

    private final C context;

    private final Deque<Menu<C>> stack = new ArrayDeque<>();

    public AbstractMenuSession(
            @NotNull C context,
            @NotNull Menu<C> root
    ) {
        this.context = context;
        stack.push(root);
    }

    @Override
    public @NotNull C getContext() {
        return context;
    }

    @Override
    public @Nullable Menu<C> getCurrentMenu() {
        return stack.peek();
    }

    @Override
    public void show(@NotNull Menu<C> menu) {
        stack.addFirst(menu);
        rebuildAndRender();
    }

    @Override
    public void back() {
        if (stack.size() <= 1) return;

        if (getCurrentMenu() != null || getCurrentMenu().getParent().isPresent()) {
            stack.removeFirst();
            stack.addFirst(getCurrentMenu().getParent().get());
            return;
        }

        stack.removeFirst();
        rebuildAndRender();
    }

    @Override
    public abstract @NotNull CompletableFuture<Void> rebuildAndRender();

    @Override
    public abstract @NotNull CompletableFuture<Void> renderItem(int slot);

    @Override
    public abstract @NotNull CompletableFuture<Void> renderAllItems();
}