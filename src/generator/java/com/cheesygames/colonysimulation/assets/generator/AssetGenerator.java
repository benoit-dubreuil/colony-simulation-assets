package com.cheesygames.colonysimulation.assets.generator;

import com.cheesygames.colonysimulation.assets.AssetType;
import com.cheesygames.colonysimulation.assets.IAsset;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.squareup.javapoet.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The Asset Generator creates an enum that contains IDs for all correctly placed and supported assets. For an asset to be correctly placed, its extension must correspond to one of
 * its parent directory as described in the {@link AssetType AssetType} enum. The specified parent directory must have the same name
 * (capitalization doesn't matter) as one of the enum value.
 */
public final class AssetGenerator implements IGenerator {

    private static final String ASSET_MANAGER_NAME = "assetManager";

    private static final String ENUM_NAME = "Asset";
    private static final String ENUM_ASSET_TYPE_VAR_NAME = "assetType";
    private static final String ENUM_PATH_VAR_NAME = "path";
    private static final String ENUM_ASSET_KEY_VAR_NAME = "assetKey";
    private static final String ENUM_ASSET_HIERARCHY_VAR_NAME = "assetHierarchy";
    private static final String ENUM_ASSET_TYPE_VAR_M_NAME = VAR_M_ + ENUM_ASSET_TYPE_VAR_NAME;
    private static final String ENUM_PATH_VAR_M_NAME = VAR_M_ + ENUM_PATH_VAR_NAME;
    private static final String ENUM_ASSET_KEY_VAR_M_NAME = VAR_M_ + ENUM_ASSET_KEY_VAR_NAME;
    private static final String ENUM_ASSET_HIERARCHY_VAR_M_NAME = VAR_M_ + ENUM_ASSET_HIERARCHY_VAR_NAME;

    private static final String ENUM_LOAD_ASSET_METHOD_NAME = "loadAsset";

    private static final String GENERIC_TYPE = "T";

    private static final String ANNOTATION_SUPPRESS_WARNINGS_RAWTYPES_VALUE = "rawtypes";
    private static final String EXPRESSION_SUPPRESS_WARNINGS_UNCHECKED_COMMENT = "noinspection unchecked";

    private static final String JAVADOC = "Holds IDs for all the game assets.";

    private ClassName m_assetHierarchyType;

    public AssetGenerator() {
        this.m_assetHierarchyType = MainGenerator.computeTypeClassName(AssetHierarchyGenerator.ENUM_NAME);
    }

    public TypeSpec buildTypeSpec(List<File> assetDirectories, File[] assets) {
        TypeSpec.Builder builder = TypeSpec.enumBuilder(ENUM_NAME).addModifiers(Modifier.PUBLIC);

        builder.addJavadoc(GeneratorTypeJavadocFactory.createJavadoc(AssetGenerator.class, JAVADOC));
        builder.addSuperinterface(IAsset.class);
        addAssetManager(builder);
        addConstructor(builder);
        addAssetTypeVar(builder);
        addPathVar(builder);
        addAssetKeyVar(builder);
        addAssetHierarchyVar(builder);
        addLoadAssetMethod(builder);

        fillEnumConstants(assets, builder);

        return builder.build();
    }

    /**
     * Adds the assetManager public static variable.
     *
     * @param builder The enum's type specification builder.
     */
    private void addAssetManager(TypeSpec.Builder builder) {
        builder.addField(AssetManager.class, ASSET_MANAGER_NAME, Modifier.PUBLIC, Modifier.STATIC);
    }

    /**
     * Fills the enum with constants.
     *
     * @param assets  All the assets.
     * @param builder The enum's type specification builder.
     */
    private void fillEnumConstants(File[] assets, TypeSpec.Builder builder) {
        List<AssetType> assetTypes = new ArrayList<>(Arrays.asList(AssetType.values()));

        for (File asset : assets) {
            File parentHierarchyFolder = asset.getParentFile().getParentFile();
            String[] explodedHierarchy = parentHierarchyFolder.getPath().split(Pattern.quote(File.separator));

            AssetType assetTypeRoot = null;
            for (int i = 0; i < explodedHierarchy.length; ++i) {
                if (assetTypeRoot == null) {
                    for (int assetTypeIndex = 0; assetTypeIndex < assetTypes.size() && assetTypeRoot == null; ++assetTypeIndex) {
                        if (assetTypes.get(assetTypeIndex).name().toLowerCase().equals(explodedHierarchy[i].toLowerCase())) {
                            assetTypeRoot = assetTypes.get(assetTypeIndex);
                            --i; // To allow sub-folders with assets directly under the root asset type folder.
                        }
                    }
                }
                else if (explodedHierarchy[i].equals(parentHierarchyFolder.getName())) {
                    StringBuilder assetNameBuilder = new StringBuilder();
                    StringBuilder assetExtensionBuilder = new StringBuilder();
                    splitAssetNameAndExtension(asset.getName(), assetNameBuilder, assetExtensionBuilder);

                    if (assetTypeRoot.getSupportedFormats().contains(assetExtensionBuilder.toString())) {
                        String assetPath = asset.getPath();
                        String assetHierarchy = constructAssetHierarchy(explodedHierarchy[i], assetTypeRoot, assetPath);

                        TypeSpec.Builder enumConstantBuilder = TypeSpec.anonymousClassBuilder(assembleTypeArgumentsFormat(),
                            AssetType.class.getSimpleName(),
                            assetTypeRoot.name(),
                            assetPath.replace(File.separatorChar, FILE_SEPARATOR),
                            AssetHierarchyGenerator.ENUM_NAME,
                            assetHierarchy);

                        builder.addEnumConstant(assetNameBuilder.toString(), enumConstantBuilder.build());
                        break;
                    }
                }
            }
        }
    }

    /**
     * Constructs the asset hierarchy parameter.
     *
     * @param topHierarchy  The topmost hierarchy folder excluding the one specifically for the asset.
     * @param assetTypeRoot The asset type root folder for this asset.
     * @param assetPath     The complete relative path of this asset.
     *
     * @return The asset hierarchy parameter.
     */
    private String constructAssetHierarchy(String topHierarchy, AssetType assetTypeRoot, String assetPath) {
        return assetPath.substring(assetPath.indexOf(assetTypeRoot.name().toLowerCase()), assetPath.indexOf(topHierarchy) + topHierarchy.length())
                        .replace(File.separatorChar, ENUM_CONSTANT_NAME_SEPARATOR)
                        .toUpperCase();
    }

    private String assembleTypeArgumentsFormat() {
        StringBuilder typeArgumentsFormat = new StringBuilder();

        addTypeArgumentFormat(typeArgumentsFormat, "$L.$L");
        addTypeArgumentFormat(typeArgumentsFormat, "$S");
        addTypeArgumentFormat(typeArgumentsFormat, "$L.$L");

        return typeArgumentsFormat.toString();
    }

    private void addTypeArgumentFormat(StringBuilder typeArgumentsFormat, String typeArgumentFormat) {
        if (typeArgumentsFormat.length() != 0) {
            typeArgumentsFormat.append(PARAMETER_SEPARATOR);
        }

        typeArgumentsFormat.append(typeArgumentFormat);
    }

    /**
     * Splits an asset full name into its extension and its shorter name.
     *
     * @param assetFullName     The full name of the asset.
     * @param outAssetName      The output parameter for the shorter asset name.
     * @param outAssetExtension The out parameter for the extension.
     */
    private void splitAssetNameAndExtension(String assetFullName, StringBuilder outAssetName, StringBuilder outAssetExtension) {
        outAssetName.append(assetFullName);
        while (!FilenameUtils.getExtension(outAssetName.toString()).isEmpty()) {
            if (!outAssetExtension.toString().isEmpty()) {
                outAssetExtension.insert(0, '.');
            }

            outAssetExtension.insert(0, FilenameUtils.getExtension(outAssetName.toString()));

            String tmpRemovedExtension = FilenameUtils.removeExtension(outAssetName.toString());
            outAssetName.setLength(0);
            outAssetName.append(tmpRemovedExtension);
        }
    }

    /**
     * Adds a basic constructor that sets the TypeSpec's member variable(s).
     *
     * @param builder The owner of the constructor.
     */
    private void addConstructor(TypeSpec.Builder builder) {
        builder.addMethod(MethodSpec.constructorBuilder()
                                    .addParameter(AssetType.class, ENUM_ASSET_TYPE_VAR_NAME)
                                    .addParameter(String.class, ENUM_PATH_VAR_NAME)
                                    .addParameter(m_assetHierarchyType, ENUM_ASSET_HIERARCHY_VAR_NAME)
                                    .addStatement("this.$L = $L", ENUM_ASSET_TYPE_VAR_M_NAME, ENUM_ASSET_TYPE_VAR_NAME)
                                    .addStatement("this.$L = $L", ENUM_PATH_VAR_M_NAME, ENUM_PATH_VAR_NAME)
                                    .addStatement("this.$L = $L.createAssetKey($L)", ENUM_ASSET_KEY_VAR_M_NAME, ENUM_ASSET_TYPE_VAR_NAME, ENUM_PATH_VAR_NAME)
                                    .addStatement("this.$L = $L", ENUM_ASSET_HIERARCHY_VAR_M_NAME, ENUM_ASSET_HIERARCHY_VAR_NAME)
                                    .build());
    }

    /**
     * Adds the member variable m_assetType to the enum and also its getter.
     *
     * @param builder The TypeSpec.Builder to attach the member variable and its getter.
     */
    private void addAssetTypeVar(TypeSpec.Builder builder) {
        builder.addField(AssetType.class, ENUM_ASSET_TYPE_VAR_M_NAME, Modifier.PRIVATE, Modifier.FINAL);

        builder.addMethod(MethodSpec.methodBuilder(MEMBER_VAR_GET + StringUtils.capitalize(ENUM_ASSET_TYPE_VAR_NAME))
                                    .addAnnotation(Override.class)
                                    .addModifiers(Modifier.PUBLIC)
                                    .addStatement("return $L", ENUM_ASSET_TYPE_VAR_M_NAME)
                                    .returns(AssetType.class)
                                    .build());
    }

    /**
     * Adds the member variable m_path to the enum and also its getter.
     *
     * @param builder The TypeSpec.Builder to attach the member variable and its getter.
     */
    private void addPathVar(TypeSpec.Builder builder) {
        builder.addField(String.class, ENUM_PATH_VAR_M_NAME, Modifier.PRIVATE, Modifier.FINAL);

        builder.addMethod(MethodSpec.methodBuilder(MEMBER_VAR_GET + StringUtils.capitalize(ENUM_PATH_VAR_NAME))
                                    .addAnnotation(Override.class)
                                    .addModifiers(Modifier.PUBLIC)
                                    .addStatement("return $L", ENUM_PATH_VAR_M_NAME)
                                    .returns(String.class)
                                    .build());
    }

    /**
     * Adds the member variable m_assetKey to the enum and also its getter.
     *
     * @param builder The TypeSpec.Builder to attach the member variable and its getter.
     */
    private void addAssetKeyVar(TypeSpec.Builder builder) {
        TypeVariableName genericTypeName = TypeVariableName.get(GENERIC_TYPE, ParameterizedTypeName.get(ClassName.get(AssetKey.class), WildcardTypeName.subtypeOf(Object.class)));

        AnnotationSpec suppressRawtypesWarning = AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", ANNOTATION_SUPPRESS_WARNINGS_RAWTYPES_VALUE).build();
        builder.addField(FieldSpec.builder(AssetKey.class, ENUM_ASSET_KEY_VAR_M_NAME, Modifier.PROTECTED, Modifier.FINAL).addAnnotation(suppressRawtypesWarning).build());

        builder.addMethod(MethodSpec.methodBuilder(MEMBER_VAR_GET + StringUtils.capitalize(ENUM_ASSET_KEY_VAR_NAME))
                                    .addAnnotation(Override.class)
                                    .addModifiers(Modifier.PUBLIC)
                                    .addTypeVariable(genericTypeName)
                                    .addComment("$L", EXPRESSION_SUPPRESS_WARNINGS_UNCHECKED_COMMENT)
                                    .addStatement("return ($L) $L", genericTypeName, ENUM_ASSET_KEY_VAR_M_NAME)
                                    .returns(genericTypeName)
                                    .build());
    }

    /**
     * Adds the member variable m_assetHierarchy to the enum and also its getter.
     *
     * @param builder The TypeSpec.Builder to attach the member variable and its getter.
     */
    private void addAssetHierarchyVar(TypeSpec.Builder builder) {
        builder.addField(m_assetHierarchyType, ENUM_ASSET_HIERARCHY_VAR_M_NAME, Modifier.PRIVATE, Modifier.FINAL);

        builder.addMethod(MethodSpec.methodBuilder(MEMBER_VAR_GET + StringUtils.capitalize(ENUM_ASSET_HIERARCHY_VAR_NAME))
                                    .addModifiers(Modifier.PUBLIC)
                                    .addStatement("return $L", ENUM_ASSET_HIERARCHY_VAR_M_NAME)
                                    .returns(m_assetHierarchyType)
                                    .build());
    }

    /**
     * Adds the base method for loading assets.
     *
     * @param builder The TypeSpec.Builder to attach the member variable and its getter.
     */
    private void addLoadAssetMethod(TypeSpec.Builder builder) {
        TypeVariableName genericTypeName = TypeVariableName.get(GENERIC_TYPE);

        builder.addMethod(MethodSpec.methodBuilder(ENUM_LOAD_ASSET_METHOD_NAME)
                                    .addModifiers(Modifier.PUBLIC)
                                    .addTypeVariable(genericTypeName)
                                    .addComment("$L", EXPRESSION_SUPPRESS_WARNINGS_UNCHECKED_COMMENT)
                                    .addStatement("return ($L) $L.$L($L())",
                                        GENERIC_TYPE,
                                        ASSET_MANAGER_NAME,
                                        ENUM_LOAD_ASSET_METHOD_NAME,
                                        MEMBER_VAR_GET + StringUtils.capitalize(ENUM_ASSET_KEY_VAR_NAME))
                                    .returns(genericTypeName)
                                    .build());
    }
}
