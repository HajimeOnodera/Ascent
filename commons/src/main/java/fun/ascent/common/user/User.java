package fun.ascent.common.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.UUID;

import static fun.ascent.common.StringUtility.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID uuid;
    private String name;
    private Rank rank = Rank.DEFAULT;
    private int level = 1;
    private int achievementPoints = 0;

    public Component getDisplayName() {
        Component prefix = rank.getFormattedPrefix();
        TextColor nameColor = rank.getTextColor();
        
        return prefix.append(text("<" + nameColor.asHexString() + ">" + escapeMiniMessage(name)));
    }
}
