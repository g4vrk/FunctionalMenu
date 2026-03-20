package com.g4vrk.functionalMenu.old.task;

import org.jetbrains.annotations.NotNull;

public interface TaskRunner {
    void runSync(@NotNull Runnable runnable);
    void runAsync(@NotNull Runnable runnable);
}
