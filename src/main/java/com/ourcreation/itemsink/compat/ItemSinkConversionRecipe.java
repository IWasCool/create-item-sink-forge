package com.ourcreation.itemsink.compat;

import com.ourcreation.itemsink.Config;
import com.ourcreation.itemsink.ItemSink;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public record ItemSinkConversionRecipe(ResourceLocation id, ItemStack input, ItemStack output, int ticks, int energyPerTick) {
    public static List<ItemSinkConversionRecipe> fromConfig() {
        List<ItemSinkConversionRecipe> recipes = new ArrayList<>();
        Set<ResourceLocation> seenInputs = new LinkedHashSet<>();

        for (Config.Exchange exchange : Config.exchanges()) {
            if (!seenInputs.add(exchange.input())) {
                continue;
            }

            BuiltInRegistries.ITEM.getOptional(exchange.input()).ifPresent(inputItem -> {
                ItemStack output = exchange.createOutput();
                if (!output.isEmpty()) {
                    recipes.add(new ItemSinkConversionRecipe(
                            recipeId(exchange.input()),
                            new ItemStack(inputItem),
                            output,
                            exchange.ticks(),
                            exchange.energyPerTick()
                    ));
                }
            });
        }

        return recipes;
    }

    private static ResourceLocation recipeId(ResourceLocation input) {
        return ResourceLocation.fromNamespaceAndPath(
                ItemSink.MODID,
                "item_sink_conversion/" + input.getNamespace() + "/" + input.getPath()
        );
    }
}
