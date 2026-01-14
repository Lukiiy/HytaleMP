package me.lukiiy.hytaleMP.utils;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.universe.world.World;

import javax.annotation.Nonnull;

public record ComposedLocation(@Nonnull World world, @Nonnull Vector3d position, @Nonnull Vector3f rotation) {
    public Transform positionAsTransform() {
        return new Transform(position);
    }

    public HeadRotation rotationAsHeadRot() {
        return new HeadRotation(rotation);
    }
}
