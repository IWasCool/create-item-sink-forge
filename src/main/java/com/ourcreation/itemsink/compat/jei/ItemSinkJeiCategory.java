package com.ourcreation.itemsink.compat.jei;

import com.ourcreation.itemsink.ItemSink;
import com.ourcreation.itemsink.compat.ItemSinkConversionRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class ItemSinkJeiCategory implements IRecipeCategory<ItemSinkConversionRecipe> {
    public static final RecipeType<ItemSinkConversionRecipe> TYPE = RecipeType.create(
            ItemSink.MODID,
            "item_sink_conversion",
            ItemSinkConversionRecipe.class
    );

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public ItemSinkJeiCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(116, 44);
        icon = guiHelper.createDrawableItemStack(new ItemStack(ItemSink.ITEM_SINK_ITEM.get()));
        arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public RecipeType<ItemSinkConversionRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.item_sink.item_sink_conversion");
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemSinkConversionRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(8, 10)
                .setStandardSlotBackground()
                .addItemStack(recipe.input());
        builder.addOutputSlot(90, 10)
                .setOutputSlotBackground()
                .addItemStack(recipe.output());
    }

    @Override
    public void draw(ItemSinkConversionRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 45, 11);
        Component details = Component.translatable("jei.item_sink.details",
                String.format(Locale.ROOT, "%.1f", recipe.ticks() / 20.0F), recipe.energyPerTick());
        guiGraphics.drawString(Minecraft.getInstance().font, details, 8, 34, 0x555555, false);
    }

    @Override
    public ResourceLocation getRegistryName(ItemSinkConversionRecipe recipe) {
        return recipe.id();
    }
}
