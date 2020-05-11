//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeoneditor;

import fr.dungeons.custommob.CustomMob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomMobEditor {
    private static HashMap<String, Inventory> currentEditing = new HashMap();
    private static HashMap<Inventory, Integer> currentEditingId = new HashMap();

    public CustomMobEditor() {
    }

    public static void editCustomMob(Player p, CustomMob cm) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLUE + "CustomMobEditor " + ChatColor.RESET + " ID: " + cm.getReferenceID());
        currentEditing.put(p.getName(), inv);
        currentEditingId.put(inv, cm.getReferenceID());
        ItemStack is;
        ItemMeta im;
        if (cm.getHelmet() == null) {
            is = new ItemStack(Material.BARRIER);
            im = is.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + "Helmet");
            im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera l'helmet", ChatColor.GRAY + "de l'entite"));
            is.setItemMeta(im);
        } else {
            is = cm.getHelmet();
        }

        inv.setItem(0, is);
        if (cm.getChestplate() == null) {
            is = new ItemStack(Material.BARRIER);
            im = is.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + "Chestplate");
            im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera le chestplate", ChatColor.GRAY + "de l'entite"));
            is.setItemMeta(im);
        } else {
            is = cm.getChestplate();
        }

        inv.setItem(1, is);
        if (cm.getLeggings() == null) {
            is = new ItemStack(Material.BARRIER);
            im = is.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + "Leggings");
            im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera le leggings", ChatColor.GRAY + "de l'entite"));
            is.setItemMeta(im);
        } else {
            is = cm.getLeggings();
        }

        inv.setItem(2, is);
        if (cm.getBoots() == null) {
            is = new ItemStack(Material.BARRIER);
            im = is.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + "Boots");
            im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera les boots", ChatColor.GRAY + "de l'entite"));
            is.setItemMeta(im);
        } else {
            is = cm.getBoots();
        }

        inv.setItem(3, is);
        if (cm.getHand() == null) {
            is = new ItemStack(Material.BARRIER);
            im = is.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + "Hand");
            im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera la main", ChatColor.GRAY + "de l'entite"));
            is.setItemMeta(im);
        } else {
            is = cm.getHand();
        }

        inv.setItem(4, is);
        is = new ItemStack(Material.LEGACY_SKULL);
        im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Type");
        im.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.BLUE + cm.getType().name()));
        is.setItemMeta(im);
        inv.setItem(5, is);
        is = new ItemStack(Material.CHEST);
        im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Loots");
        List<String> toDropListe = new ArrayList();
        Iterator var6 = cm.getItemsToDrop().entrySet().iterator();

        while(var6.hasNext()) {
            Entry<ItemStack, Float> e = (Entry)var6.next();
            toDropListe.add(ChatColor.DARK_PURPLE + e.getKey().getType().name() + " x" + e.getKey().getAmount() + ChatColor.GRAY + " (" + e.getValue() * 100.0F + "%)");
        }

        im.setLore(toDropListe);
        is.setItemMeta(im);
        inv.setItem(6, is);
        is = new ItemStack(Material.CACTUS);
        im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Sauvegarder");
        im.setLore(Arrays.asList(ChatColor.GRAY + "Display name:", cm.getDisplayName() == null ? "pas spécifié" : cm.getDisplayName()));
        is.setItemMeta(im);
        inv.setItem(8, is);
        p.openInventory(inv);
    }

    public static void createCustomMob(Player p, String mobName) {
        CustomMob cm = new CustomMob(CustomMob.generateID());
        if (mobName != null) {
            cm.setDisplayName(mobName);
        }

        editCustomMob(p, cm);
    }

    public static void openMobType(Player p) {
        int size = (int)Math.floor(EntityType.values().length / 9) * 9 + (EntityType.values().length % 9 == 0 ? 0 : 9);
        Inventory inv = Bukkit.createInventory(null, size, ChatColor.GRAY + "CreatureType");
        int slot = 0;
        EntityType[] var4 = EntityType.values();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            EntityType ct = var4[var6];
            ItemStack is = new ItemStack(Material.LEGACY_SKULL);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ct.name());
            is.setItemMeta(im);
            inv.setItem(slot, is);
            ++slot;
        }

        p.openInventory(inv);
    }

    public static void openMobListe(Player p) {
        int max = 0;
        Iterator var2 = CustomMob.getLoadedCustomMobs().keySet().iterator();

        while(var2.hasNext()) {
            int id = (Integer)var2.next();
            if (id > max) {
                max = id;
            }
        }

        Inventory mobListe = Bukkit.createInventory(null, max / 9 + (max % 9 == 0 ? 0 : 9), ChatColor.BLUE + "CustomMob list");
        Iterator var10 = CustomMob.getLoadedCustomMobs().keySet().iterator();

        while(var10.hasNext()) {
            int id = (Integer)var10.next();
            CustomMob cm = CustomMob.getLoadedCustomMobs().get(id);
            ItemStack item = new ItemStack(Material.LEGACY_SKULL);
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.BLUE + "ReferenceID: " + ChatColor.GOLD + id);
            List<String> lore = new ArrayList();
            if (cm.getDisplayName() == null) {
                lore.add(ChatColor.GRAY + "Display name: " + ChatColor.GOLD + "Undefined");
            } else {
                lore.add(ChatColor.GRAY + "Display name: " + ChatColor.GOLD + cm.getDisplayName());
            }

            lore.add(ChatColor.GRAY + "Type: " + cm.getType().name());
            im.setLore(lore);
            item.setItemMeta(im);
            mobListe.setItem(id, item);
        }

        p.openInventory(mobListe);
    }

    public static HashMap<String, Inventory> getCurrentEditing() {
        return currentEditing;
    }

    public static CustomMob getCurrentMob(String playerName) {
        return CustomMob.getByID(currentEditingId.get(currentEditing.get(playerName)) + 4);
    }

    public static CustomMob getCurrentMob(Player p) {
        return getCurrentMob(p.getName());
    }

    public static void openLootChest(Player p) {
        CustomMob cm = getCurrentMob(p);
        if (cm != null) {
            p.openInventory(cm.getInventory());
        }

    }
}