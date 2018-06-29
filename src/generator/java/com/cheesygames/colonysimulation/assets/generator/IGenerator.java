package com.cheesygames.colonysimulation.assets.generator;

import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.util.List;

/**
 * Interface for all generators in the asset project.
 */
public interface IGenerator {

    String MEMBER_VAR_GET = "get";
    String MEMBER_VAR_SET = "set";
    String PARAMETER_SEPARATOR = ", ";
    String VAR_M_ = "m_";
    char ENUM_CONSTANT_NAME_SEPARATOR = '_';
    char FILE_SEPARATOR = '/';

    /**
     * Builds the type specification, which then allows the option to actually write down the code.
     *
     * @param assetDirectories The asset directories.
     * @param assets           All the assets to be used in the game.
     *
     * @return A built type specification.
     */
    TypeSpec buildTypeSpec(List<File> assetDirectories, File[] assets);
}
