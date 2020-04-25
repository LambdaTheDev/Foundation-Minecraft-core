package pl.lambda.foundationminecraft.utils.datastorage;

import org.bukkit.configuration.file.FileConfiguration;

public interface IDataStorage
{
    void setup();
    void reload();
    void save();
    FileConfiguration getData();
}
