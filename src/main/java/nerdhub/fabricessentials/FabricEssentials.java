package nerdhub.fabricessentials;

import abused_master.abusedlib.utils.Config;
import nerdhub.fabricessentials.registry.ModCommands;
import net.fabricmc.api.ModInitializer;

public class FabricEssentials implements ModInitializer {

    public static final String MODID = "fabricessentials";
    public static Config config = new Config(MODID, FabricEssentials.class);

    @Override
    public void onInitialize() {
        ModCommands.registerCommands();
    }
}
