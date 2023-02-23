package me.overlight.powertools.spigot.Modules.mods;

import me.overlight.powertools.spigot.Libraries.ColorFormat;
import me.overlight.powertools.spigot.Libraries.InvGen;
import me.overlight.powertools.spigot.Libraries.RepItem;
import me.overlight.powertools.spigot.Libraries.Vote.VoteOption;
import me.overlight.powertools.spigot.Modules.Module;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.Plugin.PlMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class Vote
        extends Module
        implements Listener {
    public Vote() {
        super("Vote", "Create chat vote for target permission or player", "PowerToolS Vote {create/give} [id]", new String[]{});
    }

    HashMap<Player, String> playersCreatingVote = new HashMap<Player, String>();
    public static List<me.overlight.powertools.spigot.Libraries.Vote.Vote> votes = new ArrayList<>();
    public static HashMap<me.overlight.powertools.spigot.Libraries.Vote.Vote, String> voteOwner = new HashMap<>();

    public static void openVoteGUI(Player player) {
        if (player == null) return;
        /*Inventory inv = InvGen.fillInv(
                Bukkit.createInventory(null, 54, PlInfo.INV_PREFIX + ChatColor.AQUA + "Vote Creation"),
                InvGen.generateItem(Material.STAINED_GLASS_PANE, 1, "PowerToolS Vote Creation", null),
                true
        );*/
        me.overlight.powertools.spigot.Libraries.Vote.Vote v = new me.overlight.powertools.spigot.Libraries.Vote.Vote(
                "Test Vote", "ALL", Arrays.asList(
                new VoteOption("Test Vote Option #1", null, 0),
                new VoteOption("Test Vote Option #2", null, 0),
                new VoteOption("Test Vote Option #3", null, 0),
                new VoteOption("Test Vote Option #4", null, 0)
        )
        );
        votes.add(v);
        openVoteCreationInventory(v, player);
    }

    @EventHandler
    public void event(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getItemMeta() == null) return;

        String name = e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase(),
                voteID = e.getView().getTitle().substring(e.getView().getTitle().lastIndexOf(">>>") + 3);
        if (e.getView().getTitle().contains("Vote Creation")) {
            if (name.contains("vote")) {
                e.getWhoClicked().closeInventory();
                e.setCancelled(true);
                if (name.contains("name")) {
                    e.getWhoClicked().sendMessage(PlMessages.Vote_SelectYourVoteName.get());
                    playersCreatingVote.put((Player) e.getWhoClicked(), "EDIT_NAME_" + voteID);
                } else if (name.contains("item")) {
                    e.getWhoClicked().sendMessage(PlMessages.Vote_SelectYourVoteItem.get(new RepItem("%INDEX%", name.substring(name.indexOf("#") + 1))));
                    playersCreatingVote.put((Player) e.getWhoClicked(), "EDIT_ITEM_" + name.substring(name.indexOf("#") + 1) + "_" + voteID);
                } else if (name.contains("permission")) {
                    e.getWhoClicked().sendMessage(PlMessages.Vote_SelectYourVotePermission.get(new RepItem("%INDEX%", name.substring(name.indexOf("#") + 1))));
                    playersCreatingVote.put((Player) e.getWhoClicked(), "EDIT_PERMISSION_" + voteID);
                }
            } else if (name.contains("start voting")) {
                e.setCancelled(true);
                me.overlight.powertools.spigot.Libraries.Vote.Vote v = getVoteById(voteID);
                assert v != null;
                if (v.getOptions().get(0).getText() != null && v.getOptions().get(1).getText() != null) {
                    e.getWhoClicked().sendMessage(PlMessages.Vote_SuccessfulStarted.get(new RepItem("%PERM%", v.getPermission())));
                    v.startVote(e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    openVoteStatsInventory(v, (Player) e.getWhoClicked());
                    voteOwner.put(v, e.getWhoClicked().getName());
                }
            }
        } else if (e.getView().getTitle().contains("Vote Stats")) {
            e.setCancelled(true);
            if (name.contains("stop vote")) {
                me.overlight.powertools.spigot.Libraries.Vote.Vote v = getVoteById(voteID);
                assert v != null;
                e.getWhoClicked().sendMessage(PlMessages.Vote_VoteEnded.get(new RepItem("%VOTERS%", String.valueOf(v.getVoters().size()))));
                votes.remove(v);
                e.getWhoClicked().closeInventory();
                voteOwner.remove(v);
            }
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void event(AsyncPlayerChatEvent e) {
        if (!playersCreatingVote.containsKey(e.getPlayer())) return;
        String action = playersCreatingVote.get(e.getPlayer()).substring(0, playersCreatingVote.get(e.getPlayer()).lastIndexOf("_"));
        long id = Long.parseLong(playersCreatingVote.get(e.getPlayer()).substring(playersCreatingVote.get(e.getPlayer()).lastIndexOf("_") + 1));
        if (action.equals("EDIT_NAME")) {
            e.getPlayer().sendMessage(PlMessages.Vote_SuccessfulUpdateVoteName.get());
            e.setCancelled(true);
            updateVoteName(id, e.getMessage(), e.getPlayer());
        } else if (action.startsWith("EDIT_ITEM_")) {
            int index = Integer.parseInt(action.substring(action.lastIndexOf("_") + 1).substring(0, 1)) - 1;
            e.getPlayer().sendMessage(PlMessages.Vote_SuccessfulUpdateVoteOption.get());
            e.setCancelled(true);
            updateVoteOption(id, new VoteOption(ColorFormat.removeAllFormats(e.getMessage()), ChatColor.translateAlternateColorCodes('&', e.getMessage()), 0), index, e.getPlayer());
        } else if (action.equals("EDIT_PERMISSION")) {
            e.getPlayer().sendMessage(PlMessages.Vote_SuccessfulUpdateVotePermission.get());
            e.setCancelled(true);
            updateVotePermission(id, e.getMessage(), e.getPlayer());
        }
    }

    @EventHandler
    public void event(InventoryCloseEvent e) {
        String voteID = e.getView().getTitle().substring(e.getView().getTitle().lastIndexOf(">>>") + 3);
        if (e.getView().getTitle().contains("Vote Creation")) {
            playersCreatingVote.remove((Player) e.getPlayer());
        } else if (e.getView().getTitle().contains("Vote Stats")) {
            votes.remove(getVoteById(voteID));
            voteOwner.remove(getVoteById(voteID));
        }
    }

    private void updateVoteName(long voteID, String newName, Player player) {
        for (me.overlight.powertools.spigot.Libraries.Vote.Vote vote : votes) {
            if (vote.getId() == voteID) {
                me.overlight.powertools.spigot.Libraries.Vote.Vote v =
                        new me.overlight.powertools.spigot.Libraries.Vote.Vote(
                                newName, vote.getPermission(), Arrays.asList(
                                vote.getOptions().get(0),
                                vote.getOptions().get(1),
                                vote.getOptions().get(2),
                                vote.getOptions().get(3)
                        )
                        );
                votes.remove(vote);
                votes.add(v);
                openVoteCreationInventory(v, player);
            }
        }
    }

    private void updateVotePermission(long voteID, String permission, Player player) {
        for (me.overlight.powertools.spigot.Libraries.Vote.Vote vote : votes) {
            if (vote.getId() == voteID) {
                me.overlight.powertools.spigot.Libraries.Vote.Vote v =
                        new me.overlight.powertools.spigot.Libraries.Vote.Vote(
                                vote.getName(), permission, Arrays.asList(
                                vote.getOptions().get(0),
                                vote.getOptions().get(1),
                                vote.getOptions().get(2),
                                vote.getOptions().get(3)
                        )
                        );
                votes.remove(vote);
                votes.add(v);
                openVoteCreationInventory(v, player);
            }
        }
    }

    private void updateVoteOption(long voteID, VoteOption option, int index, Player player) {
        for (me.overlight.powertools.spigot.Libraries.Vote.Vote vote : votes) {
            if (vote.getId() == voteID) {
                me.overlight.powertools.spigot.Libraries.Vote.Vote v =
                        new me.overlight.powertools.spigot.Libraries.Vote.Vote(
                                vote.getName(), vote.getPermission(), Arrays.asList(
                                (index == 0) ? option : vote.getOptions().get(0),
                                (index == 1) ? option : vote.getOptions().get(1),
                                (index == 2) ? option : vote.getOptions().get(2),
                                (index == 3) ? option : vote.getOptions().get(3)
                        )
                        );
                votes.remove(vote);
                votes.add(v);
                openVoteCreationInventory(v, player);
            }
        }
    }

    private static void openVoteCreationInventory(me.overlight.powertools.spigot.Libraries.Vote.Vote v, Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, PlInfo.INV_PREFIX + ChatColor.AQUA + "Vote Creation                                                                                                                                                                                                                          >>>" + v.getId());
        inv.setItem(10, InvGen.generateItem(Material.ANVIL, 1, ChatColor.RED + "Change vote name: " + v.getName(), null));
        inv.setItem(11, InvGen.generateItem(Material.GOLD_AXE, 1, ChatColor.RED + "Change vote permission: " + v.getPermission(), null));
        inv.setItem(29, InvGen.generateItem((v.getOptions().get(0).getText() == null) ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK, 1, ChatColor.RED + "Vote item #1: " + ((v.getOptions().get(0).getText() == null) ? "None" : v.getOptions().get(0).getText()), null));
        if (v.getOptions().get(0).getText() != null) inv.setItem(30, InvGen.generateItem((v.getOptions().get(1).getText() == null) ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK, 1, ChatColor.RED + "Vote item #2: " + ((v.getOptions().get(1).getText() == null) ? "None" : v.getOptions().get(1).getText()), null));
        if (v.getOptions().get(1).getText() != null) inv.setItem(31, InvGen.generateItem((v.getOptions().get(2).getText() == null) ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK, 1, ChatColor.RED + "Vote item #3: " + ((v.getOptions().get(2).getText() == null) ? "None" : v.getOptions().get(2).getText()), null));
        if (v.getOptions().get(2).getText() != null) inv.setItem(32, InvGen.generateItem((v.getOptions().get(3).getText() == null) ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK, 1, ChatColor.RED + "Vote item #4: " + ((v.getOptions().get(3).getText() == null) ? "None" : v.getOptions().get(3).getText()), null));
        inv.setItem(53, InvGen.generateItem(Material.BOW, 1, ChatColor.RED + "Start Voting", null));
        player.openInventory(inv);
    }

    public static void openVoteStatsInventory(me.overlight.powertools.spigot.Libraries.Vote.Vote v, Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, PlInfo.INV_PREFIX + ChatColor.AQUA + "Vote Stats                                                                                                                                                                                                                          >>>" + v.getId());
        inv.setItem(10, InvGen.generateItem(Material.ANVIL, 1, ChatColor.RED + "vote name: " + v.getName(), null));
        inv.setItem(11, InvGen.generateItem(Material.GOLD_AXE, 1, ChatColor.RED + "vote permission: " + v.getPermission(), null));
        inv.setItem(29, InvGen.generateItem((v.getOptions().get(0).getText() == null) ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK, 1, ChatColor.RED + "Vote item #1: " + ((v.getOptions().get(0).getText() == null) ? "None" : v.getOptions().get(0).getText()) + " : " + v.getOptions().get(0).getRange(), toStringArray(subList(join(v.getOptions().get(0).getVoters(), ", "), 50))));
        if (v.getOptions().get(1).getText() != null) inv.setItem(30, InvGen.generateItem((v.getOptions().get(1).getText() == null) ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK, 1, ChatColor.RED + "Vote item #2: " + ((v.getOptions().get(1).getText() == null) ? "None" : v.getOptions().get(1).getText()) + " : " + v.getOptions().get(1).getRange(), toStringArray(subList(join(v.getOptions().get(1).getVoters(), ", "), 50))));
        if (v.getOptions().get(2).getText() != null) inv.setItem(31, InvGen.generateItem((v.getOptions().get(2).getText() == null) ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK, 1, ChatColor.RED + "Vote item #3: " + ((v.getOptions().get(2).getText() == null) ? "None" : v.getOptions().get(2).getText()) + " : " + v.getOptions().get(2).getRange(), toStringArray(subList(join(v.getOptions().get(2).getVoters(), ", "), 50))));
        if (v.getOptions().get(3).getText() != null) inv.setItem(32, InvGen.generateItem((v.getOptions().get(3).getText() == null) ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK, 1, ChatColor.RED + "Vote item #4: " + ((v.getOptions().get(3).getText() == null) ? "None" : v.getOptions().get(3).getText()) + " : " + v.getOptions().get(3).getRange(), toStringArray(subList(join(v.getOptions().get(3).getVoters(), ", "), 50))));
        inv.setItem(53, InvGen.generateItem(Material.BOW, 1, ChatColor.RED + "Stop Vote", null));
        player.openInventory(inv);
    }

    public static me.overlight.powertools.spigot.Libraries.Vote.Vote getVoteById(String id) {
        for (me.overlight.powertools.spigot.Libraries.Vote.Vote v : votes) {
            if (id.equals(String.valueOf(v.getId()))) {
                return v;
            }
        }
        return null;
    }

    private static String join(List<String> list, String splitter) {
        String main = "";
        for (String s : list) main += s + splitter;
        if (main.equals(""))
            return main;
        return main.substring(0, main.length() - splitter.length());
    }

    private static List<String> subList(String text, int maxLength) {
        List<String> result = new ArrayList<String>();
        while (text.length() > maxLength) {
            result.add(text.substring(0, maxLength));
            text = text.substring(0, maxLength);
        }
        result.add(text);
        return result;
    }

    private static String[] toStringArray(List<String> list) {
        String[] result = new String[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
