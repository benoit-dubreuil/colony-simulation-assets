package com.cheesygames.colonysimulation.assets;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;

/**
 * This is a generated type. Do not modify this file as it can be frequently updated without any indication. To know more about what generated this class, please see the {@link com.cheesygames.colonysimulation.assets.generator.AssetGenerator generator's javadoc}. 
 * 
 * Holds IDs for all the game assets. */
public enum Asset implements IAsset {
  followEnemy(AssetType.BEHAVIOR, "behavior/follow_enemy/followEnemy.btree", AssetHierarchy.BEHAVIOR),

  extendedNotoSans(AssetType.BITMAP_FONT, "bitmap_font/extended_noto_sans_CJK/extended_noto_sans/extendedNotoSans.fnt", AssetHierarchy.BITMAP_FONT_EXTENDED_NOTO_SANS_CJK),

  notoSansJP_Blk_at_16(AssetType.BITMAP_FONT, "bitmap_font/noto_JP/noto_sans_JP_Blk/notoSansJP_Blk_at_16.fnt", AssetHierarchy.BITMAP_FONT_NOTO_JP),

  notoSansJP_Blk_at_20(AssetType.BITMAP_FONT, "bitmap_font/noto_JP/noto_sans_JP_Blk/notoSansJP_Blk_at_20.fnt", AssetHierarchy.BITMAP_FONT_NOTO_JP),

  notoSansJP_Blk_at_40(AssetType.BITMAP_FONT, "bitmap_font/noto_JP/noto_sans_JP_Blk/notoSansJP_Blk_at_40.fnt", AssetHierarchy.BITMAP_FONT_NOTO_JP),

  notoSansJP_Blk_at_60(AssetType.BITMAP_FONT, "bitmap_font/noto_JP/noto_sans_JP_Blk/notoSansJP_Blk_at_60.fnt", AssetHierarchy.BITMAP_FONT_NOTO_JP),

  notoSansJP_Blk_at_8(AssetType.BITMAP_FONT, "bitmap_font/noto_JP/noto_sans_JP_Blk/notoSansJP_Blk_at_8.fnt", AssetHierarchy.BITMAP_FONT_NOTO_JP),

  robotoBk(AssetType.BITMAP_FONT, "bitmap_font/roboto/roboto_Bk/robotoBk.fnt", AssetHierarchy.BITMAP_FONT_ROBOTO),

  cross(AssetType.GUI, "gui/hud/cross.png", AssetHierarchy.GUI),

  happyboy_notification(AssetType.GUI, "gui/hud/notification/happyboy_notification.png", AssetHierarchy.GUI_HUD),

  notification_background(AssetType.GUI, "gui/hud/notification/notification_background.png", AssetHierarchy.GUI_HUD),

  notification_generic(AssetType.GUI, "gui/hud/notification/notification_generic.png", AssetHierarchy.GUI_HUD),

  notification_money(AssetType.GUI, "gui/hud/notification/notification_money.png", AssetHierarchy.GUI_HUD),

  sadboy_notification(AssetType.GUI, "gui/hud/notification/sadboy_notification.png", AssetHierarchy.GUI_HUD),

  zigzag_background_bar(AssetType.GUI, "gui/hud/optionPanel/zigzag_background_bar.png", AssetHierarchy.GUI_HUD),

  zigzag_header_bar(AssetType.GUI, "gui/hud/optionPanel/zigzag_header_bar.png", AssetHierarchy.GUI_HUD),

  fern(AssetType.MODEL, "model/environment/fern/fern.j3o", AssetHierarchy.MODEL_ENVIRONMENT),

  fern_material(AssetType.MATERIAL, "material/environment/fern/fern_material.j3m", AssetHierarchy.MATERIAL_ENVIRONMENT),

  crystal_default_core(AssetType.MATERIAL, "material/item/consumable/crystal/crystal_default/crystal_default_core.j3m", AssetHierarchy.MATERIAL_ITEM_CONSUMABLE_CRYSTAL),

  crystal_default_shell(AssetType.MATERIAL, "material/item/consumable/crystal/crystal_default/crystal_default_shell.j3m", AssetHierarchy.MATERIAL_ITEM_CONSUMABLE_CRYSTAL),

  fresnel_outline(AssetType.MATERIAL, "material/item/fresnel_outline/fresnel_outline.j3m", AssetHierarchy.MATERIAL_ITEM),

  player_material(AssetType.MATERIAL, "material/unit/man/player_material.j3m", AssetHierarchy.MATERIAL_UNIT),

