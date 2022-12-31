package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Libraries.WebHooks.DiscordAPI;
import me.overlight.powertools.PowerTools;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class UserNameManager 
        extends AddOn
        implements Listener {

    public UserNameManager() {
        super("UsernameManager", "1.0", "Manager players usernames", "NONE", PowerTools.config.getBoolean("UsernameManager.enabled"));
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerJoinEvent e){
        if(this.isEnabled()) {
            if (PowerTools.config.getBoolean( this.getName() + ".WordBlock.enabled")) {
                String message = e.getPlayer().getName().toLowerCase();
                boolean messageFlagged = false;
                //------------------------------------>  AI 01:    Random char, splitter
                if (PowerTools.config.getBoolean(this.getName() + ".WordBlock.Modes.Splitters") || PowerTools.config.getBoolean(this.getName() + ".WordBlock.Modes.MultiLetter")) {
                    for (String word : PowerTools.config.getStringList(this.getName() + ".WordBlock.Words")) {
                        String generatedText = "";
                        int index = 0;
                        for (char chr : message.toCharArray()) {
                            if (chr == word.charAt(index)) {
                                generatedText += chr;
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
                    if(Objects.equals(PowerTools.config.getString(this.getName() + ".WordBlock.Action"), "BAN")){
                        if(PowerTools.config.getBoolean(this.getName() + ".alert-on-discord"))
                            DiscordAPI.sendEmbedOnWebhook(e.getPlayer().getName() + " banned - BadName", e.getPlayer().getName() + " has banned for contains bad words");
                        Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(), ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(PowerTools.config.getString(this.getName() + ".WordBlock.msg"))), null, null);
                        e.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(PowerTools.config.getString(this.getName() + ".WordBlock.msg"))));
                    } else if(Objects.equals(PowerTools.config.getString(this.getName() + ".WordBlock.Action"), "KICK")){
                        e.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(PowerTools.config.getString(this.getName() + ".WordBlock.msg"))));
                        if(PowerTools.config.getBoolean(this.getName() + ".alert-on-discord"))
                            DiscordAPI.sendEmbedOnWebhook(e.getPlayer().getName() + " kicked - BadName", e.getPlayer().getName() + " has kicked for contains bad words");
                    }
                }
            }
        }
    }
}
