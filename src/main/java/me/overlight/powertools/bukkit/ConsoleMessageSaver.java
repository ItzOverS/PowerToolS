package me.overlight.powertools.bukkit;

import java.util.ArrayList;
import java.util.List;

public class ConsoleMessageSaver {
    private static List<String> consoleMessages = new ArrayList<>();
    public static String getConsole(){
        String str = "";
        for(String ms: consoleMessages){
            str += ms + "\n";
        }
        return str;
    }
    public static void addConsoleMessage(String msg){
        consoleMessages.add(msg);
    }
}
