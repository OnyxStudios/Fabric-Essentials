package nerdhub.fabricessentials;

import nerdhub.fabricessentials.utils.YamlFile;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private YamlFile config;
    private Logger LOGGER = LogManager.getLogger("Fabric Essentials");

    public Config() {
        config = new YamlFile(FabricLoader.getInstance().getConfigDirectory() + "/fabricessentials.yml");

        if(!config.exists()) {
            LOGGER.info("Creating a config file for Fabric Essentials");

            InputStream defaultConfigFile = FabricEssentials.class.getClassLoader().getResourceAsStream("assets/fabricessentials/config.yml");
            try (PrintWriter printWriter = new PrintWriter(new FileWriter(config.getConfigFile()))) {
                List<String> linesList = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(defaultConfigFile));

                while (reader.ready()) {
                    linesList.add(reader.readLine());
                }

                for (String string : linesList) {
                    printWriter.println(string);
                }

            } catch (IOException e) {
                LOGGER.fatal("You done borked something with your config", e);
            }
        }

        LOGGER.info("Loading the config file for Fabric Essentials");
        config.load();
    }

    public boolean getBoolean(String name) {
        return getConfig().getBoolean(name);
    }

    public int getInt(String name) {
        return getConfig().getInt(name);
    }

    public YamlFile getConfig() {
        return config;
    }
}
