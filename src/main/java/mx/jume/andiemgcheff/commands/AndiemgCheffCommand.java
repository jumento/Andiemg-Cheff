package mx.jume.andiemgcheff.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class AndiemgCheffCommand extends AbstractPlayerCommand {
    public static final String PERM_BASE = "AndiemgCheff.admin";

    public AndiemgCheffCommand() {
        super("AndiemgCheff", "Main command for AndiemgCheff", false);
        this.addSubCommand(new SyncSubCommand());
        this.addSubCommand(new ForceSubCommand());
        this.addSubCommand(new ReloadSubCommand());
        this.requirePermission(PERM_BASE);
    }

    @Override
    protected void execute(
            @NonNullDecl CommandContext context,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl PlayerRef playerRef,
            @NonNullDecl World world) {
        Message msg = Message.raw("--- AndiemgCheff Help ---\n" +
                "/AndiemgCheff sync - Merge food values into Aqua-Thirst-hunger\n" +
                "/AndiemgCheff force <true|false> - Toggle overwrite behavior\n" +
                "/AndiemgCheff reload - Reload AndiemgCheff configurations");
        playerRef.sendMessage(msg);
    }
}


