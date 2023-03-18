package me.overlight.powertools.bukkit.Libraries.Vote;

import me.overlight.powertools.bukkit.Libraries.RandomUtil;
import me.overlight.powertools.bukkit.Plugin.PlInfo;
import me.overlight.powertools.bukkit.Plugin.PlPerms;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Vote {
    private String name, permission;
    private long id;

    public List<String> getVoters() {
        return voters;
    }

    private List<VoteOption> options;
    private List<String> voters;

    public String getPermission() {
        return permission;
    }

    public Vote(String name, String permission, List<VoteOption> options) {
        this.name = name;
        this.options = options;
        this.permission = permission;
        this.id = RandomUtil.randomInteger();
        this.voters = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public List<VoteOption> getOptions() {
        return options;
    }

    public void startVote(CommandSender sender) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (permission.equals("ALL")) sendFor(player, sender);
            else if (permission.equals("OP") && player.isOp()) sendFor(player, sender);
            else if (permission.equals("DEOP") && !player.isOp()) sendFor(player, sender);
            else if (PlPerms.hasPerm(player, permission)) sendFor(player, sender);
        }
        ((Player) sender).closeInventory();
    }

    public boolean updateOption(int index, Player who) {
        if (!voters.contains(who.getName())) {
            options.set(index, new VoteOption(options.get(index).getName(), options.get(index).getText(), options.get(index).getRange() + 1));
            voters.add(who.getName());
            return true;
        }
        return false;
    }

    private void sendFor(Player player, CommandSender sender) {
        player.sendMessage(PlInfo.PREFIX + "New vote started by " + sender.getName());
        player.sendMessage("Vote this: " + this.name);
        int index = 0;
        for (VoteOption op : options) {
            if (op.getText() == null) continue;
            TextComponent compo = new TextComponent(((index % 2 == 0) ? ChatColor.GREEN : ChatColor.AQUA) + "- " + op.getText());
            compo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.AQUA + "Click to vote for " + op.getName()).create()));
            compo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pts vote give " + index + " " + this.id));

            player.spigot().sendMessage(compo);
            index++;
        }
    }
}
