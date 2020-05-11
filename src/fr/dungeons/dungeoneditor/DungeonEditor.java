/*     */
package fr.dungeons.dungeoneditor;
/*     */
/*     */

import fr.dungeons.Dungeons;
/*     */ import fr.dungeons.custommob.CustomMob;
/*     */ import fr.dungeons.dungeon.Dungeon;
/*     */ import fr.dungeons.dungeon.actions.DungeonAction;
/*     */ import fr.dungeons.dungeon.actions.DungeonActionType;
/*     */ import fr.dungeons.dungeon.events.DungeonEvent;
/*     */ import fr.dungeons.dungeon.events.DungeonEventType;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.potion.PotionEffectType;
/*     */ import org.bukkit.util.Vector;

/*     */
/*     */ public class DungeonEditor
        /*     */ extends JavaPlugin {
    /*     */
    public static Plugin getPlugin() {
        /*  34 */
        return (Plugin) getPlugin(DungeonEditor.class);
        /*     */
    }

    /*     */
    /*     */
    /*     */
    public void onEnable() {
        /*  39 */
        getCommand("createaction").setTabCompleter(new CommandCompleter());
        /*  40 */
        getCommand("createevent").setTabCompleter(new CommandCompleter());
        /*     */
        /*  42 */
        Bukkit.getPluginManager().registerEvents(new CustomMobListener(), (Plugin) this);
        /*     */
        /*  44 */
        Dungeons.setOffMode();
        /*     */
        /*  46 */
        if (!getPlugin().getDataFolder().exists()) getPlugin().getDataFolder().mkdir();
        /*     */
        /*     */
    }

    /*     */
    /*     */
    /*     */
    /*     */
    public void onDisable() {
    }

    /*     */
    /*     */
    /*     */
    /*     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        /*  57 */
        if (args.length == 0) {
            /*  58 */
            sender.sendMessage(ChatColor.RED + cmd.getUsage());
            /*  59 */
            return true;
            /*     */
        }
        /*     */
        /*  62 */
        if (cmd.equals(CustomMobCommands.cmd)) {
            CustomMobCommands.onCommand(sender, args);
        }
        /*  63 */
        else if (cmd.equals(getCommand("createaction")))
            /*  64 */ {
            for (int i = 1; i < args.length; ) {
                args[i] = args[i].replace('&', '§');
                i++;
            }
            /*     */
            try {
                /*     */
                List<String> msg;
                String arg;
                int j;
                /*  67 */
                DungeonActionType type = DungeonActionType.valueOf(args[0]);
                /*  68 */
                DungeonAction da = null;
                /*     */
                /*  70 */
                switch (type) {
                    /*     */
                    case DISP_MESSAGE:
                        /*  72 */
                        if (args.length > 1) {
                            /*  73 */
                            List<String> list = new ArrayList<>();
                            /*  74 */
                            String str = "";
                            /*  75 */
                            for (int k = 1; k < args.length; ) {
                                str = str + args[k] + " ";
                                k++;
                            }
                            /*  76 */
                            for (String s : str.split("@n")) list.add(s);
                            /*  77 */
                            da = DungeonAction.createMessageAction(list, DungeonAction.generateID());
                            break;
                            /*     */
                        }
                        /*  79 */
                        sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " <msg>");
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    case DISP_TITLE:
                        /*  84 */
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " <title> [subtitle]");
                            break;
                        }
                        /*     */
                        /*  86 */
                        msg = new ArrayList<>();
                        /*  87 */
                        arg = "";
                        /*  88 */
                        for (j = 1; j < args.length; ) {
                            arg = arg + args[j] + " ";
                            j++;
                        }
                        /*  89 */
                        for (String s : arg.split("@n")) msg.add(s);
                        /*  90 */
                        da = DungeonAction.createTitleAction(msg.get(0), (msg.size() > 1) ? msg.get(1) : null, DungeonAction.generateID());
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    /*     */
                    case GIVE_EXP:
                        /*  96 */
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " <(int) amount>");
                            break;
                        }
                        /*     */
                        /*     */
                        try {
                            /*  99 */
                            int amount = Integer.parseInt(args[1]);
                            /* 100 */
                            da = DungeonAction.createExpAction(amount, DungeonAction.generateID());
                            /* 101 */
                        } catch (NumberFormatException e) {
                            /* 102 */
                            sender.sendMessage(ChatColor.RED + args[1] + " not a Integer !");
                            /*     */
                        }
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    case GIVE_ITEM:
                        /* 108 */
                        if (sender instanceof Player) {
                            /* 109 */
                            ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                            /* 110 */
                            if (is == null || is.getType() == Material.AIR) {
                                sender.sendMessage(ChatColor.RED + "Incorrect item in hand");
                                break;
                            }
                            /*     */
                            /* 112 */
                            is.setAmount(((Player) sender).getInventory().getItemInMainHand().getAmount());
                            /* 113 */
                            da = DungeonAction.createGiveItemAction(is, DungeonAction.generateID());
                            /*     */
                            break;
                            /*     */
                        }
                        /* 116 */
                        sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    case GIVE_POTION:
                        /* 121 */
                        if (args.length < 3) {
                            sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " <PotionEffectType> <(int) duration sec> [(int) amplifier]");
                            break;
                        }
                        /*     */
                        /*     */
                        try {
                            /* 124 */
                            PotionEffectType pet = PotionEffectType.getByName(args[1]);
                            /* 125 */
                            if (pet != null) {
                                /* 126 */
                                int amplifier = (args.length == 4) ? Integer.parseInt(args[3]) : 1;
                                /* 127 */
                                da = DungeonAction.createPotionAction(pet, Integer.parseInt(args[2]) * 20, amplifier, DungeonAction.generateID());
                                break;
                                /* 128 */
                            }
                            sender.sendMessage(ChatColor.RED + "Incorrect PotionEffectType");
                            /* 129 */
                        } catch (NumberFormatException e) {
                            /* 130 */
                            sender.sendMessage(ChatColor.RED + args[1] + " not a Integer !");
                            /*     */
                        }
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    case PASTE_SCHEMATIC:
                        /* 136 */
                        if (sender instanceof Player) {
                            /* 137 */
                            if (args.length == 1) {
                                sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " <schematicName> [replaceAir]");
                                /*     */
                                break;
                            }
                            /*     */
                            /* 140 */
                            Vector vec = ((Player) sender).getTargetBlock((Set) null, 50).getLocation().toVector();
                            /* 141 */
                            if (args.length >= 2) {
                                /* 142 */
                                da = DungeonAction.createSchematicPasteAction(vec, args[1], Boolean.parseBoolean(args[2]), DungeonAction.generateID());
                                break;
                                /* 143 */
                            }
                            da = DungeonAction.createSchematicPasteAction(vec, args[1], false, DungeonAction.generateID());
                            /*     */
                            break;
                            /*     */
                        }
                        /* 146 */
                        sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    case SPAWN_ITEM:
                        /* 151 */
                        if (sender instanceof Player) {
                            /* 152 */
                            Vector vec = ((Player) sender).getLocation().toVector();
                            /* 153 */
                            ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                            /* 154 */
                            if (is == null || is.getType() == Material.AIR) {
                                sender.sendMessage(ChatColor.RED + "Incorrect item in hand");
                                break;
                            }
                            /*     */
                            /* 156 */
                            is.setAmount(((Player) sender).getInventory().getItemInMainHand().getAmount());
                            /* 157 */
                            da = DungeonAction.createSpawnItemAction(is, vec, DungeonAction.generateID());
                            /*     */
                            break;
                            /*     */
                        }
                        /* 160 */
                        sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                        /*     */
                        break;
                    /*     */
                    /*     */
                    case SPAWN_MOB:
                        /* 164 */
                        if (sender instanceof Player) {
                            /* 165 */
                            if (args.length < 2) {
                                sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " <customMobID> [mobCount]");
                                break;
                            }
                            /*     */
                            /* 167 */
                            Vector vec = ((Player) sender).getLocation().toVector();
                            /*     */
                            /*     */
                            try {
                                /* 170 */
                                int id = Integer.parseInt(args[1]);
                                /* 171 */
                                if (args.length > 2) {
                                    /* 172 */
                                    int count = Integer.parseInt(args[2]);
                                    /* 173 */
                                    da = DungeonAction.createMobSpawnAction(vec, CustomMob.getByID(id), count, DungeonAction.generateID());
                                    break;
                                    /*     */
                                }
                                /* 175 */
                                da = DungeonAction.createMobSpawnAction(vec, CustomMob.getByID(id), 1, DungeonAction.generateID());
                                /* 176 */
                            } catch (NumberFormatException e) {
                                /* 177 */
                                sender.sendMessage(ChatColor.RED + "Not a Integer");
                                /*     */
                            }
                            /*     */
                            break;
                            /*     */
                        }
                        /* 181 */
                        sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    case TELEPORT:
                        /* 186 */
                        if (sender instanceof Player) {
                            /* 187 */
                            Vector vec = ((Player) sender).getLocation().toVector();
                            /* 188 */
                            da = DungeonAction.createTeleportAction(vec, DungeonAction.generateID());
                            break;
                            /*     */
                        }
                        /* 190 */
                        sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    case TIME_OUT:
                        /* 195 */
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " <(int) tick> <(int) action1> [(int) action2] [...]");
                            break;
                        }
                        /*     */
                        /*     */
                        try {
                            /* 198 */
                            int tick = Integer.parseInt(args[1]);
                            /* 199 */
                            List<DungeonAction> actions = new ArrayList<>();
                            /* 200 */
                            for (j = 2; j < args.length; ) {
                                actions.add(DungeonAction.getByID(Integer.parseInt(args[j])));
                                j++;
                            }
                            /* 201 */
                            da = DungeonAction.createTimeOutAction(tick, actions, DungeonAction.generateID());
                            /* 202 */
                        } catch (NumberFormatException e) {
                            /* 203 */
                            sender.sendMessage(ChatColor.RED + "Not a Integer");
                            /*     */
                        }
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    /*     */
                    case STOP_TIME_OUT:
                        /* 210 */
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " <action1> [action2] [...]");
                            break;
                        }
                        /*     */
                        /*     */
                        try {
                            /* 213 */
                            List<DungeonAction> timeOutFromActions = new ArrayList<>();
                            /* 214 */
                            for (int k = 1; k < args.length; ) {
                                timeOutFromActions.add(DungeonAction.getByID(Integer.parseInt(args[k])));
                                k++;
                            }
                            /* 215 */
                            da = DungeonAction.createStopTimeOutAction(timeOutFromActions, DungeonAction.generateID());
                            /* 216 */
                        } catch (NumberFormatException e) {
                            /* 217 */
                            sender.sendMessage(ChatColor.RED + "Not a Integer");
                            /*     */
                        }
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    /*     */
                    case CREATE_EXPLOSION:
                        /* 224 */
                        if (sender instanceof Player) {
                            /* 225 */
                            if (args.length < 1) {
                                sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " [ExplosionStrength=1] [fire=false|true] [breakBlocks=false|true]");
                                break;
                            }
                            /*     */
                            /* 227 */
                            Vector vec = ((Player) sender).getLocation().toVector();
                            /*     */
                            try {
                                /* 229 */
                                float strengh = (args.length >= 2) ? Float.parseFloat(args[1]) : 1.0F;
                                /* 230 */
                                boolean fire = (args.length >= 3 && Boolean.parseBoolean(args[2]));
                                /* 231 */
                                boolean breakBlocks = (args.length >= 4 && Boolean.parseBoolean(args[3]));
                                /* 232 */
                                da = DungeonAction.createExplosionAction(vec, strengh, fire, breakBlocks, DungeonAction.generateID());
                                /* 233 */
                            } catch (NumberFormatException e) {
                                /* 234 */
                                sender.sendMessage(ChatColor.RED + "Not a float");
                                /*     */
                            }
                            /*     */
                            break;
                            /*     */
                        }
                        /* 238 */
                        sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                        /*     */
                        break;
                    /*     */
                    /*     */
                    case ACTIVATE_EVENT:
                        /* 242 */
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/createaction " + args[0] + " <event1> [event2] [...]");
                            break;
                        }
                        /*     */
                        /*     */
                        try {
                            /* 245 */
                            List<Integer> eventsID = new ArrayList<>();
                            /* 246 */
                            for (int k = 1; k < args.length; ) {
                                eventsID.add(Integer.valueOf(Integer.parseInt(args[k])));
                                k++;
                            }
                            /* 247 */
                            da = DungeonAction.createActivateEventAction(eventsID, DungeonAction.generateID());
                            /* 248 */
                        } catch (NumberFormatException e) {
                            /* 249 */
                            sender.sendMessage(ChatColor.RED + "Not a Integer");
                            /*     */
                        }
                        /*     */
                        break;
                    /*     */
                    /*     */
                    /*     */
                    default:
                        /* 255 */
                        da = null;
                        /* 256 */
                        sender.sendMessage(ChatColor.RED + "Action not found");
                        /*     */
                        break;
                    /*     */
                }
                /*     */
                /*     */
                /* 261 */
                if (da == null) {
                    sender.sendMessage(ChatColor.RED + "Action not created");
                }
                /*     */
                else
                    /* 263 */ {
                    sender.sendMessage(ChatColor.GREEN + "Action created ! Reference_ID: " + da.getReferenceID());
                    /*     */
                    /* 265 */
                    File actionFolder = new File(getPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + "Actions");
                    /* 266 */
                    if (!actionFolder.exists()) actionFolder.mkdir();
                    /*     */
                    /* 268 */
                    File actionFile = new File(actionFolder.getAbsolutePath() + File.separatorChar + "Action_" + da.getReferenceID() + ".yml");
                    /*     */
                    /* 270 */
                    YamlConfiguration yml = new YamlConfiguration();
                    /* 271 */
                    yml.loadFromString(da.getData().serialize());
                    /* 272 */
                    yml.save(actionFile);
                }
                /*     */
                /*     */
                /* 275 */
            } catch (Exception e) {
                /* 276 */
                sender.sendMessage(ChatColor.RED + "Can't find type \"" + args[0] + "\"");
                /*     */
            }
            /*     */
        }
        /* 279 */
        else if (cmd.equals(getCommand("doaction")))
            /* 280 */ {
            if (args.length >= 1)
                /* 281 */ {
                DungeonAction da = DungeonAction.getByID(Integer.parseInt(args[0]));
                /* 282 */
                da.action();
            }
            /* 283 */
            else {
                sender.sendMessage(ChatColor.RED + "Argument manquant.");
            }
            /*     */
        }
        /* 285 */
        else if (cmd.equals(getCommand("createevent")))
            /* 286 */ {
            DungeonEventType type = DungeonEventType.valueOf(args[0]);
            /* 287 */
            DungeonEvent de = null;
            /*     */
            /*     */
            /* 290 */
            switch (type) {
                /*     */
                case AREA_DETECTION:
                    /* 292 */
                    if (sender instanceof Player) {
                        /* 293 */
                        if (args.length < 3) {
                            sender.sendMessage(ChatColor.RED + "/createevent " + args[0] + " <radius> <action1> [action2] [...]");
                            break;
                        }
                        /*     */
                        /*     */
                        try {
                            /* 296 */
                            double radius = Double.parseDouble(args[1]);
                            /* 297 */
                            Vector vec = ((Player) sender).getLocation().toVector();
                            /* 298 */
                            List<DungeonAction> actions = new ArrayList<>();
                            /* 299 */
                            int i = 2;
                            while (true) {
                                if (i < args.length) {
                                    /* 300 */
                                    DungeonAction da = DungeonAction.getByID(Integer.parseInt(args[i]));
                                    /* 301 */
                                    if (da == null) {
                                        /* 302 */
                                        sender.sendMessage(ChatColor.RED + "Aucune action ID: " + args[i]);
                                        /*     */
                                        break;
                                        /*     */
                                    }
                                    /* 305 */
                                    actions.add(da);
                                    i++;
                                    continue;
                                    /*     */
                                }
                                de = DungeonEvent.createAreaActivationEvent(actions, Arrays.asList(vec), radius, DungeonEvent.generateID());
                                break;
                            }
                            /* 308 */
                        } catch (NumberFormatException e) {
                            /* 309 */
                            sender.sendMessage(ChatColor.RED + "Not a number");
                            /*     */
                        }
                        /*     */
                        break;
                        /*     */
                    }
                    /* 313 */
                    sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                    /*     */
                    break;
                /*     */
                /*     */
                /*     */
                case MOB_WAVE_ENDED:
                    /* 318 */
                    if (args.length < 4) {
                        sender.sendMessage(ChatColor.RED + "/createevent " + args[0] + " <fromActionID> [...] @n <todoActionID> [...]");
                        break;
                    }
                    /*     */
                    /*     */
                    try {
                        /* 321 */
                        List<DungeonAction> actionsFrom = new ArrayList<>();
                        /* 322 */
                        List<DungeonAction> actionTodo = new ArrayList<>();
                        /*     */
                        int i;
                        /* 324 */
                        for (i = 1; i < args.length &&
                                /* 325 */               !args[i].equals("@n"); i++) {
                            /*     */
                            /* 327 */
                            DungeonAction da = DungeonAction.getByID(Integer.parseInt(args[i]));
                            /* 328 */
                            if (da == null) {
                                /* 329 */
                                sender.sendMessage(ChatColor.RED + "Aucune action ID: " + args[i]);
                                /*     */                 // Byte code: goto -> 4340
                                /*     */
                            }
                            /* 332 */
                            actionsFrom.add(da);
                            /*     */
                        }
                        /* 334 */
                        while (++i < args.length) {
                            actionTodo.add(DungeonAction.getByID(Integer.parseInt(args[i])));
                            i++;
                        }
                        /* 335 */
                        de = DungeonEvent.createMobWaveEndedEvent(actionsFrom, actionTodo, DungeonEvent.generateID());
                        /* 336 */
                    } catch (NumberFormatException e) {
                        /* 337 */
                        sender.sendMessage(ChatColor.RED + "Not a Integer");
                        /*     */
                    }
                    /*     */
                    break;
                /*     */
                /*     */
                /*     */
                /*     */
                case PUT_ITEM:
                    /* 344 */
                    if (sender instanceof Player) {
                        /* 345 */
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/createevent " + args[0] + " <action1> [action2] [...]");
                            break;
                        }
                        /*     */
                        /* 347 */
                        Vector vec = null;
                        /*     */
                        /* 349 */
                        Block center = ((Player) sender).getLocation().getBlock();
                        /* 350 */
                        for (BlockFace bf : BlockFace.values()) {
                            /* 351 */
                            Block b = center.getRelative(bf);
                            /* 352 */
                            if (b.getType() == Material.CHEST) vec = b.getLocation().toVector();
                            /*     */
                            /*     */
                        }
                        /* 355 */
                        ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                        /*     */
                        /*     */
                        try {
                            /* 358 */
                            List<DungeonAction> actionsTodo = new ArrayList<>();
                            /* 359 */
                            for (int i = 1; i < args.length; i++) {
                                /* 360 */
                                DungeonAction da = DungeonAction.getByID(Integer.parseInt(args[i]));
                                /* 361 */
                                if (da == null) {
                                    /* 362 */
                                    sender.sendMessage(ChatColor.RED + "Aucune action ID: " + args[i]);
                                    /*     */                   // Byte code: goto -> 4340
                                    /*     */
                                }
                                /* 365 */
                                actionsTodo.add(da);
                                /*     */
                            }
                            /*     */
                            /* 368 */
                            if (vec != null) {
                                /* 369 */
                                if (is == null || is.getType() == Material.AIR) {
                                    sender.sendMessage(ChatColor.RED + "Incorrect item in hand");
                                    break;
                                }
                                /* 370 */
                                de = DungeonEvent.createPutItemEvent(is, vec, actionsTodo, DungeonEvent.generateID());
                                break;
                                /*     */
                            }
                            /* 372 */
                            sender.sendMessage(ChatColor.RED + "No chest found around");
                            /* 373 */
                        } catch (NumberFormatException e) {
                            /* 374 */
                            sender.sendMessage(ChatColor.RED + "Not a Integer");
                            /*     */
                        }
                        /*     */
                        break;
                        /*     */
                    }
                    /* 378 */
                    sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                    /*     */
                    break;
                /*     */
                /*     */
                /*     */
                case PLACE_BLOCK:
                    /* 383 */
                    if (sender instanceof Player) {
                        /* 384 */
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/createevent " + args[0] + " <action1> [action2] [...]");
                            break;
                        }
                        /*     */
                        /*     */
                        try {
                            /* 387 */
                            List<DungeonAction> actionsTodo = new ArrayList<>();
                            /* 388 */
                            for (int i = 1; i < args.length; i++) {
                                /* 389 */
                                DungeonAction da = DungeonAction.getByID(Integer.parseInt(args[i]));
                                /* 390 */
                                if (da == null) {
                                    /* 391 */
                                    sender.sendMessage(ChatColor.RED + "Aucune action ID: " + args[i]);
                                    /*     */                   // Byte code: goto -> 4340
                                    /*     */
                                }
                                /* 394 */
                                actionsTodo.add(da);
                                /*     */
                            }
                            /*     */
                            /* 397 */
                            Vector vec = ((Player) sender).getLocation().toVector();
                            /* 398 */
                            vec.setX(vec.getBlockX());
                            /* 399 */
                            vec.setY(vec.getBlockY());
                            /* 400 */
                            vec.setZ(vec.getBlockZ());
                            /* 401 */
                            ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                            /* 402 */
                            if (is == null || is.getType() == Material.AIR) {
                                /* 403 */
                                sender.sendMessage(ChatColor.RED + "Vous devez posseder le block en main");
                                break;
                                /*     */
                            }
                            /* 405 */
                            Material m = is.getType();
                            /* 406 */
                            if (m == Material.WITHER_SKELETON_SKULL) m = Material.WITHER_SKELETON_SKULL;
                            /*     */
                            /* 408 */
                            if (m.isBlock())
                                de = DungeonEvent.createPlaceBlockEvent(m, vec, actionsTodo, DungeonEvent.generateID());
                            /*     */
                            /* 410 */
                        } catch (NumberFormatException e) {
                            /* 411 */
                            sender.sendMessage(ChatColor.RED + "Not a Integer");
                            /*     */
                        }
                        /*     */
                        break;
                        /*     */
                    }
                    /* 415 */
                    sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                    /*     */
                    break;
                /*     */
                /*     */
                /*     */
                case INTERACT:
                    /* 420 */
                    if (sender instanceof Player) {
                        /* 421 */
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "/createevent " + args[0] + "<action1> [action2] [...]");
                            break;
                        }
                        /*     */
                        /* 423 */
                        Vector vec = null;
                        /*     */
                        /* 425 */
                        Block center = ((Player) sender).getLocation().getBlock();
                        /* 426 */
                        for (BlockFace bf : BlockFace.values()) {
                            /* 427 */
                            Block b = center.getRelative(bf);
                            /* 428 */
                            if (Arrays.<Material>asList(new Material[]{Material.STONE_BUTTON, Material.BIRCH_BUTTON, Material.ACACIA_BUTTON, Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON, Material.OAK_BUTTON, Material.BIRCH_BUTTON, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.STONE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.LEVER
/* 429 */}).contains(b.getType())) vec = b.getLocation().toVector();
                            /*     */
                            /*     */
                        }
                        /*     */
                        try {
                            /* 433 */
                            List<DungeonAction> actionsTodo = new ArrayList<>();
                            /* 434 */
                            for (int i = 1; i < args.length; i++) {
                                /* 435 */
                                DungeonAction da = DungeonAction.getByID(Integer.parseInt(args[i]));
                                /* 436 */
                                if (da == null) {
                                    /* 437 */
                                    sender.sendMessage(ChatColor.RED + "Aucune action ID: " + args[i]);
                                    /*     */                   // Byte code: goto -> 4340
                                    /*     */
                                }
                                /* 440 */
                                actionsTodo.add(da);
                                /*     */
                            }
                            /*     */
                            /* 443 */
                            if (vec != null) {
                                de = DungeonEvent.createInteractEvent(vec, actionsTodo, DungeonEvent.generateID());
                                break;
                            }
                            /* 444 */
                            sender.sendMessage(ChatColor.RED + "No block to interact found around");
                            /* 445 */
                        } catch (NumberFormatException e) {
                            /* 446 */
                            sender.sendMessage(ChatColor.RED + "Not a Integer");
                            /*     */
                        }
                        /*     */
                        break;
                        /*     */
                    }
                    /* 450 */
                    sender.sendMessage(ChatColor.RED + "You must be a player to do this");
                    /*     */
                    break;
                /*     */
                /*     */
                /*     */
                default:
                    /* 455 */
                    de = null;
                    /* 456 */
                    sender.sendMessage(ChatColor.RED + "Event not found");
                    /*     */
                    break;
                /*     */
            }
            /*     */
            /*     */
            /* 461 */
            if (de == null) {
                sender.sendMessage(ChatColor.RED + "Event not created");
            }
            /*     */
            else
                /* 463 */ {
                sender.sendMessage(ChatColor.GREEN + "Event created ! Reference_ID: " + de.getReferenceID());
                /*     */
                /* 465 */
                File eventFolder = new File(getPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + "Events");
                /* 466 */
                if (!eventFolder.exists()) eventFolder.mkdir();
                /*     */
                /* 468 */
                File eventFile = new File(eventFolder.getAbsolutePath() + File.separatorChar + "Event_" + de.getReferenceID() + ".yml");
                /*     */
                /* 470 */
                YamlConfiguration yml = new YamlConfiguration();
                /*     */
                try {
                    /* 472 */
                    yml.loadFromString(de.getData().serialize());
                    /* 473 */
                    yml.save(eventFile);
                    /* 474 */
                } catch (InvalidConfigurationException | java.io.IOException e) {
                    /* 475 */
                    e.printStackTrace();
                    /*     */
                }
                /*     */
            }
            /*     */
        }
        /* 479 */
        else if (cmd.equals(getCommand("createdungeon")))
            /* 480 */ {
            if (args.length < 11) {
                sender.sendMessage(ChatColor.RED + cmd.getUsage());
            }
            /*     */
            else
                /* 482 */ {
                Dungeon d = null;
                /*     */
                /* 484 */
                String name = args[0];
                /*     */
                /*     */
                try {
                    /* 487 */
                    double x1 = Double.parseDouble(args[1]), y1 = Double.parseDouble(args[2]), z1 = Double.parseDouble(args[3]);
                    /* 488 */
                    double x2 = Double.parseDouble(args[4]), y2 = Double.parseDouble(args[5]), z2 = Double.parseDouble(args[6]);
                    /* 489 */
                    double sx = Double.parseDouble(args[7]), sy = Double.parseDouble(args[8]), sz = Double.parseDouble(args[9]);
                    /*     */
                    /* 491 */
                    d = new Dungeon(0, new Vector(x1, y1, z1), new Vector(x2, y2, z2), new Vector(sx, sy, sz), name);
                    /*     */
                    /* 493 */
                    for (int i = 10; i < args.length; i++) {
                        /* 494 */
                        DungeonEvent de = DungeonEvent.getByID(Integer.parseInt(args[i]));
                        /* 495 */
                        if (de != null) d.addEvent(de);
                        /*     */
                    }
                    /* 497 */
                } catch (NumberFormatException event) {
                    /* 498 */
                    sender.sendMessage(ChatColor.RED + "Not a number");
                    /*     */
                }
                /*     */
                /* 501 */
                if (d == null) {
                    sender.sendMessage(ChatColor.RED + "Dungeon not created");
                }
                /*     */
                else
                    /* 503 */ {
                    sender.sendMessage(ChatColor.GREEN + "Dungeon " + d.getName() + " created !");
                    /*     */
                    /* 505 */
                    File dungeonFolder = new File(getPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + "Dungeons");
                    /* 506 */
                    if (!dungeonFolder.exists()) dungeonFolder.mkdir();
                    /*     */
                    /* 508 */
                    File dungeonFile = new File(dungeonFolder.getAbsolutePath() + File.separatorChar + "Dungeon_" + d.getReferenceID() + ".yml");
                    /*     */
                    /* 510 */
                    YamlConfiguration yml = new YamlConfiguration();
                    /*     */
                    try {
                        /* 512 */
                        yml.loadFromString(d.serialize());
                        /* 513 */
                        yml.save(dungeonFile);
                        /* 514 */
                    } catch (InvalidConfigurationException | java.io.IOException e) {
                        /* 515 */
                        e.printStackTrace();
                        /*     */
                    }
                }
                /*     */
            }
            /*     */
        }
        /*     */
        /*     */
        /* 521 */
        return true;
        /*     */
    }
    /*     */
}


/* Location:              /Users/tancou/Documents/Katail/DungeonEditor.jar!/fr/dungeons/dungeoneditor/DungeonEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */