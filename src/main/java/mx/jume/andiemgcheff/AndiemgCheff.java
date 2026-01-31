package mx.jume.andiemgcheff;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.logger.HytaleLogger;
import mx.jume.andiemgcheff.commands.AndiemgCheffCommand;
import mx.jume.andiemgcheff.config.ConfigManager;
import mx.jume.andiemgcheff.logic.Merger;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.io.File;
import java.util.logging.Level;

public class AndiemgCheff extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static AndiemgCheff instance;

    private ConfigManager configManager;
    private Merger merger;

    public AndiemgCheff(@NonNullDecl JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {
        super.setup();

        // Setup config directory (manual management for consistency with
        // Aqua-Thirst-hunger)
        File configDir = new File("mods/AndiemgCheff");
        this.configManager = new ConfigManager(configDir);
        this.merger = new Merger();

        // Register commands
        this.getCommandRegistry().registerCommand(new AndiemgCheffCommand());

        // Automatic synchronization on startup if target mod is found
        Merger.MergeResult result = this.merger.performSync(this.configManager.getMainConfig(),
                this.configManager.getFoodConfig());
        if (result.targetFound()) {
            LOGGER.at(Level.INFO).log("Automatic sync completed: Added " + result.added() + ", Overwritten "
                    + result.overwritten() + ", Skipped " + result.skipped());
        } else {
            LOGGER.at(Level.INFO).log("Target mod (Aqua-Thirst-hunger) not detected. Skipping automatic sync.");
        }

        LOGGER.at(Level.INFO).log("AndiemgCheff v1.0.0 enabled!");
    }

    public static AndiemgCheff getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Merger getMerger() {
        return merger;
    }
}
