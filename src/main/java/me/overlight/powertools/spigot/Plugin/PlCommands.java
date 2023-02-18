package me.overlight.powertools.spigot.Plugin;

public enum PlCommands {
    reload("reload", "reload plugins config files", "reload", PlPerms.Perms.PluginReload.get()),
    knockback("knockback", "knockback targeted player to detect velocity hack", "knockback {target}", PlPerms.Perms.KnockBackCommand.get()),
    rotate("rotate", "rotate targeted player to detect NoRot hack", "rotate {target}", PlPerms.Perms.RotateCommand.get()),
    playtime("playtime", "get player's playtime", "playtime {target}", PlPerms.Perms.PlayTimeCommand.get()),
    protect("protect", "protect players from damage", "protect {target}", PlPerms.Perms.ProtectCommand.get()),
    vanish("vanish", "hide your self from other players", "vanish {target}", PlPerms.Perms.VanishCommand.get()),
    toggle("toggle", "toggle something of a player to your self", "toggle {toggleItem} [target]", PlPerms.Perms.ToggleCommand.get()),
    freeze("freeze", "freeze a player for SS", "freeze {target}", PlPerms.Perms.FreezeCommand.get()),
    cps("cps", "get player's 'client show' cps", "cps {target}", PlPerms.Perms.CpsCheckCommand.get()),
    knockbackAll("knockbackAll", "Knockback all players at same time to detect who using velocity hack", "knockbackall", PlPerms.Perms.KnockBackCommand.get()),
    blacklist("blacklist", "Blacklist who you want they don't play in your server", "blacklist {add/remove/list} [target]", PlPerms.Perms.BlackList.get()),
    whitelist("whitelist", "whitelist who you want powertools skip them at checks ( AntiBot, ... )", "whitelist {add/remove/list} [target]", PlPerms.Perms.WhiteList.get()),
    addons("addons", "manage plugin's addons without accessing the config.yml", "addons {addonName} {option} {value}", PlPerms.Perms.AddOnsManage.get()),
    mute("mute", "Mute a player to prevent them from sending chat & sign placement", "mute {target} {date} [reason]", PlPerms.Perms.Mute.get()),
    unmute("unmute", "unMute a player if they're muted", "unmute {target}", PlPerms.Perms.UnMute.get()),
    ;
    final String name, hover, click, perm;

    PlCommands(String name, String hover, String click, String permission) {
        this.name = name;
        this.hover = hover;
        this.click = click;
        this.perm = permission;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.hover;
    }

    public String getUsage() {
        return "/pts " + this.click;
    }

    public String getPermission() {
        return this.perm;
    }
}
