package com.cheesygames.colonysimulation.assets.generator;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Generates an enum the asset hierarchy in terms of directories.
 */
public class AssetHierarchyGenerator implements IGenerator {

    private static final String EMPTY_STR = "";
    private static final String REGEX_OS_FILE_SEPARATOR = Matcher.quoteReplacement(File.separator);

    static final String ENUM_NAME = "AssetHierarchy";
    private static final String ENUM_VAR_PARENT = "parent";
    private static final String ENUM_VAR_CHILDREN = "children";
    private static final String ENUM_VAR_M_PARENT = VAR_M_ + ENUM_VAR_PARENT;
    private static final String ENUM_VAR_M_CHILDREN = VAR_M_ + ENUM_VAR_CHILDREN;

    private static final String JAVADOC = "Asset directory hierarchy. It allows to bidirectionally traverse the hierarchy tree.";

    private ClassName m_selfClass;
    private ArrayTypeName m_selfClassArray;
    private MethodSpec m_getParent;
    private MethodSpec m_setChildren;

    public AssetHierarchyGenerator() {
        this.m_selfClass = MainGenerator.computeTypeClassName(ENUM_NAME);
        this.m_selfClassArray = ArrayTypeName.of(m_selfClass);
    }

    @Override
    public TypeSpec buildTypeSpec(List<File> assetDirectories, File[] assets) {
        TypeSpec.Builder builder = TypeSpec.enumBuilder(ENUM_NAME).addModifiers(Modifier.PUBLIC);

        builder.addJavadoc(GeneratorTypeJavadocFactory.createJavadoc(AssetGenerator.class, JAVADOC));
        addConstructor(builder);
        addVarParent(builder);
        addVarChildren(builder);
        fillEnumConstants(builder, assetDirectories);
        addStaticBlocSetChildren(builder);

        return builder.build();
    }

    /**
     * Adds a basic constructor that sets the TypeSpec's member variable(s).
     *
     * @param builder The owner of the constructor.
     */
    private void addConstructor(TypeSpec.Builder builder) {
        builder.addMethod(MethodSpec.constructorBuilder().addStatement("this.$N = null", ENUM_VAR_M_PARENT).build());

        builder.addMethod(MethodSpec.constructorBuilder().addParameter(m_selfClass, ENUM_VAR_PARENT).addStatement("this.$N = $N", ENUM_VAR_M_PARENT, ENUM_VAR_PARENT).build());
    }

    /**
     * Adds the member variable m_parent to the enum and also its getter.
     *
     * @param builder The TypeSpec.Builder to attach the member variable and its getter.
     */
    private void addVarParent(TypeSpec.Builder builder) {
        builder.addField(m_selfClass, ENUM_VAR_M_PARENT, Modifier.PRIVATE, Modifier.FINAL);

        m_getParent = MethodSpec.methodBuilder(MEMBER_VAR_GET + StringUtils.capitalize(ENUM_VAR_PARENT))
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement("return $N", ENUM_VAR_M_PARENT)
                                .returns(m_selfClass)
                                .build();

        builder.addMethod(m_getParent);
    }

    /**
     * Adds the member variable m_children to the enum and also its getter and setter.
     *
     * @param builder The TypeSpec.Builder to attach the member variable and its getter and setter.
     */
    private void addVarChildren(TypeSpec.Builder builder) {
        builder.addField(m_selfClassArray, ENUM_VAR_M_CHILDREN, Modifier.PRIVATE);

        builder.addMethod(MethodSpec.methodBuilder(MEMBER_VAR_GET + StringUtils.capitalize(ENUM_VAR_CHILDREN))
                                    .addModifiers(Modifier.PUBLIC)
                                    .addStatement("return $N", ENUM_VAR_M_CHILDREN)
                                    .returns(m_selfClassArray)
                                    .build());

        m_setChildren = MethodSpec.methodBuilder(MEMBER_VAR_SET + StringUtils.capitalize(ENUM_VAR_CHILDREN))
                                  .addModifiers(Modifier.PRIVATE)
                                  .addParameter(m_selfClassArray, ENUM_VAR_CHILDREN)
                                  .addStatement("this.$N = $N", ENUM_VAR_M_CHILDREN, ENUM_VAR_CHILDREN)
                                  .build();

        builder.addMethod(m_setChildren);
    }

