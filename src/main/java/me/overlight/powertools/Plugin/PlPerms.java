package me.overlight.powertools.Plugin;

import me.overlight.powertools.APIs.Vault;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlPerms {
    public static boolean hasPerm(CommandSender player, String perm){
        return Vault.perms().has(player, perm);
    }

    public enum Perms{

        KnockBackCommand("modules.command.knockback"),
        KnockBackStick("modules.stick.knockback"),

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
