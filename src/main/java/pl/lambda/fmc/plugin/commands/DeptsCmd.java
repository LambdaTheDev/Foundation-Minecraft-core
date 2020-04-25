package pl.lambda.fmc.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.lambda.fmc.plugin.listeners.OnPlayerChat;
import pl.lambda.fmc.plugin.listeners.OnPlayerJoin;
import pl.lambda.fmc.utils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DeptsCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(command.getName().equalsIgnoreCase("depts"))
        {
            //<editor-fold desc="non-related stuff">
            if(!(sender instanceof Player)) return false;
            Player p = (Player) sender;
            UsersAPI usersAPI = new UsersAPI();
            RoleAPI roleAPI = new RoleAPI();
            ConfigAPI configAPI = new ConfigAPI();
            Inventory currentgui;

            if(args.length == 0)
            {
                openGUI(p, createMainMenu());
                return true;
            }
            //</editor-fold>

            else if(args.length == 1)
            {
                //<editor-fold desc="Discord & choose">
                if(args[0].equalsIgnoreCase("discord"))
                {
                    String link = configAPI.getServerDiscordlink();
                    if(link != null)
                    {
                        p.sendMessage(ChatColor.GREEN + "Our Discord server is: " + ChatColor.BLUE + link + ChatColor.GREEN + "!");
                        return true;
                    }
                    p.sendMessage(ChatColor.DARK_RED + "Issue! Discord link not set/is invalid. Report it to staff member!");
                    return false;
                }

                else if(args[0].equalsIgnoreCase("choose"))
                {
                    List<String> playerRoles = usersAPI.getRoles(p.getUniqueId().toString());
                    currentgui = Bukkit.createInventory(null, 27, "Choose department");

                    for(String playerRole : playerRoles)
                    {
                        if(!roleAPI.roleExist(playerRole))
                        {
                            usersAPI.deleteRoles(p.getUniqueId().toString(), playerRole);
                            continue;
                        }

                        char color = roleAPI.getColor(playerRole);
                        String name = roleAPI.getName(playerRole);
                        ItemStack item = Utils.createWool(Utils.getDyeFromColor(color), name);
                        currentgui.addItem(item);
                    }

                    openGUI(p, currentgui);
                    return true;
                }
                //</editor-fold>
                else
                {
                    openGUI(p, createMainMenu());
                    return false;
                }
            }

            //<editor-fold desc="args length 2">
            else if(args.length == 2)
            {
                if(args[0].equalsIgnoreCase("set"))
                {
                    if(args[1].isEmpty())
                    {
                        openGUI(p, createMainMenu());
                        return false;
                    }

                    List<String> playerRoles = usersAPI.getRoles(p.getUniqueId().toString());
                    if(playerRoles.contains(args[1]))
                    {
                        if(!roleAPI.roleExist(args[1]))
                        {
                            openGUI(p, createMainMenu());
                            return false;
                        }

                        usersAPI.setCurrentRole(p.getUniqueId().toString(), args[1]);
                        p.sendMessage(ChatColor.GREEN + "Your role is now: " + roleAPI.getName(args[1]) + "!");
                        return true;
                    }
                    else
                    {
                        openGUI(p, createMainMenu());
                        return false;
                    }
                }
                else
                {
                    openGUI(p, createMainMenu());
                    return false;
                }
            }
            //</editor-fold>
            else
            {
                openGUI(p, createMainMenu());
                return false;
            }
        }
        return false;
    }

    //<editor-fold desc="Useless functions">
    private void openGUI(Player p, Inventory inv)
    {
        p.closeInventory();
        p.openInventory(inv);
    }

    private Inventory createMainMenu()
    {
        Inventory mainmenu = Bukkit.createInventory(null, 9, "Main menu");
        mainmenu.setItem(1, Utils.createWool(DyeColor.GREEN, "Set department"));
        mainmenu.setItem(4, Utils.createWool(DyeColor.RED, "Your balance"));
        mainmenu.setItem(7, Utils.createWool(DyeColor.BLUE, "Our Discord"));
        return mainmenu;
    }
    //</editor-fold>

    public static final class GUI implements Listener
    {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent e)
        {
            //<editor-fold desc="Not-applicaitions stuff">
            ItemStack clicked = e.getCurrentItem();
            String open = e.getView().getTitle();
            String itemname;
            try
            {
                itemname = e.getCurrentItem().getItemMeta().getDisplayName();
            }
            catch (NullPointerException ex)
            {
                return;
            }
            Player p = (Player) e.getWhoClicked();

            UsersAPI usersAPI = new UsersAPI();
            RoleAPI roleAPI = new RoleAPI();

            if(open.equalsIgnoreCase("main menu"))
            {
                e.setCancelled(true);
                p.closeInventory();

                if(itemname.equalsIgnoreCase("set department"))
                {
                    Bukkit.dispatchCommand(p, "depts choose");
                }

                else if(itemname.equalsIgnoreCase("Your balance"))
                {
                    Bukkit.dispatchCommand(p, "money");
                }

                else if(itemname.equalsIgnoreCase("our discord"))
                {
                    Bukkit.dispatchCommand(p, "depts discord");
                }
            }

            else if(open.equalsIgnoreCase("choose department"))
            {
                e.setCancelled(true);
                p.closeInventory();

                List<String> userClasses = usersAPI.getRoles(p.getUniqueId().toString());
                List<String> classnames = new ArrayList<>();
                for (String classid : userClasses)
                {
                    if (!roleAPI.roleExist(classid))
                    {
                        usersAPI.deleteRoles(p.getUniqueId().toString(), classid);
                        continue;
                    }
                    classnames.add(roleAPI.getName(classid));
                }

                if (classnames.contains(clicked.getItemMeta().getDisplayName()))
                {
                    String classid = roleAPI.getRoleUUIDByName(clicked.getItemMeta().getDisplayName());
                    if (!classid.isEmpty())
                    {
                        Location l = roleAPI.getLocation(classid);
                        if(l == null) l = p.getLocation();
                        usersAPI.setCurrentRole(p.getUniqueId().toString(), classid);
                        p.teleport(l);
                        p.setPlayerListName(Utils.getChatColor(roleAPI.getColor(classid)) + roleAPI.getName(classid));
                        p.sendMessage(ChatColor.GREEN + "Class choosen: " + roleAPI.getName(classid) + "!");

                        char chatColor = roleAPI.getColor(classid);
                        String shortcut = roleAPI.getShortcut(classid);
                        ChatColor color = OnPlayerChat.getColor(chatColor);

                        p.setPlayerListName(color + "[" + shortcut + "] " + ChatColor.RESET + p.getName());
                    }
                }
            }
            //</editor-fold>

        }
    }
}
