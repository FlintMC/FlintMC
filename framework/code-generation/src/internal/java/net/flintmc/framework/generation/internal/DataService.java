package net.flintmc.framework.generation.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.generation.annotation.Data;
import net.flintmc.framework.generation.annotation.creator.DataCreator;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;

@Singleton
@Service({Data.class})
public class DataService implements ServiceHandler<Data> {

  private final CreatorImplementationGenerator creatorImplementationGenerator;

  private final DataImplementationGenerator dataImplementationGenerator;

  @Inject
  public DataService(
      CreatorImplementationGenerator creatorImplementationGenerator,
      DataImplementationGenerator dataImplementationGenerator) {
    this.creatorImplementationGenerator = creatorImplementationGenerator;
    this.dataImplementationGenerator = dataImplementationGenerator;
  }

  @Override
  public void discover(AnnotationMeta<Data> meta) throws ServiceNotFoundException {
    CtClass dataInterface = meta.getClassIdentifier().getLocation();

    if (!dataInterface.isInterface()) {
      throw new IllegalStateException(
          String.format("Target data class %s is not an interface!", dataInterface.getName()));
    }

    CtClass creatorInterface = null;

    try {
      for (CtClass declaredClass : dataInterface.getDeclaredClasses()) {
        if (declaredClass.hasAnnotation(DataCreator.class) && declaredClass.isInterface()) {
          creatorInterface = declaredClass;
          break;
        }
      }

      if (creatorInterface == null) {
        throw new IllegalStateException(
            String.format(
                "Data class %s does not specify an inner creator-interface!",
                dataInterface.getName()));
      }

      Class<Object> resolvedCreatorInterface = CtResolver.get(creatorInterface);
      Class<Object> resolvedDataInterface = CtResolver.get(dataInterface);

      Object creatorInstance =
          this.creatorImplementationGenerator.generateCreator(resolvedCreatorInterface);
      Object dataImplementationInstance =
          this.dataImplementationGenerator.generateImplementation(
              resolvedDataInterface, resolvedCreatorInterface);

      InjectionHolder.getInstance()
          .addModules(
              new AbstractModule() {
                @Override
                protected void configure() {
                  super.bind(resolvedCreatorInterface).toInstance(creatorInstance);
                  super.bind(resolvedDataInterface).toInstance(dataImplementationInstance);
                }
              });

    } catch (NotFoundException exception) {
      throw new ServiceNotFoundException(
          String.format(
              "Unable to create implementation for data class %s!", dataInterface.getName()),
          exception);
    }
  }
}
