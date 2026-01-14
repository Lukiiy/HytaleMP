package me.lukiiy.hytaleMP;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.Config;
import me.lukiiy.hytaleMP.cmds.Back;
import me.lukiiy.hytaleMP.system.DeathListener;
import me.lukiiy.hytaleMP.utils.ComposedLocation;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HytaleMP extends JavaPlugin {
    private static HytaleMP instance;
    public Config<MPConfig> CONFIG;
    public Map<PlayerRef, ComposedLocation> deathLocations;

    public HytaleMP(@NonNullDecl JavaPluginInit init) {
        super(init);

        CONFIG = this.withConfig("HytaleMPConfig", MPConfig.CODEC);
    }

    @Override
    protected void setup() {
        instance = this;
        deathLocations = new ConcurrentHashMap<>();
        CONFIG.save();

        getLogger().atInfo().log("HytaleMP loaded!");

        new PlayerListener(getEventRegistry());
        new DeathListener(getEntityStoreRegistry());

        getCommandRegistry().registerCommand(new Back());
    }

    public static HytaleMP getInstance() {
        return instance;
    }
}
