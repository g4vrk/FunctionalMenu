package com.g4vrk.functionalMenu.session.manager;

import com.g4vrk.functionalMenu.context.MenuContext;
import com.g4vrk.functionalMenu.session.MenuSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleMenuSessionManager<C extends MenuContext> implements MenuSessionManager<C> {

    private final Map<Object, ContextState<C>> states = new ConcurrentHashMap<>();

    @Override
    public int next(@NotNull C context, @NotNull MenuSession<C> session) {
        final ContextState<C> state = states.computeIfAbsent(context.uniqueIdentifier(), k -> new ContextState<>());

        synchronized (state) {
            int id = state.counter.getAndIncrement();

            state.sessions.put(id, session);
            state.history.addLast(id);

            return id;
        }
    }

    @Override
    public @Nullable MenuSession<C> getSession(@NotNull C context, int windowId) {
        final ContextState<C> state = states.get(context.uniqueIdentifier());

        return (state == null) ? null : state.sessions.get(windowId);
    }

    @Override
    public @NotNull Optional<MenuSession<C>> getCurrentSession(@NotNull C context) {
        final ContextState<C> state = states.get(context.uniqueIdentifier());
        if (state == null) return Optional.empty();

        final Integer last = state.history.peekLast();
        if (last == null) return Optional.empty();

        return Optional.ofNullable(state.sessions.get(last));
    }

    @Override
    public void remove(@NotNull C context, int windowId) {
        final ContextState<C> state = states.get(context.uniqueIdentifier());
        if (state == null) return;

        synchronized (state) {
            final MenuSession<C> removed = state.sessions.remove(windowId);
            if (removed != null) {
                state.history.remove(windowId);
            }

            if (state.sessions.isEmpty()) {
                states.remove(context.uniqueIdentifier());
            }
        }
    }

    @Override
    public void clear(@NotNull C context) {
        states.remove(context.uniqueIdentifier());
    }

    static final class ContextState<C extends MenuContext> {
        final AtomicInteger counter = new AtomicInteger(1);
        final Map<Integer, MenuSession<C>> sessions = new ConcurrentHashMap<>();
        final Deque<Integer> history = new ConcurrentLinkedDeque<>();
    }
}