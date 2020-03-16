package net.labyfy.component.assistedinject;

import com.google.inject.assistedinject.AssistedInject;

public class Test {

  @AssistedInject
  private Test() {
  }

  @AssistedFactory(Test.class)
  public  interface Factory{
    Test create();
  }
}
