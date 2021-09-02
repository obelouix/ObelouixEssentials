package fr.obelouix.essentials.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class TextColorFormatter {

    /**
     * Convert hex color codes and < 1.16 codes into components color codes
     *
     * @param component the component to colorize
     * @return a colorized component
     */
    public static @NotNull TextComponent colorFormatter(Component component) {
        @NotNull String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder()
                .hexColors()
                .character('&')
                .hexCharacter('#')
                .build();

        return serializer.deserialize(plainText);
    }
}
