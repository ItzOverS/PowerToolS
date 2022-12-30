package me.overlight.powertools.Plugin;

import me.overlight.powertools.APIs.Vault;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlPerms {
    public static boolean hasPerm(CommandSender player, String perm){
        return player.hasPermission(perm);
    }

    public enum Perms{

        KnockBackCommand("modules.knockback.command"),
        KnockBackStick("modules.knockback.stick"),
        FreezeCommand("modules.freeze.command"),
        FreezeStick("modules.freeze.stick"),

        ;
        private final String perm;
        Perms(String perm){
            this.perm = perm;
        }
        public String get(){
            return "powertools." + perm;
        }
    }
}
