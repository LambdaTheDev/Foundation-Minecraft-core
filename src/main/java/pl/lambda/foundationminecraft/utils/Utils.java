package pl.lambda.foundationminecraft.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.Random;

public class Utils
{
    public static String randomString(int length)
    {

        int leftLimit = 97;
        int rightLimit = 122;
        Random random = new Random();

        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++)
        {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }

    public static boolean validColor(String code)
    {
        if(code.length() != 1) return false;
        return code.matches("-?[0-9a-fA-F]+");
    }

    public static String stringBuilder(String[] args, int countFromArg)
    {
        String msg = "";
        for (int i = countFromArg; i < args.length; i++)
        {
            msg = msg + args[i];
            if (i <= args.length - 2) {
                msg = msg + " ";
            }
        }
        return msg;
    }

    public static ItemStack createWool(DyeColor dc, String name)
    {
        ItemStack i = (new Wool(dc)).toItemStack(1);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        i.setItemMeta(im);
        return i;
    }

    public static char randomHexChar()
    {
        char result;
        Random r = new Random();
        int random = r.nextInt(17);

        switch (random)
        {
            case 1:
                result = '1';
                break;
            case 2:
                result = '2';
                break;
            case 3:
                result = '3';
                break;
            case 4:
                result = '4';
                break;
            case 5:
                result = '5';
                break;
            case 6:
                result = '6';
                break;
            case 7:
                result = '7';
                break;
            case 8:
                result = '8';
                break;
            case 9:
                result = '9';
                break;
            case 10:
                result = 'a';
                break;
            case 11:
                result = 'b';
                break;
            case 12:
                result = 'c';
                break;
            case 13:
                result = 'd';
                break;
            case 14:
                result = 'e';
                break;
            case 15:
                result = 'f';
                break;
            default:
                result = '0';
                break;
        }
        return result;
    }

    public static ChatColor getChatColor(char color) {
        switch (color) {
            case '0':
                return ChatColor.BLACK;
            case '1':
                return ChatColor.DARK_BLUE;
            case '2':
                return ChatColor.DARK_GREEN;
            case '3':
                return ChatColor.DARK_AQUA;
            case '4':
                return ChatColor.DARK_RED;
            case '5':
                return ChatColor.DARK_PURPLE;
            case '6':
                return ChatColor.GOLD;
            case '7':
                return ChatColor.GRAY;
            case '8':
                return ChatColor.DARK_GRAY;
            case '9':
                return ChatColor.BLUE;
            case 'a':
                return ChatColor.GREEN;
            case 'b':
                return ChatColor.AQUA;
            case 'c':
                return ChatColor.RED;
            case 'd':
                return ChatColor.LIGHT_PURPLE;
            case 'e':
                return ChatColor.YELLOW;
            case 'f':
                return ChatColor.WHITE;
        }
        return ChatColor.WHITE;
    }



    public static DyeColor getDyeFromColor(char color) {
        switch (color) {
            case 'a':
                return DyeColor.LIME;
            case 'b':
            case '3':
                return DyeColor.LIGHT_BLUE;
            case 'c':
            case '4':
                return DyeColor.RED;
            case 'd':
                return DyeColor.PINK;
            case 'e':
            case '6':
                return DyeColor.YELLOW;
            case 'f':
                return DyeColor.WHITE;
            case '0':
                return DyeColor.BLACK;
            case '1':
            case '9':
                return DyeColor.BLUE;
            case '2':
                return DyeColor.GREEN;
            case '5':
                return DyeColor.MAGENTA;
            case '7':
            case '8':
                return DyeColor.GRAY;
        }
        return DyeColor.WHITE;
    }
}
