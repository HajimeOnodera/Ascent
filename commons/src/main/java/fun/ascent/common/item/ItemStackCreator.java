package fun.ascent.common.item;

import fun.ascent.common.StringUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.network.player.ResolvableProfile;
import net.minestom.server.tag.Tag;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

import static fun.ascent.common.StringUtility.*;
import static net.minestom.server.component.DataComponents.*;

public class ItemStackCreator {
    private static final TooltipDisplay DEFAULT_TOOLTIP_DISPLAY = new TooltipDisplay(false, Set.of(
            UNBREAKABLE
    ));
    /**
     * Creates an {@link ItemStack.Builder} with a specified material and custom name.
     *
     * @param material the material of the item stack
     * @param name     the custom name of the item stack
     * @return an {@link ItemStack.Builder} with the specified properties
     */
    public static ItemStack.Builder createNamedItemStack(Material material, String name) {
        return clearAttributes(ItemStack.builder(material)
                .set(CUSTOM_NAME, text(name))
                .set(TOOLTIP_DISPLAY, DEFAULT_TOOLTIP_DISPLAY));
    }

    /**
     * Creates an {@link ItemStack.Builder} with a specified material and custom name.
     *
     * @param material the material of the item stack
     * @param name     the custom name of the item stack
     * @return an {@link ItemStack.Builder} with the specified properties
     */
    public static ItemStack.Builder createNamedItemStack(Material material, Component name) {
        return clearAttributes(ItemStack.builder(material)
                .set(CUSTOM_NAME, name.decoration(TextDecoration.ITALIC, false))
                .set(TOOLTIP_DISPLAY, DEFAULT_TOOLTIP_DISPLAY));
    }

    /**
     * Clears attribute modifiers from the given {@link ItemStack.Builder}.
     *
     * @param builder the {@link ItemStack.Builder} to clear attributes from
     * @return the modified {@link ItemStack.Builder}
     */
    public static ItemStack.Builder clearAttributes(ItemStack.Builder builder) {
        builder.set(ATTRIBUTE_MODIFIERS, new AttributeList(List.of()));
        builder.set(TOOLTIP_DISPLAY, DEFAULT_TOOLTIP_DISPLAY);
        return builder;
    }

    /**
     * Creates an {@link ItemStack.Builder} with a specified material and an empty custom name.
     *
     * @param material the material of the item stack
     * @return an {@link ItemStack.Builder} with the specified material and an empty name
     */
    public static ItemStack.Builder createNamedItemStack(Material material) {
        return createNamedItemStack(material, "");
    }

    /**
     * Creates an {@link ItemStack.Builder} with a single lore line.
     *
     * @param name     the name of the item stack
     * @param color    the color to apply to the lore
     * @param material the material of the item stack
     * @param amount   the amount of items in the stack
     * @param lore     the lore to display
     * @return an {@link ItemStack.Builder} with the specified properties
     */
    public static ItemStack.Builder getSingleLoreStack(String name, String color, Material material, int amount, String lore) {
        List<String> l = new ArrayList<>();
        for (String line : splitByWordAndLength(lore, 30)) {
            l.add(color + line);
        }
        return getStack(name, material, amount, l.toArray(new String[]{}));
    }

    /**
     * Creates an {@link ItemStack.Builder} with a single lore line. Will split the lore into multiple lines on every \n.
     *
     * @param name     the name of the item stack
     * @param color    the color to apply to the lore
     * @param material the material of the item stack
     * @param amount   the number of items in the stack
     * @param lore     the lore to display
     * @return an {@link ItemStack.Builder} with the specified properties
     */
    public static ItemStack.Builder getSingleLoreStackLineSplit(String name, String color, Material material, int amount, String lore) {
        List<String> l = new ArrayList<>();
        for (String line : lore.split("\n")) {
            l.add(color + line);
        }
        return getStack(name, material, amount, l.toArray(new String[]{}));
    }

