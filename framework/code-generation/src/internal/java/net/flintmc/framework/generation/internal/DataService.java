package net.flintmc.framework.generation.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import javassist.ClassPool;
import javassist.CtClass;
import net.flintmc.framework.generation.DataImplementationGenerator;
import net.flintmc.framework.generation.annotation.Data;
import net.flintmc.framework.generation.annotation.creator.DataCreator;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.DataMethodParser;
import net.flintmc.framework.generation.parsing.creator.DataCreatorMethod;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;

@Singleton
@Service({Data.class})
public class DataService implements ServiceHandler<Data> {

  private final DataMethodParser dataMethodParser;

  private final DataImplementationGenerator dataImplementationGenerator;

  private final ClassPool classPool;

  @Inject
  public DataService(
      DataMethodParser dataMethodParser,
      DataImplementationGenerator dataImplementationGenerator,
      ClassPool classPool) {
    this.dataMethodParser = dataMethodParser;
    this.dataImplementationGenerator = dataImplementationGenerator;
    this.classPool = classPool;
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
                "Data interface %s does not specify an inner creator-interface!",
                dataInterface.getName()));
      }

      // parsing all methods which define the data fields by their parameters
      Collection<DataCreatorMethod> dataCreatorMethods =
          this.dataMethodParser.parseCreatorMethods(creatorInterface, dataInterface);

      // collecting all data fields from all create methods
      Collection<DataField> targetDataFields = new HashSet<>();
      for (DataCreatorMethod dataCreatorMethod : dataCreatorMethods) {
        targetDataFields.addAll(dataCreatorMethod.getTargetDataFields());
      }

      // parsing all wanted getter/setter methods from the data interface
      Collection<DataFieldMethod> dataFieldMethods =
          this.dataMethodParser.parseDataMethods(dataInterface, targetDataFields);

      String dataImplementationName =
          "Default"
              + dataInterface.getSimpleName()
              + UUID.randomUUID().toString().replaceAll("-", "");

      CtClass dataClass =
          this.dataImplementationGenerator.generateDataImplementationClass(
              dataInterface, dataImplementationName, targetDataFields, dataFieldMethods);

      CtClass creatorClass =
          this.dataImplementationGenerator.generateCreatorImplementationClass(
              creatorInterface, dataCreatorMethods, dataImplementationName);

      Object creatorInstance =
          this.classPool.toClass(creatorClass).getDeclaredConstructor().newInstance();
      Object dataImplementationInstance =
          this.classPool.toClass(dataClass).getDeclaredConstructor().newInstance();

      Class<Object> resolvedCreatorInterface = CtResolver.get(creatorInterface);
      Class<Object> resolvedDataInterface = CtResolver.get(dataInterface);

      InjectionHolder.getInstance()
          .addModules(
              new AbstractModule() {
                @Override
                protected void configure() {
                  super.bind(resolvedCreatorInterface).toInstance(creatorInstance);
                  super.bind(resolvedDataInterface).toInstance(dataImplementationInstance);
                }
              });

    } catch (Exception exception) {
      throw new ServiceNotFoundException(
          String.format(
              "Unable to create implementation for data class %s!", dataInterface.getName()),
          exception);
    }
  }
}
