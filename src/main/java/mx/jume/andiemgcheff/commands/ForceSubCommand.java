package mx.jume.andiemgcheff.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import mx.jume.andiemgcheff.AndiemgCheff;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ForceSubCommand extends AbstractPlayerCommand {
    private final RequiredArg<Boolean> forceArg = this.withRequiredArg("value", "The force overwrite state",
            ArgTypes.BOOLEAN);

    public ForceSubCommand() {
        super("force", "Toggle force overwrite", false);
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

        boolean value = this.forceArg.get(context);
        plugin.getConfigManager().getMainConfig().forceOverwrite = value;
        plugin.getConfigManager().saveMainConfig();

        playerRef.sendMessage(Message.raw("Force overwrite set to: " + value));
    }
}


