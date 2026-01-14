package me.lukiiy.hytaleMP.utils;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.concurrent.CompletableFuture;

public class PlayerHelper {
    public static Player resolvePlayer(PlayerRef ref) {
        Ref<EntityStore> entityRef = ref.getReference();
        if (entityRef == null) return null;

        Store<EntityStore> store = entityRef.getStore();
        World world = store.getExternalData().getWorld();

        if (!world.isInThread()) return CompletableFuture.supplyAsync(() -> resolvePlayer(ref), world).join();
        return store.getComponent(entityRef, Player.getComponentType());
    }

    public static PlayerRef resolvePlayerRef(Player ref) {
        Ref<EntityStore> entityRef = ref.getReference();
        if (entityRef == null) return null;

        Store<EntityStore> store = entityRef.getStore();
        World world = store.getExternalData().getWorld();

        if (!world.isInThread()) return CompletableFuture.supplyAsync(() -> resolvePlayerRef(ref), world).join();
        return store.getComponent(entityRef, PlayerRef.getComponentType());
    }

    public static Vector3d getPlayerLocation(World world, PlayerRef playerRef) {
        if (!world.isInThread()) return CompletableFuture.supplyAsync(() -> getPlayerLocation(world, playerRef), world).join();

        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null || !ref.isValid()) return null;

        TransformComponent transform = world.getEntityStore().getStore().getComponent(ref, TransformComponent.getComponentType());

        return transform != null ? transform.getPosition() : null;
    }
}
