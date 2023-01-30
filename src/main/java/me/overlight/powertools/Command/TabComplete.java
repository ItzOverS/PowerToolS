package me.overlight.powertools.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabComplete
        implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        String[] commands = {
                "pts knockback {TARGET}",
                "pts freeze {TARGET}",
                "pts rotate {TARGET}",
                "pts playtime {TARGET}",
                "pts protect {TARGET}",
                "pts vanish {TARGET}",
                "pts toggle {TARGET} {TOGGLE_TYPES}",
                "pts cps {TARGET}"
        };
        List<String> currentIndexCommands = new ArrayList<>();
        for(int i = 0; i < commands.length; i++){
            if(commands[i].split(" ").length <= args.length) continue;
            if (commands[i].startsWith("pts " + mixArray(args, " "))) {
                currentIndexCommands.addAll(replaceVars(commands[i].split(" ")[args.length]));
            }
        }
        return currentIndexCommands;
    }

    public String mixArray(String[] array, String splitter){
        String s = "";
        for (String value : array) s += value + splitter;
        return s.substring(0, s.length() - splitter.length());
    }

    public List<String> replaceVars(String text){
        if(text.equals("{TARGET}"))
            return ImplementedVariables.getOnlinePlayers();
        else if(text.equals("{TOGGLE_TYPES}"))
            return Arrays.asList("cps");
        return new ArrayList<>(Collections.singletonList(text));
    }

    private static class ImplementedVariables {
        public static List<String> getOnlinePlayers(){
            List<String> players = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
            return players;
        }
    }
}
