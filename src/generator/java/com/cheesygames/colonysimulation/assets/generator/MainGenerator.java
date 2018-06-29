package com.cheesygames.colonysimulation.assets.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The Main Generator, the one that contains the main. The task executeCodeGeneration calls this class's main.
 */
public class MainGenerator {

    private static final String MAIN_ARG_EXCEPTION_MSG = "Main's arguments aren't asset paths.";
    private static final int ERROR_WRONG_ARGS = 1;
    private static final String SRC_GENERATED_JAVA = "src/generated/java";
    private static final String PACKAGE_GENERATED = "com.cheesygames.colonysimulation.assets";

    /**
     * Executes the Asset Generator.
     *
     * @param assetDirAndAssetPaths The arguments are the resources root folders.
     */
    public static void main(String[] assetDirAndAssetPaths) {
        AssetGenerator assetGenerator = new AssetGenerator();
        AssetHierarchyGenerator assetHierarchyGenerator = new AssetHierarchyGenerator();

        List<File> assetDirectories = extractAssetDirectories(assetDirAndAssetPaths);
        File[] assets = extractAssets(assetDirectories, assetDirAndAssetPaths);

        TypeSpec assetHierarchyEnum = assetHierarchyGenerator.buildTypeSpec(assetDirectories, assets);
        TypeSpec assetEnum = assetGenerator.buildTypeSpec(assetDirectories, assets);

        writeTypeSpecToGeneratedPackage(assetHierarchyEnum);
        writeTypeSpecToGeneratedPackage(assetEnum);
    }

    /**
     * Extracts the asset directories from the main's arguments.
     *
     * @param assetDirAndAssetPaths The main's arguments, which each argument is either an asset directory path or an asset path. Asset directory paths are at the beginning of the
     *                              array.
     *
     * @return All asset directories.
     */
    private static List<File> extractAssetDirectories(String[] assetDirAndAssetPaths) {
        List<File> assetDirectories = new ArrayList<>();

        for (int i = 0; i < assetDirAndAssetPaths.length; ++i) {
            File assetDirectory = new File(assetDirAndAssetPaths[i]);
            if (!assetDirectory.isDirectory()) {
                break;
            }

            assetDirectories.add(assetDirectory);
        }

        return assetDirectories;
    }

    /**
     * Extracts the asset files from the main's arguments.
     *
     * @param assetDirectories      The asset directories.
     * @param assetDirAndAssetPaths The main's arguments, which each argument is either an asset directory path or an asset path. Asset directory paths are at the beginning of the
     *                              array.
     *
     * @return The asset files.
     */
    private static File[] extractAssets(List<File> assetDirectories, String[] assetDirAndAssetPaths) {
        File[] assets = new File[assetDirAndAssetPaths.length - assetDirectories.size()];

        for (int i = 0; i < assets.length; ++i) {
            try {
                assets[i] = new File(assetDirAndAssetPaths[i + assetDirectories.size()]);

                if (AssetGenerator.class.getClassLoader().getResource(assets[i].getPath()) == null) {
                    throw new IllegalArgumentException(MAIN_ARG_EXCEPTION_MSG);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(ERROR_WRONG_ARGS);
            }
        }

        return assets;
    }

    /**
     * Computes the ClassName for the class name according to the assets package.
     *
     * @param className The class name from which to get the ClassName.
     *
     * @return The computed ClassName
     */
    public static ClassName computeTypeClassName(String className) {
        return ClassName.get(PACKAGE_GENERATED, className);
    }

    /**
     * Write the code to the package for generated code.
     *
     * @param typeSpec The TypeSpec to save.
     */
    private static void writeTypeSpecToGeneratedPackage(TypeSpec typeSpec) {
        JavaFile javaFile = JavaFile.builder(PACKAGE_GENERATED, typeSpec).build();

        try {
            File assetEnum = new File(SRC_GENERATED_JAVA);
            javaFile.writeTo(assetEnum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
