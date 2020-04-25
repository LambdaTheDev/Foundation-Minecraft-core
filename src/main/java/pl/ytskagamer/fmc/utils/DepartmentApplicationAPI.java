package pl.ytskagamer.fmc.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.ytskagamer.fmc.plugin.FMC;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class DepartmentApplicationAPI
{
    private final String FILENAME = "applications.yml";
    private FMC plugin = FMC.getPlugin(FMC.class);
    private FileConfiguration config;
    private File configFile;

    public void setup()
    {
        try
        {
            if(!plugin.getDataFolder().exists())
            {
                plugin.getDataFolder().mkdir();
            }

            configFile = new File(plugin.getDataFolder(), FILENAME);

            if(!configFile.exists())
            {
                configFile.createNewFile();
            }

            config = YamlConfiguration.loadConfiguration(configFile);
        }
        catch (IOException e)
        {
            FMC.error(e.getMessage());
        }
    }

    private void save()
    {
        try
        {
            config.save(configFile);
        }
        catch (IOException e)
        {
            FMC.error(e.getMessage());
        }
    }

    private void reload()
    {
        if(configFile == null)
        {
            setup();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void failApplication(String playerUUID)
    {
        config.set("tempcache." + playerUUID + ".failed", true);
    }

    public void finishApplication(String playerUUID)
    {
        config.set("tempcache." + playerUUID + ".finished", true);
    }

    public boolean startApplication(String playerUUID, String applicationUUID)
    {
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        reload();

        int cacheDay = config.getInt("staticcache." + playerUUID + ".cooldown");
        if(dayOfMonth == cacheDay)
        {
            System.out.println(playerUUID + " did application today! RESULT: " + cacheDay);
            return false;
        }

        config.set("staticcache." + playerUUID + ".cooldown", dayOfMonth);
        config.set("tempcache." + playerUUID + ".applicationid", applicationUUID);
        config.set("tempcache." + playerUUID + ".step", 0);
        config.set("tempcache." + playerUUID + ".finished", false);
        config.set("tempcache." + playerUUID + ".failed", false);
        save();
        return true;
    }

    public HashMap<Integer, String> getQuestions(String applicationUUID)
    {
        reload();
        HashMap<Integer, String> questionMap = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("application." + applicationUUID + ".questions");
        if(section == null) return questionMap;

        int i = 0;
        for(String question : section.getKeys(false))
        {
            questionMap.put(i, question);
            i++;
        }

        return questionMap;
    }

    public int getTempCacheApplicationID(String playerUUID)
    {
        return config.getInt("tempcache." + playerUUID + ".applicationid");
    }

    public int getTempCacheStep(String playerUUID)
    {
        return config.getInt("tempcache." + playerUUID + ".step");
    }

    public boolean getTempCacheFailed(String playerUUID)
    {
        return config.getBoolean("tempcache." + playerUUID + ".failed");
    }

    public boolean getTempCacheFinished(String playerUUID)
    {
        return config.getBoolean("tempcache." + playerUUID + ".finished");
    }

    public HashMap<String, Object> getStaticCache(String playerUUID)
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cooldown", config.getInt("staticcache." + playerUUID + ".cooldown"));
        return result;
    }

    public List<String> getCorrectAnswers(String applicationUUID, String question)
    {
        reload();
        List<String> correct = config.getStringList("applications." + applicationUUID + ".questions." + question + ".correct");
        if(correct.size() == 0) correct.add("NO CORRECT ANSWERS! REPORT IT TO STAFF!");
        return correct;
    }

    public List<String> getIncorrectAnswers(String applicationUUID, String question)
    {
        reload();
        List<String> correct = config.getStringList("applications." + applicationUUID + ".questions." + question + ".correct");
        if(correct.size() == 0) correct.add("NO CORRECT ANSWERS! REPORT IT TO STAFF!");
        return correct;
    }

    public String getDepartmentUUID(String applicationUUID)
    {
        return config.getString("applications." + applicationUUID + ".departmentuuid");
    }

    public List<String> getApplicationsByClearance(int clearance)
    {
        reload();
        ConfigurationSection keys = config.getConfigurationSection("applications");
        List<String> res = new ArrayList<>();
        if (keys == null) return res;
        for (String uuid : keys.getKeys(false))
        {
            int required = config.getInt("applications." + uuid + ".reqclearance");
            if(clearance >= required) res.add(uuid);
        }
        return res;
    }
}