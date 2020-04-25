package pl.ytskagamer.fmc.plugin.commands;

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
import pl.ytskagamer.fmc.utils.*;

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
            DepartmentApplicationAPI departmentApplicationAPI = new DepartmentApplicationAPI();
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

                else if(args[0].equalsIgnoreCase("apply"))
                {
                    int applicationid = departmentApplicationAPI.getTempCacheApplicationID(p.getUniqueId().toString());
                    if(applicationid == 0)
                    {
                        //Here pick application
                        System.out.println("Application ID is null!");
                        currentgui = Bukkit.createInventory(null, 27, "Apply to department");
                        int playerClearance = usersAPI.getClearance(p.getUniqueId().toString());
                        List<String> applications = departmentApplicationAPI.getApplicationsByClearance(playerClearance);

                        for (String application : applications)
                        {
                            String departmentuuid = departmentApplicationAPI.getDepartmentUUID(application);
                            char color = roleAPI.getColor(departmentuuid);
                            ItemStack item = Utils.createWool(Utils.getDyeFromColor(color), roleAPI.getName(departmentuuid));
                            currentgui.addItem(item);
                        }

                        openGUI(p, currentgui);
                        System.out.println("DOSED");
                        return false;
                    }
                    else
                    {
                        System.out.println("Application ID is set!");

                        String string_applicationid = String.valueOf(applicationid);
                        HashMap<Integer, String> questions = departmentApplicationAPI.getQuestions(string_applicationid);
                        int step = departmentApplicationAPI.getTempCacheStep(p.getUniqueId().toString());
                        String question = questions.get(step);
                        currentgui = Bukkit.createInventory(null, 27, "> " + question);

                        List<String> answers = new ArrayList<>(departmentApplicationAPI.getCorrectAnswers(string_applicationid, question));
                        answers.addAll(departmentApplicationAPI.getIncorrectAnswers(string_applicationid, question));
                        Collections.shuffle(answers);

                        for(String answer : answers)
                        {
                            char color = Utils.randomHexChar();
                            ItemStack item = Utils.createWool(Utils.getDyeFromColor(color), answer);
                            currentgui.addItem(item);
                        }

                        openGUI(p, currentgui);
                        return false;
                    }
                }
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
        mainmenu.setItem(4, Utils.createWool(DyeColor.RED, "Apply to department"));
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
            DepartmentApplicationAPI departmentApplicationAPI = new DepartmentApplicationAPI();

            if(open.equalsIgnoreCase("main menu"))
            {
                e.setCancelled(true);
                p.closeInventory();

                if(itemname.equalsIgnoreCase("set department"))
                {
                    Bukkit.dispatchCommand(p, "depts choose");
                }

                else if(itemname.equalsIgnoreCase("apply to department"))
                {
                    Bukkit.dispatchCommand(p, "depts apply");
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
                    }
                }
            }
            //</editor-fold>

            else if(open.equalsIgnoreCase("apply to department"))
            {
                e.setCancelled(true);
                p.closeInventory();

                List<String> applications = departmentApplicationAPI.getApplicationsByClearance(usersAPI.getClearance(p.getUniqueId().toString()));

                for (String application : applications)
                {
                    String departmentuuid = departmentApplicationAPI.getDepartmentUUID(application);
                    String departmentName = roleAPI.getName(departmentuuid);

                    if(itemname.equalsIgnoreCase(departmentName))
                    {
                        boolean starting = departmentApplicationAPI.startApplication(p.getUniqueId().toString(), application);
                        if(!starting)
                        {
                            p.sendMessage(ChatColor.RED + "Unable to start application! You are allowed to apply ONCE PER DAY! If you think that's an error, contact staff!");
                            return;
                        }
                        Bukkit.dispatchCommand(p, "depts apply");
                    }
                }
            }

            else if(open.startsWith("> "))
            {
                open = open.replace("> ", "");
            }
            //todo - picking applications AND answers
        }
    }
}
