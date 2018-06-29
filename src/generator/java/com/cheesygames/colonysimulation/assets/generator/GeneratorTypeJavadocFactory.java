package com.cheesygames.colonysimulation.assets.generator;

/**
 * This factory creates javadoc for a generated type, which instructs the reader that the type is generated and where to find the generator.
 */
public final class GeneratorTypeJavadocFactory {

    private static final String JAVADOC = "This is a generated type. Do not modify this file as it can be frequently updated without any indication. To know more about what "
        + "generated this class, please see the {@link %1$s generator's javadoc}. \r\n\r\n%2$s";

    private GeneratorTypeJavadocFactory() {
    }

    public static String createJavadoc(Class<?> generatorClass, String actualJavadoc) {
        return String.format(JAVADOC, generatorClass.getName(), actualJavadoc);
    }
}
