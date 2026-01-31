package mx.jume.andiemgcheff.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import mx.jume.andiemgcheff.AndiemgCheff;
import mx.jume.andiemgcheff.config.AndiemgCheffConfig;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ReloadSubCommand extends AbstractPlayerCommand {
    public ReloadSubCommand() {
        super("reload", "Reload configurations", false);
    }

    @Override
    protected void execute(
            @NonNullDecl CommandContext context,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl PlayerRef playerRef,
            @NonNullDecl World world) {
        AndiemgCheff plugin = AndiemgCheff.getInstance();
        if (plugin == null)
            return;

        plugin.getConfigManager().loadAll();
        AndiemgCheffConfig cfg = plugin.getConfigManager().getMainConfig();
        int count = plugin.getConfigManager().getFoodConfig().entries.size();

        String msg = String.format("AndiemgCheff Reloaded!\nVersion: %s, Overwrite: %b, Food Entries: %d",
                cfg.version, cfg.forceOverwrite, count);
        playerRef.sendMessage(Message.raw(msg));
    }
}


