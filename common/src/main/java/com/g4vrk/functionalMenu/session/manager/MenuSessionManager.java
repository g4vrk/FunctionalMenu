package com.g4vrk.functionalMenu.session.manager;

import com.g4vrk.functionalMenu.context.MenuContext;
import com.g4vrk.functionalMenu.session.MenuSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface MenuSessionManager<C extends MenuContext> {

    int next(@NotNull C context, @NotNull MenuSession<C> session);

    @Nullable MenuSession<C> getSession(@NotNull C context, int windowId);

    @NotNull Optional<MenuSession<C>> getCurrentSession(@NotNull C context);

    void remove(@NotNull C context, int windowId);

    void clear(@NotNull C context);
}