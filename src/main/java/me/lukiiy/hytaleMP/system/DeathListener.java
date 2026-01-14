package me.lukiiy.hytaleMP.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.lukiiy.hytaleMP.HytaleMP;
import me.lukiiy.hytaleMP.utils.ComposedLocation;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class DeathListener extends EntityTickingSystem<EntityStore> {
    private final ComponentType<EntityStore, DeathHandledComponent> DEATH_HANDLED_TYPE;

    public DeathListener(ComponentRegistryProxy<EntityStore> componentRegistry) {
        DEATH_HANDLED_TYPE = componentRegistry.registerComponent(DeathHandledComponent.class, () -> DeathHandledComponent.INSTANCE);
        componentRegistry.registerSystem(this);
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(DeathComponent.getComponentType(), TransformComponent.getComponentType(), Query.not(DEATH_HANDLED_TYPE), PlayerRef.getComponentType());
    }

    @Override
    public void tick(float v, int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        PlayerRef pRef = archetypeChunk.getComponent(i, PlayerRef.getComponentType());
        TransformComponent transform = archetypeChunk.getComponent(i, TransformComponent.getComponentType());
        if (pRef == null || transform == null) return;

        ComposedLocation loc = new ComposedLocation(store.getExternalData().getWorld(), transform.getPosition(), archetypeChunk.getComponent(i, HeadRotation.getComponentType()).getRotation());

        HytaleMP.getInstance().deathLocations.put(pRef, loc);

        commandBuffer.addComponent(archetypeChunk.getReferenceTo(i), DEATH_HANDLED_TYPE, DeathHandledComponent.INSTANCE);
    }

    public static class DeathHandledComponent implements Component<EntityStore> {
        public static final DeathHandledComponent INSTANCE = new DeathHandledComponent();

        private DeathHandledComponent() {}

        @Override
        public Component<EntityStore> clone() {
            return null;
        }
    }
}
