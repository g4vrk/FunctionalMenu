package com.g4vrk.functionalMenu.util;

import com.g4vrk.functionalMenu.click.InventoryClickType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import org.jetbrains.annotations.NotNull;

public final class InventoryClickMapper {

    private InventoryClickMapper() {
    }

    public static @NotNull InventoryClickType map(@NotNull WrapperPlayClientClickWindow click) {
        final WrapperPlayClientClickWindow.WindowClickType type = click.getWindowClickType();
        final int button = click.getButton();
        final int slot = click.getSlot();

        if (slot == -999) {
            return button == 0
                    ? InventoryClickType.WINDOW_BORDER_LEFT
                    : InventoryClickType.WINDOW_BORDER_RIGHT;
        }

        return switch (type) {
            case PICKUP -> button == 0
                    ? InventoryClickType.LEFT
                    : InventoryClickType.RIGHT;

            case QUICK_MOVE -> button == 0
                    ? InventoryClickType.SHIFT_LEFT
                    : InventoryClickType.SHIFT_RIGHT;

            case SWAP -> {
                if (button == 40) yield InventoryClickType.SWAP_OFFHAND;

                if (button >= 0 && button <= 8) yield InventoryClickType.fromHotbarKey(button + 1);

                yield InventoryClickType.NUMBER_KEY;
            }

            case CLONE -> InventoryClickType.MIDDLE;

            case THROW -> button == 0
                    ? InventoryClickType.DROP
                    : InventoryClickType.CONTROL_DROP;

            case PICKUP_ALL -> InventoryClickType.DOUBLE_CLICK;

            default -> InventoryClickType.UNKNOWN;
        };
    }
}