    /**
     * Creates an {@link ItemStack.Builder} with specified name, material, amount, and lore.
     *
     * @param name     the name of the item stack
     * @param material the material of the item stack
     * @param amount   the amount of items in the stack
     * @param lore     the lore of the item stack
     * @return an {@link ItemStack.Builder} with the specified properties
     */
    public static ItemStack.Builder getStack(String name, Material material, int amount, String... lore) {
        return getStack(name, material, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder getStack(String name, Material material, int amount) {
        return getStack(name, material, amount, new String[0]);
    }

    public static ItemStack.Builder getStack(String name, Material material, int amount, Component... lore) {
        return getStack(text(name), material, amount, Arrays.asList(lore));
    }

    /**
     * Creates an {@link ItemStack.Builder} with specified name, material, amount, and lore.
     *
     * @param name     the name of the item stack
     * @param material the material of the item stack
     * @param amount   the amount of items in the stack
     * @param lore     the lore of the item stack
     * @return an {@link ItemStack.Builder} with the specified properties
     */
    public static ItemStack.Builder getStack(Component name, Material material, int amount, Component... lore) {
        return getStack(name, material, amount, Arrays.asList(lore));
    }

    /**
     * Updates the lore of the given {@link ItemStack.Builder} with the specified lore lines.
     *
     * @param builder the {@link ItemStack.Builder} to update
     * @param lore    the new lore lines to set
     * @return the updated {@link ItemStack.Builder}
     */
    public static ItemStack.Builder updateLore(ItemStack.Builder builder, List<String> lore) {
        List<String> copiedLore = new ArrayList<>();
        for (String s : lore) {
            copiedLore.add(replaceColorCodes(s));
        }

        return clearAttributes(builder.set(LORE, copiedLore.stream()
                        .map(StringUtility::text)
                        .collect(Collectors.toList()))
                .set(TOOLTIP_DISPLAY, DEFAULT_TOOLTIP_DISPLAY));
    }

    /**
     * Appends lore lines to the existing lore of the given {@link ItemStack.Builder}.
     *
     * @param builder the {@link ItemStack.Builder} to modify
     * @param lore    the lore lines to append
     * @return the modified {@link ItemStack.Builder}
     */
    public static ItemStack.Builder appendLore(ItemStack.Builder builder, List<String> lore) {
        List<Component> existingLore = new ArrayList<>(Objects.requireNonNull(builder.build().get(LORE)));
        for (String s : lore) {
            existingLore.add(text(s));
        }

        return clearAttributes(builder.set(LORE, existingLore)
                .set(TOOLTIP_DISPLAY, DEFAULT_TOOLTIP_DISPLAY));
    }

    /**
     * Marks the given {@link ItemStack.Builder} as not editable.
     *
     * @param builder the {@link ItemStack.Builder} to modify
     * @return the modified {@link ItemStack.Builder}
     */
    public static ItemStack.Builder setNotEditable(ItemStack.Builder builder) {
        return builder.set(Tag.Boolean("uneditable"), true);
    }

    /**
     * Applies an enchantment glint to the given {@link ItemStack.Builder}.
     *
     * @param builder the {@link ItemStack.Builder} to modify
     * @return the modified {@link ItemStack.Builder}
     */
    public static ItemStack.Builder enchant(ItemStack.Builder builder) {
        return clearAttributes(builder.set(ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(TOOLTIP_DISPLAY, DEFAULT_TOOLTIP_DISPLAY));
    }

    /**
     * Creates an {@link ItemStack.Builder} from an existing {@link ItemStack}.
     *
     * @param stack the original {@link ItemStack} to create a builder from
     * @return an {@link ItemStack.Builder} with the properties of the original stack
     */
    public static ItemStack.Builder getFromStack(ItemStack stack) {
        ItemStack.Builder builder = ItemStack.builder(stack.material())
                .amount(stack.amount());
        
        // Copy components safely
        if (stack.has(CUSTOM_NAME)) builder.set(CUSTOM_NAME, Objects.requireNonNull(stack.get(CUSTOM_NAME)));
        if (stack.has(LORE)) builder.set(LORE, Objects.requireNonNull(stack.get(LORE)));
        if (stack.has(PROFILE)) builder.set(PROFILE, Objects.requireNonNull(stack.get(PROFILE)));
        if (stack.has(CUSTOM_DATA)) builder.set(CUSTOM_DATA, Objects.requireNonNull(stack.get(CUSTOM_DATA)));
        if (stack.has(ENCHANTMENTS)) builder.set(ENCHANTMENTS, Objects.requireNonNull(stack.get(ENCHANTMENTS)));
        if (stack.has(ATTRIBUTE_MODIFIERS)) builder.set(ATTRIBUTE_MODIFIERS, Objects.requireNonNull(stack.get(ATTRIBUTE_MODIFIERS)));
        if (stack.has(TOOLTIP_DISPLAY)) builder.set(TOOLTIP_DISPLAY, Objects.requireNonNull(stack.get(TOOLTIP_DISPLAY)));
        if (stack.has(DYED_COLOR)) builder.set(DYED_COLOR, Objects.requireNonNull(stack.get(DYED_COLOR)));
        if (stack.has(POTION_CONTENTS)) builder.set(POTION_CONTENTS, Objects.requireNonNull(stack.get(POTION_CONTENTS)));
        if (stack.has(UNBREAKABLE)) builder.set(UNBREAKABLE, Objects.requireNonNull(stack.get(UNBREAKABLE)));
        
        return builder;
    }

    /**
     * Creates an {@link ItemStack.Builder} with the specified name, material, amount, and lore list.
     *
     * @param name     the name of the item stack
     * @param material the material of the item stack
     * @param amount   the amount of items in the stack
     * @param lore     the list of lore lines for the item stack
     * @return an {@link ItemStack.Builder} with the specified properties
     */
    public static ItemStack.Builder getStack(String name, Material material, int amount, List<?> lore) {
        return getStack(text(name), material, amount, literalLoreComponents(lore));
    }

    /**
     * Creates an {@link ItemStack.Builder} with the specified name, material, amount, and lore list.
     *
     * @param name     the name of the item stack
     * @param material the material of the item stack
     * @param amount   the amount of items in the stack
     * @param lore     the list of lore lines for the item stack
     * @return an {@link ItemStack.Builder} with the specified properties
     */
    public static ItemStack.Builder getStack(Component name, Material material, int amount, List<Component> lore) {
        List<Component> copiedLore = new ArrayList<>(lore);

        return clearAttributes(ItemStack.builder(material).amount(amount).set(LORE, copiedLore.stream()
                        .map(line -> line.decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList()))
                .set(CUSTOM_NAME, name.decoration(TextDecoration.ITALIC, false))
                .set(TOOLTIP_DISPLAY, DEFAULT_TOOLTIP_DISPLAY));
    }

    /**
     * Creates an {@link ItemStack.Builder} for a player head with the specified properties.
     *
     * @param name    the name of the item stack
     * @param texture the texture URL of the player skin
     * @param amount  the amount of items in the stack
     * @param lore    the lore of the item stack
     * @return an {@link ItemStack.Builder} for a player head with the specified properties
     */
    public static ItemStack.Builder getStackHead(String name, String texture, int amount, String... lore) {
        return getStackHead(name, texture, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder getStackHead(String name, String texture, int amount) {
        return getStackHead(name, texture, amount, new String[0]);
    }

    public static ItemStack.Builder getStackHead(String name, String texture, int amount, Component... lore) {
        return getStackHead(text(name), texture, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder getStackHead(Component name, String texture, int amount, Component... lore) {
        return getStackHead(name, texture, amount, Arrays.asList(lore));
    }

    /**
     * Creates an {@link ItemStack.Builder} for a player head with the specified name and texture.
     *
     * @param name    the name of the item stack
     * @param texture the texture URL of the player skin
     * @return an {@link ItemStack.Builder} for a player head
     */
    public static ItemStack.Builder getStackHead(String name, String texture) {
        return getStackHead(name, texture, 1, new ArrayList<>());
    }

    /**
     * Creates an {@link ItemStack.Builder} for a player head with the specified texture.
     *
     * @param texture the texture URL of the player skin
     * @return an {@link ItemStack.Builder} for a player head
     */
    public static ItemStack.Builder getStackHead(String texture) {
        return getStackHead("", texture, 1, new ArrayList<>());
    }

    /**
     * Creates an {@link ItemStack.Builder} for a player head using a {@link PlayerSkin}.
     *
     * @param name   the name of the item stack
     * @param skin   the {@link PlayerSkin} to use for the head
     * @param amount the amount of items in the stack
     * @param lore   the lore of the item stack
     * @return an {@link ItemStack.Builder} for a player head
     */
    public static ItemStack.Builder getStackHead(String name, PlayerSkin skin, int amount, String... lore) {
        return getStackHead(name, skin, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder getStackHead(String name, PlayerSkin skin, int amount) {
        return getStackHead(name, skin, amount, new String[0]);
    }

    public static ItemStack.Builder getStackHead(String name, PlayerSkin skin, int amount, Component... lore) {
        return getStackHead(text(name), skin, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder getStackHead(Component name, PlayerSkin skin, int amount, Component... lore) {
        return getStackHead(name, skin, amount, Arrays.asList(lore));
    }

    /**
     * Creates an {@link ItemStack.Builder} for a player head with a specified name, texture, amount, and lore list.
     *
     * @param name    the name of the item stack
     * @param texture the texture URL of the player skin
     * @param amount  the amount of items in the stack
     * @param lore    the list of lore lines for the item stack
     * @return an {@link ItemStack.Builder} for a player head with the specified properties
     */
    public static ItemStack.Builder getStackHead(String name, String texture, int amount, List<?> lore) {
        JSONObject json = new JSONObject();
        json.put("isPublic", true);
        json.put("signatureRequired", false);
        json.put("textures", new JSONObject().put("SKIN",
                new JSONObject().put("url", "http://textures.minecraft.net/texture/" + texture).put("metadata", new JSONObject().put("model", "slim"))));

        String texturesEncoded = Base64.getEncoder().encodeToString(json.toString().getBytes());

        return ItemStack.builder(Material.PLAYER_HEAD)
                .set(LORE, literalLoreComponents(lore).stream()
                        .map(line -> line.decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList()))
                .set(CUSTOM_NAME, text(name))
                .set(TOOLTIP_DISPLAY, DEFAULT_TOOLTIP_DISPLAY)
                .set(PROFILE, new ResolvableProfile(new PlayerSkin(texturesEncoded, null)))
                .amount(amount);
    }

    public static ItemStack.Builder getStackHead(Component name, String texture, int amount, List<Component> lore) {
        JSONObject json = new JSONObject();
        json.put("isPublic", true);
        json.put("signatureRequired", false);
        json.put("textures", new JSONObject().put("SKIN",
                new JSONObject().put("url", "http://textures.minecraft.net/texture/" + texture).put("metadata", new JSONObject().put("model", "slim"))));

        String texturesEncoded = Base64.getEncoder().encodeToString(json.toString().getBytes());

        return ItemStack.builder(Material.PLAYER_HEAD)
                .set(LORE, new ArrayList<>(lore).stream()
                        .map(line -> line.decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList()))
                .set(CUSTOM_NAME, name.decoration(TextDecoration.ITALIC, false))
                .set(TOOLTIP_DISPLAY, DEFAULT_TOOLTIP_DISPLAY)
                .set(PROFILE, new ResolvableProfile(new PlayerSkin(texturesEncoded, null)))
                .amount(amount);
    }

    /**
     * Creates an {@link ItemStack.Builder} for a player head with a specified name, {@link PlayerSkin}, amount, and lore list.
     *
     * @param name   the name of the item stack
     * @param skin   the {@link PlayerSkin} to use for the head
     * @param amount the amount of items in the stack
     * @param lore   the list of lore lines for the item stack
     * @return an {@link ItemStack.Builder} for a player head with the specified properties
     */
    public static ItemStack.Builder getStackHead(String name, PlayerSkin skin, int amount, List<?> lore) {
        return clearAttributes(ItemStack.builder(Material.PLAYER_HEAD)
                .set(LORE, literalLoreComponents(lore).stream()
                        .map(line -> line.decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList()))
                .set(CUSTOM_NAME, text(name))
                .set(TOOLTIP_DISPLAY, new TooltipDisplay(true, Set.of(
                        CONSUMABLE,
                        DAMAGE,
                        BASE_COLOR,
                        UNBREAKABLE
                )))
                .set(PROFILE, new ResolvableProfile(skin))
                .amount(amount));
    }

    public static ItemStack.Builder getStackHead(Component name, PlayerSkin skin, int amount, List<Component> lore) {
        return clearAttributes(ItemStack.builder(Material.PLAYER_HEAD)
                .set(LORE, new ArrayList<>(lore).stream()
                        .map(line -> line.decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList()))
                .set(CUSTOM_NAME, name.decoration(TextDecoration.ITALIC, false))
                .set(TOOLTIP_DISPLAY, new TooltipDisplay(true, Set.of(
                        CONSUMABLE,
                        DAMAGE,
                        BASE_COLOR,
                        UNBREAKABLE
                )))
                .set(PROFILE, new ResolvableProfile(skin))
                .amount(amount));
    }

    public static ItemStack.Builder getUsingGUIMaterial(String name, GUIMaterial material, int amount, List<?> lore) {
        if (material.hasTexture()) {
            return ItemStackCreator.getStackHead(name, material.texture(), amount, lore);
        } else {
            return ItemStackCreator.getStack(name, material.material(), amount, lore);
        }
    }

    public static ItemStack.Builder getUsingGUIMaterial(String name, GUIMaterial material, int amount, String... lore) {
        return getUsingGUIMaterial(name, material, amount, Arrays.asList(lore));
    }

    /**
     * Replaces color codes in the given string with Minecraft color codes.
     *
     * @param string the input string with color codes
     * @return the string with color codes replaced
     */
    public static String replaceColorCodes(String string) {
        return toMiniMessage(string);
    }

    public static List<Component> literalLoreComponents(List<?> lore) {
        List<Component> loreComponents = new ArrayList<>();
        for (Object line : lore) {
            if (line == null) {
                continue;
            }

            if (line instanceof Component component) {
                loreComponents.add(component);
                continue;
            }

            loreComponents.add(text(String.valueOf(line)));
        }
        return loreComponents;
    }
}

