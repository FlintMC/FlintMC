package net.flintmc.mcapi.v1_15_2.entity;

import com.google.common.collect.Sets;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntitySize;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.reason.MoverType;
import net.flintmc.mcapi.entity.render.EntityModelBoxHolder;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityPose;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.internal.entity.DefaultEntity;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.v1_15_2.entity.render.*;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.math.Vector3D;
import net.flintmc.mcapi.world.scoreboad.team.Team;
import net.flintmc.render.model.ModelBox;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.util.property.Property;
import net.flintmc.util.property.PropertyContext;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.stream.Stream;

@Implement(value = Entity.class, version = "1.15.2")
public class VersionedEntity<E extends net.minecraft.entity.Entity> extends DefaultEntity<E>
    implements Entity {

  private final Random random;
  private final EntityModelBoxHolder.Factory modelBoxHolderFactory;
  private final ModelBox.Factory modelBoxFactory;
  private final ModelBox.TexturedQuad.Factory texturedQuadFactory;
  private final ModelBox.TexturedQuad.VertexPosition.Factory vertexPositionFactory;

  @AssistedInject
  public VersionedEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityRenderContext.Factory entityRenderContextFactory) {
    super((E) entity, entityType, world, entityFoundationMapper, entityRenderContextFactory);
    this.modelBoxHolderFactory =
        InjectionHolder.getInjectedInstance(EntityModelBoxHolder.Factory.class);
    this.modelBoxFactory = InjectionHolder.getInjectedInstance(ModelBox.Factory.class);
    this.texturedQuadFactory =
        InjectionHolder.getInjectedInstance(ModelBox.TexturedQuad.Factory.class);
    this.vertexPositionFactory =
        InjectionHolder.getInjectedInstance(ModelBox.TexturedQuad.VertexPosition.Factory.class);
    this.entityRenderContext = this.createRenderContext();
    for (Map.Entry<String, ModelBoxHolder<Entity, EntityRenderContext>> entry :
        this.createModelRenderers().entrySet()) {
      this.entityRenderContext.registerRenderable(entry.getKey(), entry.getValue());
    }
    this.random = new Random();
  }

  protected ModelBoxHolder<Entity, EntityRenderContext> createModelBox(
      ModelRenderer modelRenderer) {

    Property<Float, Void> ROTATION_ANGLE_X_OLD =
        Property.builder().<Float>withValue().withDefaultValue(0f).build();

    Property<Float, Void> ROTATION_ANGLE_Y_OLD =
        Property.builder().<Float>withValue().withDefaultValue(0f).build();

    Property<Float, Void> ROTATION_ANGLE_Z_OLD =
        Property.builder().<Float>withValue().withDefaultValue(0f).build();

    Property<Float, Void> ROTATION_POINT_X_OLD =
        Property.builder().<Float>withValue().withDefaultValue(0f).build();

    Property<Float, Void> ROTATION_POINT_Y_OLD =
        Property.builder().<Float>withValue().withDefaultValue(0f).build();

    Property<Float, Void> ROTATION_POINT_Z_OLD =
        Property.builder().<Float>withValue().withDefaultValue(0f).build();

    Property<Integer, Void> TEXTURE_OFFSET_X_OLD =
        Property.builder().<Integer>withValue().withDefaultValue(0).build();

    Property<Integer, Void> TEXTURE_OFFSET_Y_OLD =
        Property.builder().<Integer>withValue().withDefaultValue(0).build();

    Property<Float, Void> TEXTURE_WIDTH_OLD =
        Property.builder().<Float>withValue().withDefaultValue(0f).build();

    Property<Float, Void> TEXTURE_HEIGHT_OLD =
        Property.builder().<Float>withValue().withDefaultValue(0f).build();

    Property<Boolean, Void> SHOW_MODEL_OLD =
        Property.builder().<Boolean>withValue().withDefaultValue(true).build();

    Property<Boolean, Void> MIRROR_OLD =
        Property.builder().<Boolean>withValue().withDefaultValue(false).build();

    ModelBoxHolder<Entity, EntityRenderContext> box =
        this.modelBoxHolderFactory
            .create(this.getRenderContext(), modelRenderer)
            .addPropertyPreparation(
                modelBoxHolder -> {
                  ModelRendererAccessor modelRendererAccessor =
                      (ModelRendererAccessor) modelRenderer;

                  PropertyContext<ModelBoxHolder<Entity, EntityRenderContext>> propertyContext =
                      modelBoxHolder.getPropertyContext();
                  propertyContext.setPropertyValue(MIRROR_OLD, modelRenderer.mirror);
                  propertyContext.setPropertyValue(SHOW_MODEL_OLD, modelRenderer.showModel);
                  propertyContext.setPropertyValue(
                      ROTATION_ANGLE_X_OLD, modelRenderer.rotateAngleX);
                  propertyContext.setPropertyValue(
                      ROTATION_ANGLE_Y_OLD, modelRenderer.rotateAngleY);
                  propertyContext.setPropertyValue(
                      ROTATION_ANGLE_Z_OLD, modelRenderer.rotateAngleZ);
                  propertyContext.setPropertyValue(
                      ROTATION_POINT_X_OLD, modelRenderer.rotationPointX);
                  propertyContext.setPropertyValue(
                      ROTATION_POINT_Y_OLD, modelRenderer.rotationPointY);
                  propertyContext.setPropertyValue(
                      ROTATION_POINT_Z_OLD, modelRenderer.rotationPointZ);
                  propertyContext.setPropertyValue(
                      TEXTURE_HEIGHT_OLD, modelRendererAccessor.getTextureHeight());
                  propertyContext.setPropertyValue(
                      TEXTURE_WIDTH_OLD, modelRendererAccessor.getTextureWidth());
                  propertyContext.setPropertyValue(
                      TEXTURE_OFFSET_X_OLD, modelRendererAccessor.getTextureOffsetX());
                  propertyContext.setPropertyValue(
                      TEXTURE_OFFSET_Y_OLD, modelRendererAccessor.getTextureOffsetY());

                  Set<ModelBox> modelBoxes = modelBoxHolder.getBoxes();
                  modelBoxes.clear();
                  for (ModelRenderer.ModelBox modelBox : modelRendererAccessor.getModelBoxes()) {
                    ModelBoxAccessor modelBoxAccessor = (ModelBoxAccessor) modelBox;
                    Collection<ModelBox.TexturedQuad> texturedQuads = new HashSet<>();

                    for (TexturedQuadAccessor quad : modelBoxAccessor.getQuads()) {
                      ModelBox.TexturedQuad.VertexPosition[] vertexPositions =
                          new ModelBox.TexturedQuad.VertexPosition
                              [quad.getVertexPositions().length];
                      for (int i = 0; i < vertexPositions.length; i++) {
                        PositionTextureVertexAccessor vertexPosition = quad.getVertexPositions()[i];
                        vertexPositions[i] =
                            vertexPositionFactory.create(
                                vertexPosition.getTextureU(),
                                vertexPosition.getTextureV(),
                                vertexPosition.getPosition().getX(),
                                vertexPosition.getPosition().getY(),
                                vertexPosition.getPosition().getZ());
                      }
                      texturedQuads.add(
                          texturedQuadFactory.create(
                              quad.getNormal().getX(),
                              quad.getNormal().getY(),
                              quad.getNormal().getZ(),
                              vertexPositions));
                    }

                    modelBoxes.add(
                        modelBoxFactory
                            .create()
                            .setPositionX1(modelBox.posX1)
                            .setPositionX2(modelBox.posX2)
                            .setPositionY1(modelBox.posY1)
                            .setPositionY2(modelBox.posY2)
                            .setPositionZ1(modelBox.posZ1)
                            .setPositionZ2(modelBox.posZ2)
                            .setTexturedQuads(texturedQuads));
                  }

                  modelBoxHolder
                      .setRotationAngleX(modelRenderer.rotateAngleX)
                      .setRotationAngleY(modelRenderer.rotateAngleY)
                      .setRotationAngleZ(modelRenderer.rotateAngleZ)
                      .setRotationPointX(modelRenderer.rotationPointX)
                      .setRotationPointY(modelRenderer.rotationPointY)
                      .setRotationPointZ(modelRenderer.rotationPointZ)
                      .setMirror(modelRenderer.mirror)
                      .setShowModel(modelRenderer.showModel)
                      .setTextureHeight(modelRendererAccessor.getTextureHeight())
                      .setTextureWidth(modelRendererAccessor.getTextureWidth())
                      .setTextureOffsetX(modelRendererAccessor.getTextureOffsetX())
                      .setTextureOffsetY(modelRendererAccessor.getTextureOffsetY())
                      .setModelBoxes(modelBoxes);
                })
            .addRenderPreparation(
                modelBoxHolder -> {
                  ModelRendererAccessor modelRendererAccessor =
                      (ModelRendererAccessor) modelRenderer;

                  if (modelBoxHolder.getShowModelOverridePolicy()
                      == ModelBoxHolder.OverridePolicy.ACTIVE)
                    modelRenderer.showModel = modelBoxHolder.isShowModel();

                  if (modelBoxHolder.getMirrorOverridePolicy()
                      == ModelBoxHolder.OverridePolicy.ACTIVE)
                    modelRenderer.mirror = modelBoxHolder.isMirror();

                  if (modelBoxHolder.getTextureOffsetXOverridePolicy()
                      == ModelBoxHolder.OverridePolicy.ACTIVE)
                    modelRendererAccessor.setTextureOffsetX(modelBoxHolder.getTextureOffsetX());

                  if (modelBoxHolder.getTextureOffsetYOverridePolicy()
                      == ModelBoxHolder.OverridePolicy.ACTIVE)
                    modelRendererAccessor.setTextureOffsetY(modelBoxHolder.getTextureOffsetY());

                  if (modelBoxHolder.getTextureWidthOverridePolicy()
                      == ModelBoxHolder.OverridePolicy.ACTIVE)
                    modelRendererAccessor.setTextureWidth(modelBoxHolder.getTextureWidth());

                  if (modelBoxHolder.getTextureHeightOverridePolicy()
                      == ModelBoxHolder.OverridePolicy.ACTIVE)
                    modelRendererAccessor.setTextureHeight(modelBoxHolder.getTextureHeight());

                  if (modelBoxHolder.getRotationAngleXMode()
                      == ModelBoxHolder.RotationMode.ABSOLUTE) modelRenderer.rotateAngleX = 0;

                  modelRenderer.rotateAngleX += modelBoxHolder.getRotationAngleX();

                  if (modelBoxHolder.getRotationAngleYMode()
                      == ModelBoxHolder.RotationMode.ABSOLUTE) modelRenderer.rotateAngleY = 0;

                  modelRenderer.rotateAngleY += modelBoxHolder.getRotationAngleY();

                  if (modelBoxHolder.getRotationAngleZMode()
                      == ModelBoxHolder.RotationMode.ABSOLUTE) modelRenderer.rotateAngleZ = 0;

                  modelRenderer.rotateAngleZ += modelBoxHolder.getRotationAngleZ();

                  if (modelBoxHolder.getRotationPointXMode()
                      == ModelBoxHolder.RotationMode.ABSOLUTE) modelRenderer.rotationPointX = 0;

                  modelRenderer.rotationPointX += modelBoxHolder.getRotationPointX();

                  if (modelBoxHolder.getRotationPointYMode()
                      == ModelBoxHolder.RotationMode.ABSOLUTE) modelRenderer.rotationPointY = 0;

                  modelRenderer.rotationPointY += modelBoxHolder.getRotationPointY();

                  if (modelBoxHolder.getRotationPointZMode()
                      == ModelBoxHolder.RotationMode.ABSOLUTE) modelRenderer.rotationPointZ = 0;

                  modelRenderer.rotationPointZ += modelBoxHolder.getRotationPointZ();
                })
            .addRenderCleanup(
                modelBoxHolder -> {
                  PropertyContext<ModelBoxHolder<Entity, EntityRenderContext>> propertyContext =
                      modelBoxHolder.getPropertyContext();
                  ModelRendererAccessor modelRendererAccessor =
                      (ModelRendererAccessor) modelRenderer;

                  modelRenderer.showModel = propertyContext.getPropertyValue(SHOW_MODEL_OLD);
                  modelRenderer.mirror = propertyContext.getPropertyValue(MIRROR_OLD);
                  modelRenderer.rotationPointX =
                      propertyContext.getPropertyValue(ROTATION_POINT_X_OLD);
                  modelRenderer.rotationPointY =
                      propertyContext.getPropertyValue(ROTATION_POINT_Y_OLD);
                  modelRenderer.rotationPointZ =
                      propertyContext.getPropertyValue(ROTATION_POINT_Z_OLD);
                  modelRenderer.rotateAngleX =
                      propertyContext.getPropertyValue(ROTATION_ANGLE_X_OLD);
                  modelRenderer.rotateAngleY =
                      propertyContext.getPropertyValue(ROTATION_ANGLE_Y_OLD);
                  modelRenderer.rotateAngleZ =
                      propertyContext.getPropertyValue(ROTATION_ANGLE_Z_OLD);
                  modelRendererAccessor.setTextureHeight(
                      propertyContext.getPropertyValue(TEXTURE_HEIGHT_OLD));
                  modelRendererAccessor.setTextureWidth(
                      propertyContext.getPropertyValue(TEXTURE_WIDTH_OLD));
                  modelRendererAccessor.setTextureOffsetX(
                      propertyContext.getPropertyValue(TEXTURE_OFFSET_X_OLD));
                  modelRendererAccessor.setTextureOffsetY(
                      propertyContext.getPropertyValue(TEXTURE_OFFSET_Y_OLD));
                });

    ((EntityAccessor) this.getHandle()).getFlintRenderables().put(modelRenderer, box);
    return box;
  }

  protected Map<String, ModelBoxHolder<Entity, EntityRenderContext>> createModelRenderers() {
    return new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTeamColor() {
    return this.getHandle().getTeamColor();
  }

  /** {@inheritDoc} */
  @Override
  public void detach() {
    this.getHandle().detach();
  }

  /** {@inheritDoc} */
  @Override
  public void setPacketCoordinates(double x, double y, double z) {
    this.getHandle().setPacketCoordinates(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public int getIdentifier() {
    return this.getHandle().getEntityId();
  }

  /** {@inheritDoc} */
  @Override
  public void setIdentifier(int identifier) {
    this.getHandle().setEntityId(identifier);
  }

  /** {@inheritDoc} */
  @Override
  public Set<String> getTags() {
    return this.getHandle().getTags();
  }

  /** {@inheritDoc} */
  @Override
  public boolean addTag(String tag) {
    return this.getHandle().addTag(tag);
  }

  /** {@inheritDoc} */
  @Override
  public boolean removeTag(String tag) {
    return this.getHandle().removeTag(tag);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosX() {
    return this.getHandle().getPosX();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosY() {
    return this.getHandle().getPosY();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosZ() {
    return this.getHandle().getPosZ();
  }

  /** {@inheritDoc} */
  @Override
  public void remove() {
    this.getHandle().remove();
  }

  /** {@inheritDoc} */
  @Override
  public void setPosition(double x, double y, double z) {
    this.getHandle().setPosition(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
    this.getHandle().setPositionAndRotation(x, y, z, yaw, pitch);
  }

  /** {@inheritDoc} */
  @Override
  public void moveToBlockPosAndAngles(
      BlockPosition position, float rotationYaw, float rotationPitch) {
    this.getHandle()
        .moveToBlockPosAndAngles(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position), rotationYaw, rotationPitch);
  }

  /** {@inheritDoc} */
  @Override
  public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
    this.getHandle().setLocationAndAngles(x, y, z, yaw, pitch);
  }

  /** {@inheritDoc} */
  @Override
  public void forceSetPosition(double x, double y, double z) {
    this.getHandle().forceSetPosition(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public float getDistance(Entity entity) {
    float distanceX = (float) (this.getPosX() - entity.getPosX());
    float distanceY = (float) (this.getPosY() - entity.getPosY());
    float distanceZ = (float) (this.getPosZ() - entity.getPosZ());
    return MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
  }

  /** {@inheritDoc} */
  @Override
  public double getDistanceSq(double x, double y, double z) {
    return this.getHandle().getDistanceSq(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public double getDistanceSq(Entity entity) {
    return this.getDistanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ());
  }

  /** {@inheritDoc} */
  @Override
  public void applyEntityCollision(Entity entity) {
    this.getHandle()
        .applyEntityCollision(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public void addVelocity(double x, double y, double z) {
    this.getHandle().addVelocity(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void rotateTowards(double yaw, double pitch) {
    this.getHandle().rotateTowards(yaw, pitch);
  }

  /** {@inheritDoc} */
  @Override
  public float getPitch(float partialTicks) {
    return this.getHandle().getPitch(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getPitch() {
    return this.getHandle().rotationPitch;
  }

  /** {@inheritDoc} */
  @Override
  public void setPitch(float pitch) {
    this.getHandle().rotationPitch = pitch;
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw(float partialTicks) {
    return this.getHandle().getYaw(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw() {
    return this.getHandle().rotationYaw;
  }

  /** {@inheritDoc} */
  @Override
  public void setYaw(float yaw) {
    this.getHandle().rotationYaw = yaw;
  }

  /** {@inheritDoc} */
  @Override
  public int getMaxInPortalTime() {
    return this.getHandle().getMaxInPortalTime();
  }

  /** {@inheritDoc} */
  @Override
  public void setFire(int seconds) {
    this.getHandle().setFire(seconds);
  }

  /** {@inheritDoc} */
  @Override
  public int getFireTimer() {
    return this.getHandle().getFireTimer();
  }

  /** {@inheritDoc} */
  @Override
  public void setFireTimer(int ticks) {
    this.getHandle().setFireTimer(ticks);
  }

  /** {@inheritDoc} */
  @Override
  public void extinguish() {
    this.getHandle().extinguish();
  }

  /** {@inheritDoc} */
  @Override
  public void resetPositionToBB() {
    this.getHandle().resetPositionToBB();
  }

  /** {@inheritDoc} */
  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    this.getHandle()
        .playSound(
            (SoundEvent)
                this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            volume,
            pitch);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSilent() {
    return this.getHandle().isSilent();
  }

  /** {@inheritDoc} */
  @Override
  public void setSilent(boolean silent) {
    this.getHandle().setSilent(silent);
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasNoGravity() {
    return this.getHandle().hasNoGravity();
  }

  /** {@inheritDoc} */
  @Override
  public void setNoGravity(boolean noGravity) {
    this.getHandle().setNoGravity(noGravity);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isImmuneToFire() {
    return this.getHandle().isImmuneToFire();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOffsetPositionInLiquid(double x, double y, double z) {
    return this.getHandle().isOffsetPositionInLiquid(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isWet() {
    return this.getHandle().isWet();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInWaterRainOrBubbleColumn() {
    return this.getHandle().isInWaterRainOrBubbleColumn();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInWaterOrBubbleColumn() {
    return this.getHandle().isInWaterOrBubbleColumn();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canSwim() {
    return this.getHandle().canSwim();
  }

  /** {@inheritDoc} */
  @Override
  public void updateSwim() {
    this.getHandle().updateSwimming();
  }

  /** {@inheritDoc} */
  @Override
  public boolean handleWaterMovement() {
    return this.getHandle().handleWaterMovement();
  }

  /** {@inheritDoc} */
  @Override
  public void spawnRunningParticles() {
    this.getHandle().spawnRunningParticles();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInWater() {
    return this.getHandle().isInWater();
  }

  /** {@inheritDoc} */
  @Override
  public void setInLava() {
    this.getHandle().setInLava();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInLava() {
    return this.getHandle().isInLava();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isBurning() {
    return this.getHandle().isBurning();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPassenger() {
    return this.getHandle().isPassenger();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isBeingRidden() {
    return this.getHandle().isBeingRidden();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSneaking() {
    return this.getHandle().isSneaking();
  }

  /** {@inheritDoc} */
  @Override
  public void setSneaking(boolean sneaking) {
    this.getHandle().setSneaking(sneaking);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSteppingCarefully() {
    return this.getHandle().isSteppingCarefully();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSuppressingBounce() {
    return this.getHandle().isSuppressingBounce();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDiscrete() {
    return this.getHandle().isDiscrete();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDescending() {
    return this.getHandle().isDescending();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCrouching() {
    return this.getHandle().isCrouching();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSprinting() {
    return this.getHandle().isSprinting();
  }

  /** {@inheritDoc} */
  @Override
  public void setSprinting(boolean sprinting) {
    this.getHandle().setSprinting(sprinting);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSwimming() {
    return this.getHandle().isSwimming();
  }

  /** {@inheritDoc} */
  @Override
  public void setSwimming(boolean swimming) {
    this.getHandle().setSwimming(swimming);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActuallySwimming() {
    return this.getHandle().isActualySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isVisuallySwimming() {
    return this.getHandle().isVisuallySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isGlowing() {
    return this.getHandle().isGlowing();
  }

  /** {@inheritDoc} */
  @Override
  public void setGlowing(boolean glowing) {
    this.getHandle().setGlowing(glowing);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInvisible() {
    return this.getHandle().isInvisible();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInvisibleToPlayer(net.flintmc.mcapi.player.PlayerEntity player) {
    return this.getHandle().isInvisibleToPlayer((PlayerEntity) player);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canRenderOnFire() {
    return this.getHandle().canRenderOnFire();
  }

  /** {@inheritDoc} */
  @Override
  public UUID getUniqueId() {
    return this.getHandle().getUniqueID();
  }

  /** {@inheritDoc} */
  @Override
  public void setUniqueId(UUID uniqueId) {
    this.getHandle().setUniqueId(uniqueId);
  }

  /** {@inheritDoc} */
  @Override
  public String getCachedUniqueId() {
    return this.getHandle().getCachedUniqueIdString();
  }

  /** {@inheritDoc} */
  @Override
  public String getScoreboardName() {
    return this.getHandle().getScoreboardName();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCustomNameVisible() {
    return this.getHandle().isCustomNameVisible();
  }

  /** {@inheritDoc} */
  @Override
  public void setCustomNameVisible(boolean alwaysRenderNameTag) {
    this.getHandle().setCustomNameVisible(alwaysRenderNameTag);
  }

  /** {@inheritDoc} */
  @Override
  public float getEyeHeight(EntityPose pose) {
    return this.getHandle()
        .getEyeHeight((Pose) this.getEntityFoundationMapper().toMinecraftPose(pose));
  }

  /** {@inheritDoc} */
  @Override
  public float getEyeHeight() {
    return this.getHandle().getEyeHeight();
  }

  /** {@inheritDoc} */
  @Override
  public float getBrightness() {
    return this.getHandle().getBrightness();
  }

  /** {@inheritDoc} */
  @Override
  public EntityPose getPose() {
    return this.getEntityFoundationMapper().fromMinecraftPose(this.getHandle().getPose());
  }

  /** {@inheritDoc} */
  @Override
  public Entity getRidingEntity() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftEntity(this.getHandle().getRidingEntity());
  }

  /** {@inheritDoc} */
  @Override
  public void setMotion(double x, double y, double z) {
    this.getHandle().setMotion(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void teleportKeepLoaded(double x, double y, double z) {
    this.getHandle().teleportKeepLoaded(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void setPositionAndUpdate(double x, double y, double z) {
    this.getHandle().setPositionAndUpdate(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAlwaysRenderNameTagForRender() {
    return this.getHandle().getAlwaysRenderNameTagForRender();
  }

  /** {@inheritDoc} */
  @Override
  public void recalculateSize() {
    this.getHandle().recalculateSize();
  }

  /** {@inheritDoc} */
  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return this.getHandle()
        .replaceItemInInventory(
            slot,
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isImmuneToExplosions() {
    return this.getHandle().isImmuneToExplosions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean ignoreItemEntityData() {
    return this.getHandle().ignoreItemEntityData();
  }

  /** {@inheritDoc} */
  @Override
  public Entity getControllingPassenger() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftEntity(this.getHandle().getControllingPassenger());
  }

  /** {@inheritDoc} */
  @Override
  public List<Entity> getPassengers() {
    List<Entity> passengers = new ArrayList<>();

    for (net.minecraft.entity.Entity passenger : this.getHandle().getPassengers()) {
      passengers.add(
          this.getEntityFoundationMapper().getEntityMapper().fromMinecraftEntity(passenger));
    }

    return passengers;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPassenger(Entity entity) {
    return this.getHandle()
        .isPassenger(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public Collection<Entity> getRecursivePassengers() {
    Set<Entity> entities = Sets.newHashSet();
    for (net.minecraft.entity.Entity passenger : this.getHandle().getPassengers()) {
      entities.add(
          this.getEntityFoundationMapper().getEntityMapper().fromMinecraftEntity(passenger));
    }

    return entities;
  }

  /** {@inheritDoc} */
  @Override
  public Stream<Entity> getSelfAndPassengers() {
    return Stream.concat(
        Stream.of(this), this.getPassengers().stream().flatMap(Entity::getSelfAndPassengers));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnePlayerRiding() {
    return this.getHandle().isOnePlayerRiding();
  }

  /** {@inheritDoc} */
  @Override
  public Entity getLowestRidingEntity() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftEntity(this.getHandle().getLowestRidingEntity());
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRidingSameEntity(Entity entity) {
    return this.getHandle()
        .isRidingSameEntity(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRidingOrBeingRiddenBy(Entity entity) {
    return this.getHandle()
        .isRidingOrBeingRiddenBy(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canPassengerSteer() {
    return this.getHandle().canPassengerSteer();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasPermissionLevel(int level) {
    return this.getHandle().hasPermissionLevel(level);
  }

  /** {@inheritDoc} */
  @Override
  public float getWidth() {
    return this.getHandle().getWidth();
  }

  /** {@inheritDoc} */
  @Override
  public float getHeight() {
    return this.getHandle().getHeight();
  }

  /** {@inheritDoc} */
  @Override
  public EntitySize getSize() {
    return this.getType().getSize();
  }

  /** {@inheritDoc} */
  @Override
  public BlockPosition getPosition() {
    return this.getWorld().fromMinecraftBlockPos(this.getHandle().getPosition());
  }

  /** {@inheritDoc} */
  @Override
  public double getPosXWidth(double width) {
    return this.getHandle().getPosXWidth(width);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosXRandom(double factor) {
    return this.getHandle().getPosXRandom(factor);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosYHeight(double height) {
    return this.getHandle().getPosYHeight(height);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosYRandom() {
    return this.getHandle().getPosYRandom();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosYEye() {
    return this.getHandle().getPosYEye();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosZWidth(double width) {
    return this.getHandle().getPosZWidth(width);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosZRandom(double factor) {
    return this.getHandle().getPosZRandom(factor);
  }

  /** {@inheritDoc} */
  @Override
  public void setRawPosition(double x, double y, double z) {
    this.getHandle().setRawPosition(x, y, z);
  }

  @Override
  public boolean isInvulnerable() {
    return this.getHandle().isInvulnerable();
  }

  /** {@inheritDoc} */
  @Override
  public void setInvulnerable(boolean invulnerable) {
    this.getHandle().setInvulnerable(invulnerable);
  }

  /** {@inheritDoc} */
  @Override
  public Team getTeam() {
    return this.getWorld().getScoreboard().getPlayerTeam(this.getScoreboardName());
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAlive() {
    return this.getHandle().isAlive();
  }

  /** {@inheritDoc} */
  @Override
  public void move(MoverType moverType, Vector3D vector3D) {
    this.getHandle()
        .move(
            (net.minecraft.entity.MoverType)
                this.getEntityFoundationMapper().toMinecraftMoverType(moverType),
            new Vec3d(vector3D.getX(), vector3D.getY(), vector3D.getZ()));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCollidedHorizontally() {
    return this.getHandle().collidedHorizontally;
  }

  /** {@inheritDoc} */
  @Override
  public void setCollidedHorizontally(boolean horizontally) {
    this.getHandle().collidedHorizontally = horizontally;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCollidedVertically() {
    return this.getHandle().collidedVertically;
  }

  /** {@inheritDoc} */
  @Override
  public void setCollidedVertically(boolean vertically) {
    this.getHandle().collidedVertically = vertically;
  }

  /** {@inheritDoc} */
  @Override
  public int getChunkCoordinateX() {
    return this.getHandle().chunkCoordX;
  }

  /** {@inheritDoc} */
  @Override
  public int getChunkCoordinateY() {
    return this.getHandle().chunkCoordY;
  }

  /** {@inheritDoc} */
  @Override
  public int getChunkCoordinateZ() {
    return this.getHandle().chunkCoordZ;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnGround() {
    return this.getHandle().onGround;
  }

  /** {@inheritDoc} */
  @Override
  public void setOnGround(boolean onGround) {
    this.getHandle().onGround = onGround;
  }

  /** {@inheritDoc} */
  @Override
  public Random getRandom() {
    return this.random;
  }

  /** {@inheritDoc} */
  @Override
  public EntityFoundationMapper getEntityFoundationMapper() {
    return this.getEntityFoundationMapper();
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getName() {
    return this.getEntityFoundationMapper()
        .getComponentMapper()
        .fromMinecraft(this.getHandle().getName());
  }
}
