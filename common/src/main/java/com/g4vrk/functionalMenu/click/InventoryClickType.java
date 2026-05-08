package com.g4vrk.functionalMenu.click;

import org.jetbrains.annotations.NotNull;

public enum InventoryClickType {
    LEFT,
    SHIFT_LEFT,

    RIGHT,
    SHIFT_RIGHT,

    WINDOW_BORDER_LEFT,
    WINDOW_BORDER_RIGHT,

    MIDDLE,

    NUMBER_KEY,
    NUMBER_KEY_1(1),
    NUMBER_KEY_2(2),
    NUMBER_KEY_3(3),
    NUMBER_KEY_4(4),
    NUMBER_KEY_5(5),
    NUMBER_KEY_6(6),
    NUMBER_KEY_7(7),
    NUMBER_KEY_8(8),
    NUMBER_KEY_9(9),

    DOUBLE_CLICK,

    DROP,
    CONTROL_DROP,

    SWAP_OFFHAND,

    UNKNOWN,

    ANY_CLICK,

    ;

    private int hotbarKey = -1;

    InventoryClickType() {
    }

    InventoryClickType(int hotbarKey) {
        this.hotbarKey = hotbarKey;
    }

    public boolean matches(
            final @NotNull InventoryClickType clickType
    ) {
        return this == ANY_CLICK || this == clickType;
    }

    public boolean isNumberKey() {
        return this == NUMBER_KEY || hotbarKey != -1;
    }

    public static @NotNull InventoryClickType fromString(
            final @NotNull String clickStr
    ) {
        return switch (clickStr.toLowerCase()) {
            case "left_click" -> LEFT;
            case "shift_left_click" -> SHIFT_LEFT;

            case "right_click" -> RIGHT;
            case "shift_right_click" -> SHIFT_RIGHT;

            case "window_border_left_click" -> WINDOW_BORDER_LEFT;
            case "window_border_right_click" -> WINDOW_BORDER_RIGHT;

            case "middle_click" -> MIDDLE;

            case "number_key_click" -> NUMBER_KEY;
            case "number_key_1_click" -> NUMBER_KEY_1;
            case "number_key_2_click" -> NUMBER_KEY_2;
            case "number_key_3_click" -> NUMBER_KEY_3;
            case "number_key_4_click" -> NUMBER_KEY_4;
            case "number_key_5_click" -> NUMBER_KEY_5;
            case "number_key_6_click" -> NUMBER_KEY_6;
            case "number_key_7_click" -> NUMBER_KEY_7;
            case "number_key_8_click" -> NUMBER_KEY_8;
            case "number_key_9_click" -> NUMBER_KEY_9;

            case "double_click" -> DOUBLE_CLICK;

            case "drop" -> DROP;
            case "control_drop" -> CONTROL_DROP;

            case "swap_offhand" -> SWAP_OFFHAND;

            case "unknown_click" -> UNKNOWN;

            default -> ANY_CLICK;
        };
    }

    public static @NotNull InventoryClickType fromHotbarKey(int hotbarKey) {
        return switch (hotbarKey) {
            case 1 -> NUMBER_KEY_1;
            case 2 -> NUMBER_KEY_2;
            case 3 -> NUMBER_KEY_3;
            case 4 -> NUMBER_KEY_4;
            case 5 -> NUMBER_KEY_5;
            case 6 -> NUMBER_KEY_6;
            case 7 -> NUMBER_KEY_7;
            case 8 -> NUMBER_KEY_8;
            case 9 -> NUMBER_KEY_9;
            default -> NUMBER_KEY;
        };
    }
}
