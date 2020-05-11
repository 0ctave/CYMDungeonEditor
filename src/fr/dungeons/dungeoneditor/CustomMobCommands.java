//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeoneditor;

import fr.dungeons.custommob.CustomMob;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomMobCommands {
    public static final Command cmd = Bukkit.getPluginCommand("custommob");

    public CustomMobCommands() {
    }

    public static boolean onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(cmd.getUsage());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            info(sender, args);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            list(sender, args);
        } else if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("create")) {
            create(sender, args);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
            edit(sender, args);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("spawn")) {
            spawn(sender, args);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("save")) {
            save(sender, args);
        }

        return true;
    }

    private static void info(CommandSender sender, String[] args) {
        CustomMob cm = null;

        try {
            cm = CustomMob.getByID(Integer.parseInt(args[1]));
        } catch (Exception var5) {
            sender.sendMessage(ChatColor.RED + "Not a number");
        }

        if (cm == null) {
            sender.sendMessage(ChatColor.RED + "CustomMob introuvable");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Reference ID: " + ChatColor.BLUE + cm.getReferenceID());
            sender.sendMessage(ChatColor.GREEN + "  Type: " + cm.getType());
            if (cm.getDisplayName() == null) {
                sender.sendMessage(ChatColor.GREEN + "  Display name: " + ChatColor.GRAY + "pas spécifié");
            } else {
                sender.sendMessage(ChatColor.GREEN + "  Display name: " + ChatColor.RESET + cm.getDisplayName());
            }

            if (cm.getMaxHealth() <= 0.0D) {
                sender.sendMessage(ChatColor.GREEN + "  Max health: " + ChatColor.GRAY + "pas spécifié");
            } else {
                sender.sendMessage(ChatColor.GREEN + "  Max healt: " + ChatColor.BLUE + cm.getMaxHealth());
            }

            if (cm.getHelmet() == null) {
                sender.sendMessage(ChatColor.GREEN + "  Helmet: " + ChatColor.GRAY + "sans");
            } else {
                sender.sendMessage(ChatColor.GREEN + "  Helmet: " + ChatColor.BLUE + cm.getHelmet().toString());
                sender.sendMessage(ChatColor.GREEN + "  Helmet drop chance: " + ChatColor.BLUE + cm.getHelmetDropChance() * 100.0F + "%");
            }

            if (cm.getChestplate() == null) {
                sender.sendMessage(ChatColor.GREEN + "  Chestplate: " + ChatColor.GRAY + "sans");
            } else {
                sender.sendMessage(ChatColor.GREEN + "  Chestplate: " + ChatColor.BLUE + cm.getChestplate().toString());
                sender.sendMessage(ChatColor.GREEN + "  Chestplate drop chance: " + ChatColor.BLUE + cm.getChestplateDropChance() * 100.0F + "%");
            }

            if (cm.getLeggings() == null) {
                sender.sendMessage(ChatColor.GREEN + "  Leggings: " + ChatColor.GRAY + "sans");
            } else {
                sender.sendMessage(ChatColor.GREEN + "  Leggings: " + ChatColor.BLUE + cm.getLeggings().toString());
                sender.sendMessage(ChatColor.GREEN + "  Leggings drop chance: " + ChatColor.BLUE + cm.getLeggingsDropChance() * 100.0F + "%");
            }

            if (cm.getBoots() == null) {
                sender.sendMessage(ChatColor.GREEN + "  Boots: " + ChatColor.GRAY + "sans");
            } else {
                sender.sendMessage(ChatColor.GREEN + "  Boots: " + ChatColor.BLUE + cm.getBoots().toString());
                sender.sendMessage(ChatColor.GREEN + "  Boots drop chance: " + ChatColor.BLUE + cm.getBootsDropChance() * 100.0F + "%");
            }

            if (cm.getHand() == null) {
                sender.sendMessage(ChatColor.GREEN + "  Hand: " + ChatColor.GRAY + "sans");
            } else {
                sender.sendMessage(ChatColor.GREEN + "  Hand: " + ChatColor.BLUE + cm.getHand().toString());
                sender.sendMessage(ChatColor.GREEN + "  Hand drop chance: " + ChatColor.BLUE + cm.getHandDropChance() * 100.0F + "%");
            }

            sender.sendMessage(ChatColor.GREEN + "  Items to loot:");
            Iterator var3 = cm.getItemsToDrop().entrySet().iterator();

            while(var3.hasNext()) {
                Entry<ItemStack, Float> e = (Entry)var3.next();
                sender.sendMessage(ChatColor.GREEN + "    -" + ChatColor.BLUE + ((ItemStack)e.getKey()).toString());
                sender.sendMessage(ChatColor.GRAY + "     Drop chance: " + ChatColor.BLUE + (Float)e.getValue() * 100.0F + "%");
            }
        }

    }

    private static void list(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            CustomMobEditor.openMobListe((Player)sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Vous devez etre un joueur pour faire ca");
        }

    }

    private static void create(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (sender instanceof Player) {
                CustomMobEditor.createCustomMob((Player)sender, args.length == 2 ? args[1] : null);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Vous devez etre un joueur pour faire ca");
        }

    }

    private static void edit(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            CustomMob cm = null;

            try {
                cm = CustomMob.getByID(Integer.parseInt(args[1]));
            } catch (Exception var4) {
                sender.sendMessage(ChatColor.RED + "Not a number");
            }

            if (cm == null) {
                sender.sendMessage(ChatColor.RED + "CustomMob introuvable");
            } else {
                CustomMobEditor.editCustomMob((Player)sender, cm);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Vous devez etre un joueur pour faire ca");
        }

    }

    private static void spawn(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            CustomMob cm = null;

            try {
                cm = CustomMob.getByID(Integer.parseInt(args[1]));
            } catch (Exception var4) {
                sender.sendMessage(ChatColor.RED + "Not a number");
            }

            if (cm == null) {
                sender.sendMessage(ChatColor.RED + "CustomMob introuvable");
            } else {
                cm.spawn(((Player)sender).getLocation());
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Vous devez etre un joueur pour faire ca");
        }

    }

    private static void save(CommandSender sender, String[] args) {
        CustomMob cm = null;

        try {
            cm = CustomMob.getByID(Integer.parseInt(args[1]));
        } catch (Exception var8) {
            sender.sendMessage(ChatColor.RED + "Not a number");
        }

        if (cm == null) {
            sender.sendMessage(ChatColor.RED + "CustomMob introuvable");
        } else {
            File customMobFolder = new File(DungeonEditor.getPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + "CustomMobs");
            if (!customMobFolder.exists()) {
                customMobFolder.mkdir();
            }

            File customMobFile = new File(customMobFolder.getAbsolutePath() + File.separatorChar + "CustomMob_" + cm.getReferenceID() + ".yml");
            YamlConfiguration yml = new YamlConfiguration();

            try {
                yml.loadFromString(cm.serialize());
                yml.save(customMobFile);
                sender.sendMessage(ChatColor.GREEN + "CustomMob ID " + cm.getReferenceID() + " sauvegardé !");
            } catch (InvalidConfigurationException | IOException var7) {
                var7.printStackTrace();
                sender.sendMessage(ChatColor.RED + "CustomMob non sauvegardé, vérifiez les logs");
            }

        }
    }
}
