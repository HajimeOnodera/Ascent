package fun.ascent.skyblock.entity.mob;

import fun.ascent.skyblock.entity.mob.impl.ZoneSpawner;
import lombok.Getter;
import net.minestom.server.entity.EntityType;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EntityRegistry {

    @Getter
    private static final List<EntityRegistry> entries = new ArrayList<>();

    private final Class<? extends SkyblockMobEntity> clazz;
    @Getter
    private final SkyblockMobEntity prototype;
    @Getter
    private final EntityType entityType;

    private EntityRegistry(Class<? extends SkyblockMobEntity> clazz) {
        this.clazz = clazz;
        this.prototype = instantiate();
        this.entityType = prototype.getEntityType();
    }

    public SkyblockMobEntity spawn() {
        return instantiate();
    }

    private SkyblockMobEntity instantiate() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate mob: " + clazz.getSimpleName(), e.getCause());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate mob: " + clazz.getSimpleName(), e);
        }
    }

    public boolean isZoneSpawner() {
        return prototype instanceof ZoneSpawner;
    }

    public static void scanAndRegister(String packageName) {
        Reflections scanner = new Reflections(packageName);
        Set<Class<? extends SkyblockMobEntity>> found = scanner.getSubTypesOf(SkyblockMobEntity.class);

        for (Class<? extends SkyblockMobEntity> mobClass : found) {
            if (mobClass.isInterface() || mobClass.isAnonymousClass()) continue;
            if (java.lang.reflect.Modifier.isAbstract(mobClass.getModifiers())) continue;
            register(mobClass);
        }
    }

    public static void register(Class<? extends SkyblockMobEntity> clazz) {
        entries.add(new EntityRegistry(clazz));
    }

    public static EntityRegistry getByClass(Class<? extends SkyblockMobEntity> clazz) {
        return entries.stream()
                .filter(e -> e.clazz == clazz)
                .findFirst()
                .orElse(null);
    }

    public static EntityRegistry getByMob(SkyblockMobEntity mob) {
        return getByClass(mob.getClass());
    }

    public static List<EntityRegistry> getZoneSpawners() {
        return entries.stream()
                .filter(EntityRegistry::isZoneSpawner)
                .toList();
    }
}
