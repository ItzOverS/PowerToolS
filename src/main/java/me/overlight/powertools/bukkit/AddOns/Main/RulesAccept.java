package me.overlight.powertools.bukkit.AddOns.Main;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.Plugin.PlInfo;
import me.overlight.powertools.bukkit.PowerTools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RulesAccept
        extends AddOn
        implements Listener {
    public RulesAccept() {
        super("RulesAccept", "1.0", "Make your server rules to players know this server's rules", PowerTools.config.getBoolean("RulesAccept.enabled"));
    }

    List<String> acceptingRules = new ArrayList<String>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void event(PlayerJoinEvent e) {
        if (new ArrayList<String>(YamlConfiguration.loadConfiguration(new File("plugins\\PowerToolS\\JoinedPlayers.yml")).getKeys(true)).contains(e.getPlayer().getName()))
            return;
        org.bukkit.inventory.ItemStack stack;
        List<String> configContent = PowerTools.config.getStringList(this.getName() + ".rules");
        String[] pages = new String[(int)(configContent.size() / 10) + 1];
        int num = 0, pageNum = 0;
        for (String rule : configContent) {
            pages[pageNum] += "\n" + rule;
            num++;
            if (num > 10) {
                num = 0;
                pageNum++;
            }
        }

        TextComponent clickAccept = new TextComponent(ChatColor.GREEN + "Accept"), clickDeny = new TextComponent(ChatColor.RED + "Deny");
        clickAccept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/powertools rules accept"));
        clickDeny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/powertools rules deny"));
        pages[pages.length - 1] = "Do you accept these rules?\n\n" + clickAccept + "      " + clickDeny;
        stack = getBook(PlInfo.PREFIX + ChatColor.DARK_RED + "RULES", PlInfo.PREFIX, pages);
        ItemMeta book = stack.getItemMeta();
        book.setDisplayName(ChatColor.RED + "Server RULES");
        book.setLore(Collections.singletonList(ChatColor.RED + "You should accept this rules to play on this server"));
        stack.setItemMeta(book);
        e.getPlayer().getInventory().setItem(4, stack);
        acceptingRules.add(e.getPlayer().getName());
    }

    @EventHandler
    public void event(PlayerMoveEvent e){
        if(acceptingRules.contains(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }

    public ItemStack getBook(String title, String author, String[] pages){
        /*
        ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, 1);
        Class<?> itemStackk = NMSSupport.getBukkitClass("inventory.CraftItemStack");
        net.minecraft.server.v1_8_R3.ItemStack cis = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("title", title);
        nbt.setString("author", author);
        NBTTagList list = new NBTTagList();
        for(String text: pages){
            list.add(new NBTTagString(text));
        }
        nbt.set("pages", list);
        cis.setTag(nbt);
        stack = CraftItemStack.asBukkitCopy(cis);
        */
        return null;
    }
}
