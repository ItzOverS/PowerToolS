package me.overlight.powertools.bukkit.AddOns.Main;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.Libraries.Infinite;
import me.overlight.powertools.bukkit.Plugin.PlMessages;
import me.overlight.powertools.bukkit.PowerTools;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChatManager
        extends AddOn
        implements Listener {
    public ChatManager() {
        super("ChatManager", "2.0", "Manager players chat", PowerTools.config.getBoolean("ChatManager.enabled"));
    }

    public static List<String> DelayMessagePlayers = new ArrayList<>();
    public static HashMap<String, String> LastMSG = new HashMap<>();
    public static HashMap<String, Infinite<Long>> LastMSGMs = new HashMap<>();

    public static HashMap<String, Integer> SpamAmount = new HashMap<>();
    public static List<String> PlayersCommandCooldown = new ArrayList<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerChat(AsyncPlayerChatEvent e) {
        if (!LastMSGMs.containsKey(e.getPlayer().getName())) {
            LastMSGMs.put(e.getPlayer().getName(), new Infinite<>(5L, 4L));
        }
        String message = e.getMessage().toLowerCase();
        Player sender = e.getPlayer();
        if (PowerTools.config.getBoolean(this.getName() + ".MessageDelay.enabled")) {
            if (!DelayMessagePlayers.contains(sender.getName())) {
                DelayMessagePlayers.add(sender.getName());
                Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                    DelayMessagePlayers.remove(sender.getName());
                }, PowerTools.config.getInt(this.getName() + ".MessageDelay.delay") * 20L);
            } else {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PlMessages.ChatManager_MessagesDelay.get(e.getPlayer())));
                else
                    sender.sendMessage(PlMessages.ChatManager_MessagesDelay.get());
                e.setCancelled(true);
                return;
            }
        }
        if (PowerTools.config.getBoolean(this.getName() + ".AntiSpam.enabled")) {
            if (LastMSGMs.get(sender.getName()).isFullEquals() || getDuplicates(message, LastMSG.get(e.getPlayer().getName())) > new HashSet<>(Arrays.asList(removeSymbols(message.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}).split(" "))).size() / 2) {
                SpamAmount.put(sender.getName(), SpamAmount.containsKey(sender.getName()) ? SpamAmount.get(sender.getName()) + 1 : 1);
            } else {
                SpamAmount.put(sender.getName(), 0);
                LastMSG.put(sender.getName(), message);
                LastMSGMs.put(sender.getName(), new Infinite<Long>(4, LastMSGMs.get(sender.getName()), Collections.singletonList(System.currentTimeMillis())));
            }
            if (SpamAmount.get(sender.getName()) >= PowerTools.config.getInt(this.getName() + ".AntiSpam.maxSpam")) {
                if (PowerTools.config.getBoolean(this.getName() + ".AntiSpam.Kick.enabled")) {
                    e.setCancelled(true);
                    Bukkit.getScheduler().runTask(PowerTools.INSTANCE, () -> {
                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                            PowerTools.kick(sender, PlMessages.ChatManager_AntiSpam.get(e.getPlayer()));
                        else
                            PowerTools.kick(sender, PlMessages.ChatManager_AntiSpam.get());
                    });
                    return;
                }
            }
        }
        if (PowerTools.config.getBoolean(this.getName() + ".WordBlock.enabled")) {
            boolean messageFlagged = false;
            //------------------------------------>  AI 01:    Random char, splitter
            if (PowerTools.config.getBoolean(this.getName() + ".WordBlock.Modes.Splitters") || PowerTools.config.getBoolean(this.getName() + ".WordBlock.Modes.MultiLetter")) {
                String msg = removeSymbols(message.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"});
                for (String text : msg.split(" ")) {
                    String tex = "";
                    int index = 0;
                    for (char ch : text.toCharArray()) {
                        if (ch == text.charAt(index)) {
                            tex += ch;
                            index++;
                        }
                        if (PowerTools.config.getStringList(this.getName() + ".WordBlock.Words").contains(tex)) {
                            messageFlagged = true;
                            break;
                        }
                    }
                }
                if (!messageFlagged) {
                    msg = msg.replace(" ", "");
                    for (String str : PowerTools.config.getStringList(this.getName() + ".WordBlock.Words")) {
                        if (msg.contains(str)) {
                            messageFlagged = true;
                            break;
                        }
                    }
                }
            }
            //------------------------------------>  AI 02:    UniCodes
            if (PowerTools.config.getBoolean(this.getName() + ".WordBlock.Modes.UniCodes")) {
                for (String word : PowerTools.config.getStringList(this.getName() + ".WordBlock.Words")) {
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
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                    sender.sendMessage(PlMessages.ChatManager_BadWordDetected.get(e.getPlayer()));
                else
                    sender.sendMessage(PlMessages.ChatManager_BadWordDetected.get());
                e.setCancelled(true);
                return;
            }
        }
        if (PowerTools.config.getBoolean(this.getName() + ".AntiDuplicate.enabled")) {
            String newMSG = removeSymbols(message.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"});
            String[] Words = newMSG.split(" ");
            Set<String> words = new HashSet<>(Arrays.asList(Words));
            for (String str : words) {
                if (Count(newMSG, str) > PowerTools.config.getLong(this.getName() + ".AntiDuplicate.maxDuplicate")) {
                    e.setCancelled(true);
                    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                        sender.sendMessage(PlMessages.ChatManager_DuplicateWord.get(e.getPlayer()));
                    else
                        sender.sendMessage(PlMessages.ChatManager_DuplicateWord.get());
                    return;
                }
            }
        }
        if (PowerTools.config.getBoolean(this.getName() + ".PlayerMentions.enabled")) {
            if (ContainsNameOnText(message) != null) {
                String mode = PowerTools.config.getString(this.getName() + ".PlayerMentions.Mode");
                List<String> mentionedPlayers = ContainsNameOnText(message);
                assert mentionedPlayers != null;
                for (String name : mentionedPlayers) {
                    if (Objects.equals(mode, "title"))
                        Bukkit.getPlayer(name).sendTitle(ChatColor.GOLD + sender.getName(), ChatColor.RED + "Has mentioned you");
                    if (Objects.equals(mode, "chat"))
                        Bukkit.getPlayer(name).sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.RED + " has mentioned you");
                    if (Objects.equals(mode, "both")) {
                        Bukkit.getPlayer(name).sendTitle(ChatColor.GOLD + sender.getName(), ChatColor.RED + "Has mentioned you");
                        Bukkit.getPlayer(name).sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.RED + " has mentioned you");
                    }
                }
            }
        }
        if (PowerTools.config.getBoolean(this.getName() + ".AntiAdventure.enabled")) {
            for (String n : message.split(" ")) {
                if (n.contains(".") && n.length() > 4 && message.split("\\.")[0].length() > 2 && message.split("\\.")[1].length() > 2) {
                    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                        sender.sendMessage(PlMessages.ChatManager_AntiAdventure.get(e.getPlayer()));
                    else sender.sendMessage(PlMessages.ChatManager_AntiAdventure.get());
                }
            }
        }
        if (PowerTools.config.getBoolean(this.getName() + ".Item2Chat.enabled")) {
            if (e.getMessage().contains(PowerTools.config.getString(this.getName() + ".Item2Chat.chatPlaceHolder"))) {
                ItemStack item = e.getPlayer().getItemInHand();
                if (item != null) {
                    TextComponent component = new TextComponent("");
                    component.addExtra(item.getItemMeta().getDisplayName());
                    component.addExtra("\n");
                    if (item.getItemMeta().getLore() != null) {
                        for (String line : item.getItemMeta().getLore()) {
                            component.addExtra(line + "\n");
                        }
                    }
                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ComponentBuilder("{\"id\":\"" + CraftItemStack.asNMSCopy(item).getTag() + "\"}").create()));
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.spigot().sendMessage(component);
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void playerCommandExecute(PlayerCommandPreprocessEvent e) {
        if (this.isEnabled()) {
            if (PowerTools.config.getBoolean("ChatManager.CommandCooldown.enabled")) {
                if (PlayersCommandCooldown.contains(e.getPlayer().getName())) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "Please wait before send another command!");
                } else {
                    e.setCancelled(false);
                    PlayersCommandCooldown.add(e.getPlayer().getName());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                        PlayersCommandCooldown.remove(e.getPlayer().getName());
                    }, PowerTools.config.getInt("ChatManager.CommandCooldown.delay") * 20L);
                }
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        SpamAmount.remove(e.getPlayer().getName());
        LastMSG.remove(e.getPlayer().getName());
    }

    private static List<String> ContainsNameOnText(String text) {
        List<String> mentionedPlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (text.contains(player.getName()))
                mentionedPlayers.add(player.getName());
        }
        if (mentionedPlayers.isEmpty())
            return null;
        return mentionedPlayers;
    }

    public static Long Count(String text, String charector) {
        long amount = 0;
        for (int i = 0; i < text.length() - charector.length(); i++) {
            if (text.startsWith(charector, i))
                amount++;
        }
        return amount;
    }

    public static String removeSymbols(String text, String[] Symbols) {
        String newText = text;
        for (String symbol : Symbols) {
            newText = newText.replace(symbol, "");
        }
        return newText;
    }

    private static char DeUniCodedChar(char target) {
        if (target > 65281 && target < 65376) {
            return ((char) (target - 65248));
        }
        return target;
    }

    private static int getDuplicates(String message, String lastMessage) {
        if (message == null || message.equals("")) return 0;
        if (lastMessage == null || lastMessage.equals("")) return 0;
        Set<String> Message = new HashSet<>(Arrays.asList(removeSymbols(message.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}).split(" ")));
        Set<String> LastMessage = new HashSet<>(Arrays.asList(removeSymbols(lastMessage.toLowerCase(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}).split(" ")));
        List<String> duplicatesList = new ArrayList<>();
        int duplicates = 0;
        for (String msg : Message) {
            for (String lastMsg : LastMessage) {
                if (duplicatesList.contains(msg + " - " + lastMsg)) continue;
                if (lastMsg.equals(msg)) {
                    duplicates++;
                    duplicatesList.add(msg + " - " + lastMessage);
                }
            }
        }
        return duplicates;
    }
}
