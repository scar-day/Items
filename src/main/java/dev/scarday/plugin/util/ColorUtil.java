package dev.scarday.plugin.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class ColorUtil {
    public static String colorize(@NotNull String text) {
        val miniMessage = MiniMessage.get();
        val deserialize = miniMessage.deserialize(text);
        return LegacyComponentSerializer.legacySection().serialize(deserialize);
    }

    public static String colorizeList(@NotNull List<String> list) {
        val messageString = String.join("\n", list);

        return colorize(messageString);
    }
}
