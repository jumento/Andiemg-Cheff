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
import mx.jume.andiemgcheff.logic.Merger;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class SyncSubCommand extends AbstractPlayerCommand {
    public SyncSubCommand() {
        super("sync", "Sync food values", false);
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

        Merger.MergeResult result = plugin.getMerger().performSync(
                plugin.getConfigManager().getMainConfig(),
                plugin.getConfigManager().getFoodConfig());

        if (!result.targetFound()) {
            playerRef.sendMessage(
                    Message.raw("Sync failed: Aqua-Thirst-hunger config not found at " + result.targetPath()));
            return;
        }

        String summary = String.format("Sync completed!\nAdded: %d, Skipped: %d, Overwritten: %d\nTarget: %s",
                result.added(), result.skipped(), result.overwritten(), result.targetPath());
        playerRef.sendMessage(Message.raw(summary));
    }
}


