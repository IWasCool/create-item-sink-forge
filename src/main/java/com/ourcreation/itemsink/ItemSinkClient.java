package com.ourcreation.itemsink;

import com.ourcreation.itemsink.client.ItemSinkScreen;
import com.ourcreation.itemsink.client.ponder.ItemSinkPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ItemSink.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ItemSinkClient {
    private ItemSinkClient() {
    }

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new ItemSinkPonderPlugin());
        event.enqueueWork(() -> MenuScreens.register(ItemSink.ITEM_SINK_MENU.get(), ItemSinkScreen::new));
    }
}
