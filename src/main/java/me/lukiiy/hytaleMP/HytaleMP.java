package me.lukiiy.hytaleMP;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.concurrent.CompletableFuture;

public class HytaleMP extends JavaPlugin {
    private static HytaleMP instance;
    public static Config<MPConfig> CONFIG;

    public HytaleMP(@NonNullDecl JavaPluginInit init) {
        super(init);
        CONFIG = this.withConfig("HytaleMPConfig", MPConfig.CODEC);
    }

    @Override
    protected void setup() {
        instance = this;
        CONFIG.save();

        getLogger().atInfo().log("HytaleMP loaded!");

        EventRegistry registry = getEventRegistry();

        registry.registerGlobal(PlayerReadyEvent.class, e -> e.getPlayer().sendMessage(Message.raw(CONFIG.get().getWelcome())));

        registry.registerGlobal(PlayerChatEvent.class, e -> { // TODO
            PlayerRef sender = e.getSender();
            if (sender.getWorldUuid() == null) return;

            World w = Universe.get().getWorld(sender.getWorldUuid());
            if (w == null) return;

            Vector3d loc = getPlayerLocation(w, sender);
            if (loc == null) return;

            e.setContent(e.getContent().replace("[coords]", loc.getX() + " " + loc.getY() + " " + loc.getZ()));
        });
    }

    public static HytaleMP getInstance() {
        return instance;
    }

    public static Player resolvePlayer(PlayerRef ref) {
        Ref<EntityStore> entityRef = ref.getReference();
        if (entityRef == null) return null;

        Store<EntityStore> store = entityRef.getStore();
        World world = store.getExternalData().getWorld();

        if (!world.isInThread()) return CompletableFuture.supplyAsync(() -> resolvePlayer(ref), world).join();
        return store.getComponent(entityRef, Player.getComponentType());
    }

    public static Vector3d getPlayerLocation(World world, PlayerRef playerRef) {
        if (!world.isInThread()) return CompletableFuture.supplyAsync(() -> getPlayerLocation(world, playerRef), world).join();

        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null || !ref.isValid()) return null;

        TransformComponent transform = world.getEntityStore().getStore().getComponent(ref, TransformComponent.getComponentType());

        return transform != null ? transform.getPosition() : null;
    }
}