    /**
     * Fills the enum with constants.
     *
     * @param builder          The enum type specification builder.
     * @param assetDirectories All the asset directories (sourceSet resources directories).
     */
    private void fillEnumConstants(TypeSpec.Builder builder, List<File> assetDirectories) {
        final FileFilter directoryFilter = new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        for (File assetDirectory : assetDirectories) {
            File[] subAssetDirectories = assetDirectory.listFiles(directoryFilter);

            for (File subAssetDirectory : subAssetDirectories) {
                searchAssetSubDirectories(builder, assetDirectory, subAssetDirectory, directoryFilter, null);
            }
        }
    }

    /**
     * Search the asset subdirectories recursively in order to map all the hierarchy tree.
     *
     * @param builder                 The enum type specification builder.
     * @param absoluteParentDirectory The absolute asset directory (sourceSet resources directory).
     * @param assetDirectory          The current asset directory to search.
     * @param directoryFilter         The file filter for keeping only directories.
     * @param parentEnumConstant      The parent's folder enum constant
     */
    private void searchAssetSubDirectories(TypeSpec.Builder builder, File absoluteParentDirectory, File assetDirectory, FileFilter directoryFilter, String parentEnumConstant) {
        File[] subAssetDirectories = assetDirectory.listFiles(directoryFilter);

        if (subAssetDirectories.length > 0) {
            String enumConstant = addEnumConstant(builder, absoluteParentDirectory, assetDirectory, parentEnumConstant);

            for (File subAssetDirectory : subAssetDirectories) {
                searchAssetSubDirectories(builder, absoluteParentDirectory, subAssetDirectory, directoryFilter, enumConstant);
            }
        }
    }

    /**
     * Adds the asset directory to the enum type specification builder.
     *
     * @param builder                 The enum type specification builder.
     * @param absoluteParentDirectory The absolute asset directory (sourceSet resources directory).
     * @param assetDirectory          The current asset directory to add as a constant to the enum type specification builder.
     * @param parentEnumConstant      The parent's folder enum constant
     *
     * @return The constant name
     */
    private String addEnumConstant(TypeSpec.Builder builder, File absoluteParentDirectory, File assetDirectory, String parentEnumConstant) {
        String constantName = assetDirectory.getPath().replace(absoluteParentDirectory.getPath(), EMPTY_STR);
        constantName = constantName.replaceFirst(REGEX_OS_FILE_SEPARATOR, EMPTY_STR);
        constantName = constantName.replace(File.separatorChar, ENUM_CONSTANT_NAME_SEPARATOR);
        constantName = constantName.toUpperCase();

        if (parentEnumConstant == null) {
            builder.addEnumConstant(constantName);
        }
        else {
            builder.addEnumConstant(constantName, TypeSpec.anonymousClassBuilder("$N", parentEnumConstant).build());
        }

        return constantName;
    }

    /**
     * Adds a static bloc to set the children according to their parents.
     *
     * @param builder The type spec builder.
     */
    private void addStaticBlocSetChildren(TypeSpec.Builder builder) {
        final String children = "children";
        final String parent = "parent";
        final String child = "child";
        final String childrenArray = children + "Array";

        builder.addStaticBlock(CodeBlock.builder()
                                        .addStatement("$T $N = new $T<>()",
                                            ParameterizedTypeName.get(ClassName.get(List.class), m_selfClass.withoutAnnotations()),
                                            children,
                                            ClassName.get(ArrayList.class))
                                        .beginControlFlow("for ($T $N : $T.values())", m_selfClass, parent, m_selfClass)
                                        .beginControlFlow("for ($T $N : $T.values())", m_selfClass, child, m_selfClass)
                                        .beginControlFlow("if ($N == $N)", child, parent)
                                        .addStatement("continue")
                                        .endControlFlow()
                                        .beginControlFlow("if ($N.$N() == $N)", child, m_getParent, parent)
                                        .addStatement("$N.add($N)", children, child)
                                        .endControlFlow()
                                        .endControlFlow()
                                        .addStatement("$T $N = new $T[$N.size()]", m_selfClassArray, childrenArray, m_selfClass, children)
                                        .addStatement("$N.$N($N.toArray($N))", parent, m_setChildren, children, childrenArray)
                                        .addStatement("$N.clear()", children)
                                        .endControlFlow()
                                        .build());
    }
}