  punchingbag_material(AssetType.MATERIAL, "material/unit/man/punchingbag_material.j3m", AssetHierarchy.MATERIAL_UNIT),

  atlas_palette_material(AssetType.MATERIAL, "material/world_colors/atlas_palette_material.j3m", AssetHierarchy.MATERIAL),

  atlas_palette_material_double_sided(AssetType.MATERIAL, "material/world_colors/atlas_palette_material_double_sided.j3m", AssetHierarchy.MATERIAL),

  fresnel_outline_material_definition(AssetType.MATERIAL_DEFINITION, "material_definition/fresnel_outline/fresnel_outline_material_definition.j3md", AssetHierarchy.MATERIAL_DEFINITION),

  atlas_palette_material_definition(AssetType.MATERIAL_DEFINITION, "material_definition/world_colors/atlas_palette_material_definition.j3md", AssetHierarchy.MATERIAL_DEFINITION),

  palmtree(AssetType.MODEL, "model/environment/palmtree/palmtree.j3o", AssetHierarchy.MODEL_ENVIRONMENT),

  tree_leaful(AssetType.MODEL, "model/environment/tree_leafful/tree_leaful.j3o", AssetHierarchy.MODEL_ENVIRONMENT),

  tree_leafless(AssetType.MODEL, "model/environment/tree_leafless/tree_leafless.j3o", AssetHierarchy.MODEL_ENVIRONMENT),

  crystal(AssetType.MODEL, "model/item/consumable/crystal/crystal.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE),

  banana(AssetType.MODEL, "model/item/consumable/food/banana/banana.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE_FOOD),

  banana_low(AssetType.MODEL, "model/item/consumable/food/banana_low/banana_low.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE_FOOD),

  lime(AssetType.MODEL, "model/item/consumable/food/lime/lime.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE_FOOD),

  pineaburger(AssetType.MODEL, "model/item/consumable/food/pineaburger/pineaburger.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE_FOOD),

  pineapple(AssetType.MODEL, "model/item/consumable/food/pineapple/pineapple.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE_FOOD),

  pineapple_low(AssetType.MODEL, "model/item/consumable/food/pineapple_low/pineapple_low.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE_FOOD),

  buck(AssetType.MODEL, "model/item/consumable/money/buck/buck.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE_MONEY),

  coin(AssetType.MODEL, "model/item/consumable/money/coin/coin.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE_MONEY),

  ingot(AssetType.MODEL, "model/item/consumable/money/ingot/ingot.j3o", AssetHierarchy.MODEL_ITEM_CONSUMABLE_MONEY),

  saber(AssetType.MODEL, "model/item/equipment/weapon/saber/saber.j3o", AssetHierarchy.MODEL_ITEM_EQUIPMENT_WEAPON),

  sword(AssetType.MODEL, "model/item/equipment/weapon/sword/sword.j3o", AssetHierarchy.MODEL_ITEM_EQUIPMENT_WEAPON),

  augmented_consciousness(AssetType.MODEL, "model/item/relic/augmented_consciousness/augmented_consciousness.j3o", AssetHierarchy.MODEL_ITEM_RELIC),

  electronic_mask(AssetType.MODEL, "model/item/relic/electronic_mask/electronic_mask.j3o", AssetHierarchy.MODEL_ITEM_RELIC),

  man(AssetType.MODEL, "model/unit/man/man.j3o", AssetHierarchy.MODEL_UNIT),

  atlas_palette_texture(AssetType.TEXTURE, "texture/world_colors/atlas_palette_texture.png", AssetHierarchy.TEXTURE);

  public static AssetManager assetManager;

  private final AssetType m_assetType;

  private final String m_path;

  @SuppressWarnings("rawtypes")
  protected final AssetKey m_assetKey;

  private final AssetHierarchy m_assetHierarchy;

  Asset(AssetType assetType, String path, AssetHierarchy assetHierarchy) {
    this.m_assetType = assetType;
    this.m_path = path;
    this.m_assetKey = assetType.createAssetKey(path);
    this.m_assetHierarchy = assetHierarchy;
  }

  @Override
  public AssetType getAssetType() {
    return m_assetType;
  }

  @Override
  public String getPath() {
    return m_path;
  }

  @Override
  public <T extends AssetKey<?>> T getAssetKey() {
    // noinspection unchecked
    return (T) m_assetKey;
  }

  public AssetHierarchy getAssetHierarchy() {
    return m_assetHierarchy;
  }

  public <T> T loadAsset() {
    // noinspection unchecked
    return (T) assetManager.loadAsset(getAssetKey());
  }
}
