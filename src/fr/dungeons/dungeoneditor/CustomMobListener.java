//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeoneditor;

import fr.dungeons.Dungeons;
import fr.dungeons.custommob.CustomMob;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CustomMobListener implements Listener {
    public CustomMobListener() {
    }

    @EventHandler
    public void customMob_edit(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            Player p = (Player)event.getWhoClicked();
            if (CustomMobEditor.getCurrentEditing().containsKey(p.getName())) {
                if (((Inventory)CustomMobEditor.getCurrentEditing().get(p.getName())).getViewers().contains(p)) {
                    Inventory inv = (Inventory)CustomMobEditor.getCurrentEditing().get(p.getName());
                    CustomMob cm = CustomMobEditor.getCurrentMob(p);
                    boolean clickedInInv = event.getClickedInventory().equals(inv);
                    if (!Arrays.asList(InventoryAction.PLACE_ALL, InventoryAction.PLACE_ONE, InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME, InventoryAction.SWAP_WITH_CURSOR).contains(event.getAction()) || clickedInInv) {
                        event.setCancelled(true);
                    }

                    if (clickedInInv) {
                        ItemStack cursor;
                        if (event.getCursor().getType() != Material.AIR) {
                            cursor = event.getCursor().clone();
                            cursor.setAmount(1);
                            switch(event.getSlot()) {
                                case 0:
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setHelmet(cursor);
                                    break;
                                case 1:
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setChestplate(cursor);
                                    break;
                                case 2:
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setLeggings(cursor);
                                    break;
                                case 3:
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setBoots(cursor);
                                    break;
                                case 4:
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setHand(cursor);
                            }
                        } else {
                            cursor = new ItemStack(Material.BARRIER);
                            ItemMeta im = cursor.getItemMeta();
                            switch(event.getSlot()) {
                                case 0:
                                    im.setDisplayName(ChatColor.GREEN + "Helmet");
                                    im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera l'helmet", ChatColor.GRAY + "de l'entite"));
                                    cursor.setItemMeta(im);
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setHelmet((ItemStack)null);
                                    break;
                                case 1:
                                    im.setDisplayName(ChatColor.GREEN + "Chestplate");
                                    im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera le chestplate", ChatColor.GRAY + "de l'entite"));
                                    cursor.setItemMeta(im);
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setChestplate((ItemStack)null);
                                    break;
                                case 2:
                                    im.setDisplayName(ChatColor.GREEN + "Leggings");
                                    im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera le leggings", ChatColor.GRAY + "de l'entite"));
                                    cursor.setItemMeta(im);
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setLeggings((ItemStack)null);
                                    break;
                                case 3:
                                    im.setDisplayName(ChatColor.GREEN + "Boots");
                                    im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera les boots", ChatColor.GRAY + "de l'entite"));
                                    cursor.setItemMeta(im);
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setBoots((ItemStack)null);
                                    break;
                                case 4:
                                    im.setDisplayName(ChatColor.GREEN + "Hand");
                                    im.setLore(Arrays.asList(ChatColor.GRAY + "Placez ici l'item", ChatColor.GRAY + "qui sera la main", ChatColor.GRAY + "de l'entite"));
                                    cursor.setItemMeta(im);
                                    inv.setItem(event.getSlot(), cursor);
                                    cm.setHand((ItemStack)null);
                            }
                        }

                        switch(event.getSlot()) {
                            case 5:
                                CustomMobEditor.openMobType(p);
                                break;
                            case 6:
                                CustomMobEditor.openLootChest(p);
                            case 7:
                            default:
                                break;
                            case 8:
                                CustomMobEditor.getCurrentEditing().remove(p.getName());
                                p.performCommand("custommob save " + cm.getReferenceID());
                                p.closeInventory();
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void creatureType(final InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getView().getTitle().equals(ChatColor.GRAY + "CreatureType")) {
                if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                    if (CustomMobEditor.getCurrentMob(event.getWhoClicked().getName()) != null) {
                        CustomMobEditor.getCurrentMob(event.getWhoClicked().getName()).setType(EntityType.valueOf(event.getCurrentItem().getItemMeta().getDisplayName()));
                        event.getWhoClicked().closeInventory();
                        ItemStack is = ((Inventory)CustomMobEditor.getCurrentEditing().get(event.getWhoClicked().getName())).getItem(5);
                        ItemMeta im = is.getItemMeta();
                        im.setLore(Arrays.asList(ChatColor.GRAY + "Type: " + ChatColor.BLUE + CustomMobEditor.getCurrentMob(event.getWhoClicked().getName()).getType().name()));
                        is.setItemMeta(im);
                        (new BukkitRunnable() {
                            public void run() {
                                CustomMobEditor.editCustomMob((Player)event.getWhoClicked(), CustomMobEditor.getCurrentMob(event.getWhoClicked().getName()));
                            }
                        }).runTaskLater(Dungeons.getPlugin(), 2L);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDrag(InventoryDragEvent event) {
        if (!event.getWhoClicked().getOpenInventory().getTitle().equals(ChatColor.BLUE + "CustomMob list")) {
            event.setCancelled(true);
        } else {
            Player p = (Player)event.getWhoClicked();
            if (CustomMobEditor.getCurrentEditing().containsKey(p.getName())) {
                if (((Inventory)CustomMobEditor.getCurrentEditing().get(p.getName())).getViewers().contains(p)) {
                    if (((Inventory)CustomMobEditor.getCurrentEditing().get(p.getName())).equals(event.getInventory())) {
                        event.setCancelled(true);
                    }

                }
            }
        }
    }

    @EventHandler
    public void mobListe(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getView().getTitle().equals(ChatColor.BLUE + "CustomMob list")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    CustomMob cm = CustomMob.getByID(event.getSlot());
                    if (event.getClick() == ClickType.LEFT) {
                        ((Player)event.getWhoClicked()).performCommand("custommob info " + cm.getReferenceID());
                    } else if (event.getClick() == ClickType.RIGHT) {
                        ((Player)event.getWhoClicked()).performCommand("custommob edit " + cm.getReferenceID());
                    }
                }

            }
        }
    }
}
