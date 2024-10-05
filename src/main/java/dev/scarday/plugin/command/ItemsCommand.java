package dev.scarday.plugin.command;

import com.destroystokyo.paper.profile.ProfileProperty;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.scarday.plugin.Items;
import dev.scarday.plugin.annotation.OnlyPlayer;
import dev.scarday.plugin.config.Configuration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.UUID;

import static dev.scarday.plugin.util.ColorUtil.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Command(name = "items")
@Permission("items.use")
public class ItemsCommand {
    Items instance;

    @Async
    @Execute(name = "give")
    void giveSubCommand(@OnlyPlayer @Context CommandSender sender,
                        @Arg Player player,
                        @OptionalArg("itemName") String name
    ) {
        val messages = instance.getPluginConfig()
                .getMessages();

        if (name == null) {
            player.sendMessage(colorize(messages.getMissingItemName()));
            return;
        }

        val items = instance.getPluginConfig()
                .getItems();

        if (items.isEmpty()) {
            player.sendMessage(colorize("Предметы не зарегистрированы!"));
            return;
        }

        if (!items.containsKey(name)) {
            player.sendMessage(colorize(messages.getItemNotFound()));
            return;
        }

        val item = items.get(name);
        val itemBuilder = itemStack(name, item);

        val p = (Player) sender;

        p.getInventory().addItem(itemBuilder);
        p.sendMessage(colorize(messages.getGiveItem()));
    }

    @Async
    @Execute(name = "reload")
    void reloadSubCommand(@Context CommandSender player) {
        player.sendMessage(colorize(instance.getPluginConfig().getMessages().getReloadConfig()));
        instance.reloadConfig();
    }

    @SuppressWarnings("deprecation")
    private ItemStack itemStack(String name, Configuration.Items item) {
        val material = materialBuild(item);

        val lore = colorizeList(item.getLore());

        material.editMeta(itemMeta -> {
            itemMeta.displayName(Component.text(colorize(item.getName())));
            itemMeta.setLore(Arrays.asList(lore.split("\n")));

            val enchantments = item.getEnchantments();

            for (val enchant : enchantments) {
                val enchantParts = enchant.split(";");
                val enchantName = Enchantment.getByName(enchantParts[0]);

                if (enchantName != null) {
                    itemMeta.addEnchant(enchantName, Integer.parseInt(enchantParts[1]), true);
                }
            }

            val flags = item.getFlags();

            for (val attribute : flags) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(attribute));
                } catch (Exception ignored) {}
            }

            val key = new NamespacedKey(instance, "item");

            itemMeta.getPersistentDataContainer()
                    .set(key, PersistentDataType.STRING, name);
        });

        return material;
    }

    private ItemStack materialBuild(Configuration.Items item) {
        val material = item.getMaterial();
        String baseHead = "base-head=";
        if (material.startsWith(baseHead)) {
            val stack = new ItemStack(Material.PLAYER_HEAD, 1);
            val meta = (SkullMeta) stack.getItemMeta();
            val texture = material.substring(baseHead.length());
            if (meta != null) {
                val profile = Bukkit.createProfile(UUID.randomUUID());
                profile.setProperty(new ProfileProperty("textures", texture));
                meta.setPlayerProfile(profile);
                stack.setItemMeta(meta);
            }
            return stack;
        }
        return new ItemStack(Material.valueOf(material), 1);
    }
}
