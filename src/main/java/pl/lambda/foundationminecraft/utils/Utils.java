package pl.lambda.foundationminecraft.utils;

import org.bukkit.DyeColor;
import org.bukkit.material.Dye;

import java.util.Random;
import java.util.regex.Pattern;

public class Utils
{
    public static DyeColor getDyeColor(String character)
    {
        if(character.length() != 1) return DyeColor.BLACK;
        DyeColor result;
        switch (character)
        {
            case "a":
            case "2":
                result = DyeColor.GREEN;
                break;
            case "b":
            case "3":
                result = DyeColor.CYAN;
                break;
            case "c":
            case "4":
                result = DyeColor.RED;
                break;
            case "d":
                result = DyeColor.PINK;
                break;
            case "e":
            case "6":
                result = DyeColor.YELLOW;
                break;
            case "f":
                result = DyeColor.WHITE;
                break;
            case "1":
            case "9":
                result = DyeColor.BLUE;
                break;
            case "5":
                result = DyeColor.PURPLE;
                break;
            case "7":
            case "8":
                result = DyeColor.GRAY;
                break;
            default:
                result = DyeColor.BLACK;
                break;
        }
        return result;
    }

    public static String argsBuilder(String[] args, int countFromArgument)
    {
        int argument = countFromArgument - 1;
        StringBuilder result = new StringBuilder();
        for(int i = argument; i < args.length; i++)
        {
            result.append(args[i]).append(" ");
        }
        if(result.toString().endsWith(" "))
        {
            return result.toString().substring(0, result.toString().length() - 1);
        }
        return result.toString();
    }

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

    public static boolean verifyHEX(String code)
    {
        if(code.length() != 1) return false;
        code = code.toLowerCase();
        return code.matches("-?[0-9a-fA-F]+");
    }
}
