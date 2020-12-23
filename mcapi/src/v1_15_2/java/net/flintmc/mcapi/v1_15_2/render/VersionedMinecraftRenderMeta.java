package net.flintmc.mcapi.v1_15_2.render;

import java.util.Stack;
import java.util.UUID;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.MinecraftRenderMeta;
import net.flintmc.util.math.matrix.Matrix3x3f;
import net.flintmc.util.math.matrix.Matrix4x4f;

@Implement(MinecraftRenderMeta.class)
public class VersionedMinecraftRenderMeta implements MinecraftRenderMeta {

  private final Stack<MatrixState> matrixStates;
  private final Matrix4x4f.Factory matrix4x4fFactory;
  private final Matrix3x3f.Factory matrix3x3fFactory;

  private int packedLight;
  private float partialTick;
  private UUID targetUuid;

  @AssistedInject
  private VersionedMinecraftRenderMeta(
      Matrix3x3f.Factory matrix3x3Factory,
      Matrix4x4f.Factory matrix4x4Factory,
      Matrix4x4f.Factory matrix4x4fFactory,
      Matrix3x3f.Factory matrix3x3fFactory) {
    this.matrix4x4fFactory = matrix4x4fFactory;
    this.matrix3x3fFactory = matrix3x3fFactory;
    this.matrixStates = new Stack<>();
    this.matrixStates.push(new MatrixState(matrix3x3Factory.create(), matrix4x4Factory.create()));
  }

  @Override
  public MinecraftRenderMeta push() {
    this.matrixStates.push(new MatrixState(this.matrixStates.peek()));
    return this;
  }

  @Override
  public MinecraftRenderMeta pop() {
    this.matrixStates.pop();
    return this;
  }

  @Override
  public UUID getTargetUUID() {
    return this.targetUuid;
  }

  @Override
  public MinecraftRenderMeta rotateToPlayersCamera() {
    Matrix3x3f matrix3x3f = matrix3x3fFactory.create();
    matrix3x3f.setIdentity();
    getWorld().set3x3(matrix3x3f);
    return this;
  }

  @Override
  public MinecraftRenderMeta rotate(float ang, float x, float y, float z) {
    ang = (float) Math.toRadians(ang);
    this.getNormal().rotate(ang, -x, -y, -z);
    this.getWorld().rotate(-ang, -x, -y, -z);
    return this;
  }

  @Override
  public MinecraftRenderMeta scale(float factor) {
    this.getNormal().scale(factor);
    this.getWorld().scale(factor);
    return this;
  }

  @Override
  public MinecraftRenderMeta scale(float factorX, float factorY, float factorZ) {
    this.getNormal().scale(factorX, factorY, factorZ);
    this.getWorld().scale(factorX, factorY, factorZ);
    return this;
  }

  @Override
  public MinecraftRenderMeta translate(float x, float y, float z) {
    x *= 16;
    y *= 16;
    z *= 16;
    this.getWorld().translate(x, y, z);
    return this;
  }

  @Override
  public Matrix3x3f getNormal() {
    return this.matrixStates.peek().getNormal();
  }

  @Override
  public Matrix4x4f getWorld() {
    return this.matrixStates.peek().getWorld();
  }

  @Override
  public int getPackedLight() {
    return this.packedLight;
  }

  @Override
  public float getPartialTick() {
    return this.partialTick;
  }

  @Override
  public MinecraftRenderMeta setPackedLight(int packedLight) {
    this.packedLight = packedLight;
    return this;
  }

  @Override
  public MinecraftRenderMeta setPartialTick(float partialTick) {
    this.partialTick = partialTick;
    return this;
  }

  @Override
  public MinecraftRenderMeta setTargetUuid(UUID uuid) {
    this.targetUuid = uuid;
    return this;
  }

  private class MatrixState {
    private final Matrix3x3f normal;
    private final Matrix4x4f world;

    private MatrixState(Matrix3x3f normal, Matrix4x4f world) {
      this.normal =
          matrix3x3fFactory
              .create()
              .set(
                  normal.getM00(),
                  normal.getM01(),
                  normal.getM02(),
                  normal.getM10(),
                  normal.getM11(),
                  normal.getM12(),
                  normal.getM20(),
                  normal.getM21(),
                  normal.getM22());
      this.world =
          matrix4x4fFactory
              .create()
              .set(
                  world.getM00(),
                  world.getM01(),
                  world.getM02(),
                  world.getM03(),
                  world.getM10(),
                  world.getM11(),
                  world.getM12(),
                  world.getM13(),
                  world.getM20(),
                  world.getM21(),
                  world.getM22(),
                  world.getM23(),
                  world.getM30(),
                  world.getM31(),
                  world.getM32(),
                  world.getM33());
    }

    public MatrixState(MatrixState peek) {
      this.normal = peek.getNormal().copy(matrix3x3fFactory.create());
      this.world = peek.getWorld().copy(matrix4x4fFactory.create());
    }

    public Matrix3x3f getNormal() {
      return normal;
    }

    public Matrix4x4f getWorld() {
      return world;
    }
  }
}
