//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeoneditor;

import fr.dungeons.dungeon.actions.DungeonActionType;
import fr.dungeons.dungeon.events.DungeonEventType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandCompleter implements TabCompleter {
    public CommandCompleter() {
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> toReturn = new ArrayList();
        int var7;
        int var8;
        if (cmd.equals(Bukkit.getPluginCommand("createaction"))) {
            if (args.length == 1) {
                DungeonActionType[] var6 = DungeonActionType.values();
                var7 = var6.length;

                for(var8 = 0; var8 < var7; ++var8) {
                    DungeonActionType type = var6[var8];
                    if (type != DungeonActionType.KILL_MOBS && type.name().contains(args[0].toUpperCase())) {
                        toReturn.add(type.name());
                    }
                }
            }
        } else if (cmd.equals(Bukkit.getPluginCommand("createevent")) && args.length == 1) {
            DungeonEventType[] var10 = DungeonEventType.values();
            var7 = var10.length;

            for(var8 = 0; var8 < var7; ++var8) {
                DungeonEventType type = var10[var8];
                if (type.name().contains(args[0].toUpperCase())) {
                    toReturn.add(type.name());
                }
            }
        }

        return toReturn;
    }
}
