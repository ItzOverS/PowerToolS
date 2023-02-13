package me.overlight.powertools.spigot.AddOns.Main.Captcha;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.Libraries.PluginFile;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class Captcha
        extends AddOn
        implements Listener {
    public static HashMap<String, Integer> playersCodes = new HashMap<>();
    PluginFile verifiedPlayers;
    CaptchaMode mode;

    public Captcha() {
        super("Captcha", "1.0", "Filter bots from humans", PowerTools.config.getBoolean("Captcha.enabled"));
        try {
            verifiedPlayers = new PluginFile("verifiedPlayers");
        } catch (Exception ex) {
        }
        String text = PowerTools.config.getString(this.getName() + ".AI");
        switch (text.toLowerCase()) {
            case "map":
                mode = CaptchaMode.Map;
                break;
            case "gui":
                mode = CaptchaMode.GuiSelect;
                break;
            case "gui-ab":
                mode = CaptchaMode.GuiSelect_AB;
                break;
        }
    }

    @EventHandler
    public void event(PlayerJoinEvent e) {
        if (verifiedPlayers.getYaml().getKeys(false).contains(e.getPlayer().getName())) return;

        Random random = new Random();
        if (mode == CaptchaMode.Map) {
            MapView map = Bukkit.createMap(e.getPlayer().getWorld());
            map.getRenderers().clear();
            MapViewRenderer renderer = new MapViewRenderer();
            map.addRenderer(renderer);

            ItemStack stack = new ItemStack(Material.MAP, 1);
            e.getPlayer().getInventory().setItem(4, stack);
            e.getPlayer().sendMap(map);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (verifiedPlayers.getYaml().getKeys(false).contains(e.getPlayer().getName())) {
                        cancel();
                        return;
                    }
                    e.getPlayer().sendMessage("Enter in chat, what you see in picture");
                }
            }.runTaskTimer(PowerTools.INSTANCE, 0, 100);
        } else if (mode == CaptchaMode.GuiSelect) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Inventory inv = Bukkit.createInventory(null, 54, PlInfo.PREFIX + generateStringColorCodes(" Captcha"));
                    int num = random.nextInt(54);
                    for (int i = 0; i < 54; i++) {
                        if (num != i)
                            inv.setItem(i, generateRandomizedItemStack("Don't Click This"));
                        else
                            inv.setItem(i, generateRandomizedItemStack("Click This"));
                    }
                    e.getPlayer().openInventory(inv);
                }
            }.runTaskLater(PowerTools.INSTANCE, 5);
        } else if (mode == CaptchaMode.GuiSelect_AB) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Inventory inv = Bukkit.createInventory(null, 54, PlInfo.INV_PREFIX + generateStringColorCodes(" Captcha"));
                    int num = random.nextInt(54);
                    for (int i = 0; i < 54; i++) {
                        if (num != i)
                            inv.setItem(i, generateRandomizedItemStack(generateStringColorCodes("abcefgjmnpqruvwxyz | dolik this | abcefgjmnpqruvwxyz")));
                        else
                            inv.setItem(i, generateRandomizedItemStack(generateStringColorCodes("abdfgjmnopqruvwxyz | cliek this | abdfgjmnopqruvwxyz")));
                    }
                    e.getPlayer().openInventory(inv);
                }
            }.runTaskLater(PowerTools.INSTANCE, 5);
        }
    }

    @EventHandler
    public void event(InventoryOpenEvent e) {
        if (mode == CaptchaMode.Map) {
            e.getPlayer().closeInventory();
        }
    }

    @EventHandler
    public void event(InventoryClickEvent e) {
        if (!e.getClickedInventory().getTitle().startsWith(PlInfo.INV_PREFIX) && !e.getClickedInventory().getTitle().contains("C") &&
                !e.getClickedInventory().getTitle().contains("a") && !e.getClickedInventory().getTitle().contains("p") &&
                !e.getClickedInventory().getTitle().contains("t") && !e.getClickedInventory().getTitle().contains("c") &&
                !e.getClickedInventory().getTitle().contains("h") && !e.getClickedInventory().getTitle().contains("a"))
            return;
        if (verifiedPlayers.getYaml().getKeys(false).contains(e.getWhoClicked().getName())) return;

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        if (mode == CaptchaMode.GuiSelect_AB) {
            String text = e.getCurrentItem().getItemMeta().getDisplayName().split("\\|")[1];
            if (text.contains("c")) {
                player.sendMessage(PlInfo.PREFIX + ChatColor.GREEN + "You has been simplify verified");
                YamlConfiguration yml = verifiedPlayers.getYaml();
                yml.set(player.getName(), "");
                verifiedPlayers.setYaml(yml);
                player.closeInventory();
            } else {
                PowerTools.kick(player, PlInfo.KICK_PREFIX + ChatColor.RED + "Failed to verify");
            }
        } else if (mode == CaptchaMode.GuiSelect) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Click This")) {
                player.sendMessage(PlInfo.PREFIX + ChatColor.GREEN + "You has been simplify verified");
                YamlConfiguration yml = verifiedPlayers.getYaml();
                yml.set(player.getName(), "");
                verifiedPlayers.setYaml(yml);
                player.closeInventory();
            } else {
                PowerTools.kick(player, PlInfo.KICK_PREFIX + ChatColor.RED + "Failed to verify");
            }
        }
    }

    @EventHandler
    public void event(InventoryCloseEvent e) {
        if (mode == CaptchaMode.GuiSelect || mode == CaptchaMode.GuiSelect_AB) {
            if (!e.getInventory().getTitle().startsWith(PlInfo.INV_PREFIX) && !e.getInventory().getTitle().contains("C") &&
                    !e.getInventory().getTitle().contains("a") && !e.getInventory().getTitle().contains("p") &&
                    !e.getInventory().getTitle().contains("t") && !e.getInventory().getTitle().contains("c") &&
                    !e.getInventory().getTitle().contains("h") && !e.getInventory().getTitle().contains("a")) return;
            if (verifiedPlayers.getYaml().getKeys(false).contains(e.getPlayer().getName())) return;

            PowerTools.kick((Player) e.getPlayer(), PlInfo.KICK_PREFIX + ChatColor.RED + "Failed to verify");
        }
    }

    @EventHandler
    public void event(AsyncPlayerChatEvent e) {
        if (mode == CaptchaMode.Map) {
            if (!verifiedPlayers.getYaml().getKeys(false).contains(e.getPlayer().getName())) {
                if (getKey(e.getMessage()) == null &&
                        Objects.equals(getKey(new ArrayList<>(PowerTools.config.getConfigurationSection("Captcha.MapsLink").getKeys(false)).get(playersCodes.get(e.getPlayer().getName()))), e.getMessage())) {
                    try {
                        verifiedPlayers.insertItem(e.getPlayer().getName(), "").saveYaml();
                    } catch (IOException ignored) {
                    }
                    e.getPlayer().closeInventory();
                }
            }
        }
    }

    public enum CaptchaMode {
        Map,
        GuiSelect,
        GuiSelect_AB
    }

    private String getKey(String value) {
        List<String> items = new ArrayList<>(PowerTools.config.getConfigurationSection("Captcha.MapsLink").getKeys(false));
        for (String m : items) {
            if (m.equals(value))
                return m;
        }
        return null;
    }

    private ItemStack generateRandomizedItemStack(String text) {
        Random random = new Random();
        Material material = Material.values()[random.nextInt(Material.values().length)];
        while (material.isBlock()) material = Material.values()[random.nextInt(Material.values().length)];
        int amount = random.nextInt(64) + 1;
        ItemStack stack = new ItemStack(material, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', text));
        meta.setLore(null);
        stack.setItemMeta(meta);
        return stack;
    }

    private String generateStringColorCodes(String text) {
        String t = "";
        for (char c : text.toCharArray()) {
            t += generateRandomFormat() + generateRandomColor() + c + "\uF8FF";
        }
        return t;
    }

    private String generateRandomColor() {
        Random random = new Random();
        if (random.nextInt(2) == 0)
            return ChatColor.translateAlternateColorCodes('&', "&" + new String[]{"a", "b", "c", "d", "e", "f"}[random.nextInt(6)]);
        else
            return ChatColor.translateAlternateColorCodes('&', "&" + random.nextInt(10));
    }

    private String generateRandomFormat() {
        Random random = new Random();
        return ChatColor.translateAlternateColorCodes('&', "&" + new String[]{"l", "o", "m", "n", "r"}[random.nextInt(5)]);
    }
}
