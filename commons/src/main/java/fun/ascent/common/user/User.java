package fun.ascent.common.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID uuid;
    private String name;
    private Rank rank = Rank.DEFAULT;

    public Component getDisplayName() {
        Component prefix = rank.getFormattedPrefix();
        TextColor nameColor = rank.getColor();
        
        if (rank == Rank.DEFAULT) {
            return Component.text(name).color(NamedTextColor.GRAY);
        }
        
        return prefix.append(Component.text(name).color(nameColor));
    }

    public String getLegacyDisplayName() {
        return LegacyComponentSerializer.legacySection().serialize(getDisplayName());
    }
}
