package net.flintmc.mcapi.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.transform.hook.Hook;
import net.minecraft.potion.EffectInstance;
import org.apache.logging.log4j.Logger;

@Singleton
public class TestApplication {

  private final Logger logger;

  @Inject
  private TestApplication(Logger logger) {
    this.logger = logger;
  }


  @Hook(
          className = "net.minecraft.entity.LivingEntity",
          methodName = "baseTick"
  )
  public void hookNewPotionEffect(@Named("args") Object[] args) {
    this.logger.info("BaseTick");
  }

  @Hook(
          className = "net.minecraft.entity.LivingEntity",
          methodName = "onFinishedPotionEffect",
          parameters = {
                  @Type(reference = EffectInstance.class)
          }
  )
  public void hookFinishedPotionEffect(@Named("args") Object[] args) {
    this.logger.info("on finished potion effect!");
  }

}
