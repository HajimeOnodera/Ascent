package fun.ascent.common.user;

import fun.ascent.database.MongoProvider;
import fun.ascent.database.PlayerProfile;
import fun.ascent.database.PlayerRepository;

import java.util.UUID;

/**
 * Bridge between the old User system and the new centralized database module.
 * Delegates to {@link PlayerRepository} and {@link PlayerProfile} for storage.
 */
public class UserDatabase {

    public static void connect(String uri) {
        if (MongoProvider.isInitialized()) {
            MongoProvider.connect(uri);
        }
    }

    public static User loadUser(UUID uuid) {
        PlayerProfile profile = PlayerProfile.load(uuid);
        if (profile == null) return null;

        User user = new User();
        user.setUuid(uuid);
        user.setName(profile.getName());

        try {
            user.setRank(Rank.valueOf(profile.getRank()));
        } catch (IllegalArgumentException e) {
            user.setRank(Rank.DEFAULT);
        }

        return user;
    }

    public static void saveUser(User user) {
        PlayerProfile profile = PlayerProfile.load(user.getUuid());
        if (profile == null) {
            profile = new PlayerProfile(
                    user.getUuid(),
                    user.getName(),
                    user.getRank().name(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
            );
        } else {
            profile.setName(user.getName());
            profile.setRank(user.getRank().name());
            profile.setLastSeen(System.currentTimeMillis());
        }
        profile.save();
    }
}
