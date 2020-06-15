package net.labyfy.base.structure;

/**
 * Constants for better overview regarding the loading order of classes with the @AutoLoad annotation
 */
public class AutoLoadPriorityConstants {

    // Sort after round
    // then after priority
    // subclasses inherit the round of the parent class

    // Round -1000
    public static final int DEFAULT_MAPPING_FILE_PROVIDER_PRIORITY = -1000;
    public static final int DEFAULT_MAPPING_FILE_PROVIDER_ROUND = -1000;

    // Round -4
    public static final int ASSISTED_FACTORY_SERVICE_PRIORITY = -1000;
    public static final int ASSISTED_FACTORY_SERVICE_ROUND = -4;
    public static final int ASSISTED_FACTORY_SERVICE_REGISTRAR_PRIORITY = -1000;

    public static final int IMPLEMENT_SERVICE_PRIORITY = -1000;
    public static final int IMPLEMENT_SERVICE_ROUND = -4;
    public static final int IMPLEMENT_SERVICE_REGISTRAR_PRIORITY = -1000;

    // Round -3
    public static final int MINECRAFT_TRANSFORMER_SERVICE_PRIORITY = -1000;
    public static final int MINECRAFT_TRANSFORMER_SERVICE_ROUND = -3;
    public static final int MINECRAFT_TRANSFORMER_SERVICE_REGISTRAR_PRIORITY = -200;

    public static final int MINECRAFT_INSTRUCTION_OBFUSCATOR_PRIORITY = -1000;
    public static final int MINECRAFT_INSTRUCTION_OBFUSCATOR_ROUND = -3;

    // Round -1
    public static final int IMPLEMENT_PRIORITY = -15;
    public static final int IMPLEMENT_ROUND = -1;

    public static final int ASSISTED_FACTORY_PRIORITY = -10;
    public static final int ASSISTED_FACTORY_ROUND = -1;

    // Round 0
    public static final int SERVICE_PRIORITY = -100;
    public static final int SERVICE_ROUND = 0;

    // Round 1 is default then...

}