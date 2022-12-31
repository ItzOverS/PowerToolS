package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class ChatManager 
        extends AddOn 
        implements Listener {
    public ChatManager(boolean stats) {
        super("ChatManager", "2.0", "Manager players chat", "NONE", stats);
    }

    public static List<String> DelayMessagePlayers = new ArrayList<>();
    public static HashMap<String, String> LastMSG = new HashMap<>();
    public static HashMap<String, Integer> SpamAmount = new HashMap<>();
    public static List<String> PlayersCommandCooldown = new ArrayList<>();
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerChat(AsyncPlayerChatEvent e) {
        if (this.isEnabled()) {
            String message = e.getMessage().toLowerCase();
            Player sender = e.getPlayer();
            if (config.getBoolean(this.getName() + ".MessageDelay.enabled")) {
                if (!DelayMessagePlayers.contains(sender.getName())) {
                    DelayMessagePlayers.add(sender.getName());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                        DelayMessagePlayers.remove(sender.getName());
                    }, config.getInt(this.getName() + ".MessageDelay.delay") * 20L);
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString(this.getName() + ".MessageDelay.msg")));
                    e.setCancelled(true);
                    return;
                }
            }
            if (config.getBoolean(this.getName() + ".AntiSpam.enabled")) {
                if (getDuplicates(message, LastMSG.get(e.getPlayer().getName())) > new HashSet<>(Arrays.asList(removeSymbols(message.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}).split(" "))).size() / 2) {
                    SpamAmount.put(sender.getName(), SpamAmount.containsKey(sender.getName()) ? SpamAmount.get(sender.getName()) + 1 : 1);
                } else {
                    SpamAmount.put(sender.getName(), 0);
                    LastMSG.put(sender.getName(), message);
                }
                if (SpamAmount.get(sender.getName()) >= config.getInt(this.getName() + ".AntiSpam.maxSpam")) {
                    if (config.getBoolean(this.getName() + ".AntiSpam.Kick.enabled")) {
                        e.setCancelled(true);
                        Bukkit.getPlayer(sender.getName()).kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getString(this.getName() + ".AntiSpam.Kick.msg")));
                        return;
                    }
                }
            }
            if (config.getBoolean(this.getName() + ".WordBlock.enabled")) {
                boolean messageFlagged = false;
                //------------------------------------>  AI 01:    Random char, splitter
                if (config.getBoolean(this.getName() + ".WordBlock.Modes.Splitters") || config.getBoolean(this.getName() + ".WordBlock.Modes.MultiLetter")) {
                    String msg = removeSymbols(message.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"});
                    for(String text: msg.split(" ")){
                        String tex = "";
                        int index = 0;
                        for(char ch: text.toCharArray()){
                            if(ch == text.charAt(index)) {
                                tex += ch;
                                index++;
                            }
                            if(config.getStringList(this.getName() + ".WordBlock.Words").contains(tex)){
                                messageFlagged = true;
                                break;
                            }
                        }
                    }
                    if(!messageFlagged){
                        msg = msg.replace(" ", "");
                        for(String str: config.getStringList(this.getName() + ".WordBlock.Words")){
                            if(msg.contains(str)){
                                messageFlagged = true;
                                break;
                            }
                        }
                    }
                }
                //------------------------------------>  AI 02:    UniCodes
                if (config.getBoolean(this.getName() + ".WordBlock.Modes.UniCodes")) {
                    for (String word : config.getStringList(this.getName() + ".WordBlock.Words")) {
                        String generatedText = "";
                        int index = 0;
                        for (char chr : message.toCharArray()) {
                            if (DeUniCodedChar(chr) == word.charAt(index)) {
                                generatedText += DeUniCodedChar(chr);
                                index++;
                            }
                            if (generatedText.length() == word.length())
                                break;
                        }
                        if (generatedText.equals(word)) {
                            messageFlagged = true;
                            break;
                        }
                    }
                }
                if (messageFlagged) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString(this.getName() + ".WordBlock.msg")));
                    e.setCancelled(true);
                    return;
                }
            }
            if (config.getBoolean(this.getName() + ".AntiDuplicate.enabled")) {
                String newMSG = removeSymbols(message.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"});
                String[] Words = newMSG.split(" ");
                Set<String> words = new HashSet<>(Arrays.asList(Words));
                for (String str : words) {
                    if (Count(newMSG, str) > config.getLong(this.getName() + ".AntiDuplicate.maxDuplicate")) {
                        e.setCancelled(true);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString(this.getName() + ".AntiDuplicate.msg")));
                        return;
                    }
                }
            }
            if (config.getBoolean(this.getName() + ".PlayerMentions.enabled")) {
                if (ContainsNameOnText(message) != null) {
                    String mode = config.getString(this.getName() + ".PlayerMentions.Mode");
                    List<String> mentionedPlayers = ContainsNameOnText(message);
                    assert mentionedPlayers != null;
                    for (String name : mentionedPlayers) {
                        if (Objects.equals(mode, "title"))
                            Bukkit.getPlayer(name).sendTitle(ChatColor.GOLD + sender.getName(), ChatColor.RED + "Has mention you");
                        if (Objects.equals(mode, "chat"))
                            Bukkit.getPlayer(name).sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.RED + " has mention you");
                        if (Objects.equals(mode, "both")) {
                            Bukkit.getPlayer(name).sendTitle(ChatColor.GOLD + sender.getName(), ChatColor.RED + "Has mention you");
                            Bukkit.getPlayer(name).sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.RED + " has mention you");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerCommandExecute(PlayerCommandPreprocessEvent e){
        if(this.isEnabled()){
            if(config.getBoolean("ChatManager.CommandCooldown.enabled")){
                if(PlayersCommandCooldown.contains(e.getPlayer().getName())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "Please wait before send another command!");
                } else{
                    e.setCancelled(false);
                    PlayersCommandCooldown.add(e.getPlayer().getName());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                        PlayersCommandCooldown.remove(e.getPlayer().getName());
                    }, config.getInt("ChatManager.CommandCooldown.delay") * 20L);
                }
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e){
        SpamAmount.remove(e.getPlayer().getName());
        LastMSG.remove(e.getPlayer().getName());
    }

    private static List<String> ContainsNameOnText(String text) {
        List<String> mentionedPlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (text.contains(player.getName()))
                mentionedPlayers.add(player.getName());
        }
        if (mentionedPlayers.size() == 0)
            return null;
        return mentionedPlayers;
    }

    private static Long Count(String text, String charector) {
        long amount = 0;
        for (int i = 0; i < text.length() - charector.length(); i++) {
            if (text.startsWith(charector, i))
                amount++;
        }
        return amount;
    }

    private static String removeSymbols(String text, String[] Symbols) {
        String newText = text;
        for (String symbol : Symbols) {
            newText = newText.replace(symbol, "");
        }
        return newText;
    }

    private static char DeUniCodedChar(char targ) {
        if (targ > 65281 && targ < 65376) {
            return ((char) (targ - 65248));
        }
        return targ;
    }

    private static int getDuplicates(String message, String lastMessage){
        if(message == null || message.equals("")) return 0;
        if(lastMessage == null || lastMessage.equals("")) return 0;
        Set<String> Message = new HashSet<>(Arrays.asList(removeSymbols(message.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}).split(" ")));
        Set<String> LastMessage = new HashSet<>(Arrays.asList(removeSymbols(lastMessage.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}).split(" ")));
        List<String> duplicatesList = new ArrayList<>();
        int duplicates = 0;
        for(String msg: Message){
            for(String lastMsg: LastMessage){
                if(duplicatesList.contains(msg + " - " + lastMsg)) continue;
                if(lastMsg.equals(msg)) { duplicates++; duplicatesList.add(msg + " - " + lastMessage); }
            }
        }
        return duplicates;
    }
}
