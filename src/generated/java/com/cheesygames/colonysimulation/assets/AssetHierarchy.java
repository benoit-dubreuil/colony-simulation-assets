package com.cheesygames.colonysimulation.assets;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a generated type. Do not modify this file as it can be frequently updated without any indication. To know more about what generated this class, please see the {@link com.cheesygames.colonysimulation.assets.generator.AssetGenerator generator's javadoc}. 
 * 
 * Asset directory hierarchy. It allows to bidirectionally traverse the hierarchy tree. */
public enum AssetHierarchy {
  AUDIO,

  BEHAVIOR,

  BITMAP_FONT,

  BITMAP_FONT_EXTENDED_NOTO_SANS_CJK(BITMAP_FONT),

  BITMAP_FONT_NOTO_JP(BITMAP_FONT),

  BITMAP_FONT_ROBOTO(BITMAP_FONT),

  GUI,

  GUI_HUD(GUI),

  MATERIAL,

  MATERIAL_ENVIRONMENT(MATERIAL),

  MATERIAL_ITEM(MATERIAL),

  MATERIAL_ITEM_CONSUMABLE(MATERIAL_ITEM),

  MATERIAL_ITEM_CONSUMABLE_CRYSTAL(MATERIAL_ITEM_CONSUMABLE),

  MATERIAL_ITEM_EQUIPMENT(MATERIAL_ITEM),

  MATERIAL_UNIT(MATERIAL),

  MATERIAL_DEFINITION,

  MODEL,

  MODEL_ENVIRONMENT(MODEL),

  MODEL_ITEM(MODEL),

  MODEL_ITEM_CONSUMABLE(MODEL_ITEM),

  MODEL_ITEM_CONSUMABLE_FOOD(MODEL_ITEM_CONSUMABLE),

  MODEL_ITEM_CONSUMABLE_MONEY(MODEL_ITEM_CONSUMABLE),

  MODEL_ITEM_EQUIPMENT(MODEL_ITEM),

  MODEL_ITEM_EQUIPMENT_WEAPON(MODEL_ITEM_EQUIPMENT),

  MODEL_ITEM_RELIC(MODEL_ITEM),

  MODEL_UNIT(MODEL),

  SHADER,

  SHADER_FRAGMENT(SHADER),

  SHADER_VERTEX(SHADER),

  TEXTURE;

  static {
    List<AssetHierarchy> children = new ArrayList<>();
    for (AssetHierarchy parent : AssetHierarchy.values()) {
      for (AssetHierarchy child : AssetHierarchy.values()) {
        if (child == parent) {
          continue;
        }
        if (child.getParent() == parent) {
          children.add(child);
        }
      }
      AssetHierarchy[] childrenArray = new AssetHierarchy[children.size()];
      parent.setChildren(children.toArray(childrenArray));
      children.clear();
    }
  }

  private final AssetHierarchy m_parent;

  private AssetHierarchy[] m_children;

  AssetHierarchy() {
    this.m_parent = null;
  }

  AssetHierarchy(AssetHierarchy parent) {
    this.m_parent = parent;
  }

  public AssetHierarchy getParent() {
    return m_parent;
  }

  public AssetHierarchy[] getChildren() {
    return m_children;
  }

  private void setChildren(AssetHierarchy[] children) {
    this.m_children = children;
  }
}
