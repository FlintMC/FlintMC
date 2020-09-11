package net.labyfy.internal.component.player.v1_15_2.serializer.util;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.util.PoseSerializer;
import net.labyfy.component.player.util.EntityPose;
import net.minecraft.entity.Pose;

/**
 * 1.15.2 implementation of {@link PoseSerializer}
 */
@Singleton
@Implement(value = PoseSerializer.class, version = "1.15.2")
public class VersionedPoseSerializer implements PoseSerializer<Pose> {

    /**
     * Deserializes the {@link Pose} to the {@link EntityPose}
     *
     * @param value The pose being deserialized
     * @return A deserialized {@link EntityPose}
     */
    @Override
    public EntityPose deserialize(Pose value) {
        switch (value) {
            case STANDING:
                return EntityPose.STANDING;
            case FALL_FLYING:
                return EntityPose.FALL_FLYING;
            case SLEEPING:
                return EntityPose.SLEEPING;
            case SWIMMING:
                return EntityPose.SWIMMING;
            case SPIN_ATTACK:
                return EntityPose.SPIN_ATTACK;
            case CROUCHING:
                return EntityPose.CROUCHING;
            case DYING:
                return EntityPose.DYING;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    /**
     * Serializes the {@link EntityPose} to the {@link Pose}
     *
     * @param value The entity pose being serialized
     * @return A serialized {@link Pose}
     */
    @Override
    public Pose serialize(EntityPose value) {
        switch (value) {
            case STANDING:
                return Pose.STANDING;
            case FALL_FLYING:
                return Pose.FALL_FLYING;
            case SLEEPING:
                return Pose.SLEEPING;
            case SWIMMING:
                return Pose.SWIMMING;
            case SPIN_ATTACK:
                return Pose.SPIN_ATTACK;
            case CROUCHING:
                return Pose.CROUCHING;
            case DYING:
                return Pose.DYING;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }
}
