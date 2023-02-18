package me.overlight.powertools.spigot.Plugin;

import org.bukkit.command.CommandSender;

public class PlPerms {
    public static boolean hasPerm(CommandSender player, String perm) {
        return player.hasPermission(perm);
    }

    public enum Perms {
        Alerts("alerts"),
        KnockBackCommand("modules.knockback.command"),
        KnockBackStick("modules.knockback.stick"),
        FreezeCommand("modules.freeze.command"),
        FreezeStick("modules.freeze.stick"),
        ProtectCommand("modules.protect.command"),
        RotateCommand("modules.rotate.command"),
        RotateStick("modules.rotate.stick"),
        PlayTimeCommand("modules.playtime.command"),
        VanishCommand("modules.vanish.command"),
        ToggleCommand("modules.toggle.command"),
        CpsCheckCommand("modules.cpscheck.command"),
        BlackListAdd("blacklist.add"),
        BlackListRemove("blacklist.remove"),
        BlackListList("blacklist.list"),
        BlackList("blacklist.*"),
        BlackListNotFind("blacklist.invalid"),
        WhiteListAdd("whitelist.add"),
        WhiteListRemove("whitelist.remove"),
        WhiteList("whitelist.*"),
        WhiteListList("whitelist.list"),
        WhiteListNotFind("whitelist.invalid"),
        AddOnsList("addons.list"),
        AddOnsManage("addons.manage"),
        Help("help"),
        PluginReload("reload"),
        Mute("mute.command"),
        UnMute("unmute.command"),
        ;
        private final String perm;

        Perms(String perm) {
            this.perm = perm;
        }

        public String get() {
            return "powertools." + perm;
        }
    }
}
