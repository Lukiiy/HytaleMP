package me.lukiiy.hytaleMP.cmds;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import me.lukiiy.hytaleMP.HytaleMP;
import me.lukiiy.hytaleMP.utils.ComposedLocation;
import me.lukiiy.hytaleMP.utils.PlayerHelper;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class Back extends AbstractAsyncCommand { // TODO
    private static final Color LIGHT_RED = new Color(0xeb4034);
    private static OptionalArg<PlayerRef> TARGET;

    public Back() {
        super("back", "Tells the player's death point (and maybe teleport them [if configured as such])");

        TARGET = withOptionalArg("player", "Optional target", ArgTypes.PLAYER_REF);
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(@NonNullDecl CommandContext commandContext) {
        boolean cheatyBack = HytaleMP.getInstance().CONFIG.get().isBackCheaty();
        boolean isPlayer = commandContext.isPlayer();

        PlayerRef self = isPlayer ? PlayerHelper.resolvePlayerRef(commandContext.senderAs(Player.class)) : null;
        PlayerRef target;

        if (commandContext.provided(TARGET)) {
            target = commandContext.get(TARGET);
        } else {
            if (!isPlayer) {
                commandContext.sendMessage(Message.raw("You need to specify a player to teleport them to their death point.").color(LIGHT_RED));

                return CompletableFuture.completedFuture(null);
            }

            target = self;
        }

        ComposedLocation deathLoc = HytaleMP.getInstance().deathLocations.get(target);

        if (deathLoc == null) {
            commandContext.sendMessage(Message.raw(target == self ? "You do not have a recorded death point." : "This player has no recorded death point!").color(LIGHT_RED));

            return CompletableFuture.completedFuture(null);
        }

        // Console: always teleport
        if (!isPlayer) {
            deathLoc.world().execute(() -> {
                target.getReference().getStore().addComponent(target.getReference(), Teleport.getComponentType(), new Teleport(deathLoc.world(), deathLoc.position(), deathLoc.rotation()));
                commandContext.sendMessage(Message.raw("Teleported " + target.getUsername() + " to their death point.").color(Color.GREEN));
            });

            return CompletableFuture.completedFuture(null);
        }

        // Player logic
        if (cheatyBack) {
            if (target != self) {
                deathLoc.world().execute(() -> {
                    target.getReference().getStore().addComponent(target.getReference(), Teleport.getComponentType(), new Teleport(deathLoc.world(), deathLoc.position(), deathLoc.rotation()));
                    commandContext.sendMessage(Message.raw("Teleported " + target.getUsername() + " to their death point.").color(Color.GREEN));
                });

                return CompletableFuture.completedFuture(null);
            }

            self.updatePosition(deathLoc.world(), deathLoc.positionAsTransform(), deathLoc.rotation());
            commandContext.sendMessage(Message.raw("Teleported to your last recorded death point!").color(Color.GREEN));
        } else if (target == self) {
            var p = deathLoc.position();

            commandContext.sendMessage(Message.join(Message.raw("Your death point is located at "), Message.raw((int) p.getX() + " " + (int) p.getY() + " " + (int) p.getZ()).color(LIGHT_RED)));
        }

        return CompletableFuture.completedFuture(null);
    }
}
