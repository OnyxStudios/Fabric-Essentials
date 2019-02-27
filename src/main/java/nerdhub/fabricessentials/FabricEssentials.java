package nerdhub.fabricessentials;

import abused_master.abusedlib.utils.Config;
import nerdhub.fabricessentials.registry.ModCommands;
import nerdhub.fabricessentials.utils.TPSHelper;
import net.fabricmc.api.ModInitializer;

import java.util.Timer;
import java.util.TimerTask;

public class FabricEssentials implements ModInitializer {

    public static final String MODID = "fabricessentials";
    public static Config config = new Config(MODID, FabricEssentials.class);
    public static TPSHelper serverTimer;
    public Timer timer = new Timer();

    @Override
    public void onInitialize() {
        ModCommands.registerCommands();
        serverTimer = new TPSHelper();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                serverTimer.run();
            }
        }, 1000, 50);
    }
}
