package dev.scarday.plugin.config;

import dev.scarday.plugin.item.Type;
import eu.okaeri.configs.OkaeriConfig;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class Configuration extends OkaeriConfig {
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    public static class Messages extends OkaeriConfig {
        String itemNotFound = "<red>Предмет не найден";
        String playerNotFound = "<red>Игрок не найден";

        String missingPlayer = "<red>Вы не указали ник игрока";
        String missingItemName = "<red>Вы не указали название предмета";

        String giveItem = "<green>Предмет успешно выдан!";

        String reloadConfig = "<green>Конфиг успешно перезагружен!";
        String noPermission = "<red>У вас недостаточно прав!";

        List<String> commands = List.of(" ",
                " /items give <player> <item-name>",
                " /items reload", "");
    }

    Messages messages = new Messages();

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Getter
    public static class Items extends OkaeriConfig {
        String name;
        List<String> lore;

        Type type;

        String material;

        List<String> enchantments;
        List<String> effects; // no work!

        List<String> flags;
    }

    Map<String, Items> items = itemsMap();

    private Map<String, Items> itemsMap() {
        val map = new HashMap<String, Items>();

        map.put("testItem", new Items(
                "<red>Тест",
                List.of("",
                        "<white>Это тестовый",
                        "<red>lore",
                        ""),
                Type.DEFAULT,
                "NETHERITE_SWORD",
                List.of("ALL_DAMAGE;5"),
                List.of("HASTE;3"),
                List.of("HIDE_ENCHANTS")
        ));

        map.put("runa1", new Items(
                "<red>Тестовая руна",
                List.of("",
                        "<white>Это тестовая",
                        "<red>руна",
                        ""),
                Type.RUNE,
                "base-head=eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFjOTY3NTBmNTM3ZTU4YTU5ODgwNDJhNjIyZGMxZmNiZWJiYzVjYmJhZjQ5MzU3ZGFjYjc2NGIzYjkxNiJ9fX0=",
                List.of(""),
                List.of("JUMP;3"),
                List.of("")
        ));

        return map;
    }
}
