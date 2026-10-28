package com.ourcreation.itemsink;

import com.mojang.logging.LogUtils;
import com.ourcreation.itemsink.block.ItemSinkBlock;
import com.ourcreation.itemsink.block.entity.ItemSinkBlockEntity;
import com.ourcreation.itemsink.menu.ItemSinkMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(ItemSink.MODID)
public class ItemSink {
    public static final String MODID = "item_sink";
    public static final String BLOCK_NAMESPACE = "item_sink";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, BLOCK_NAMESPACE);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, BLOCK_NAMESPACE);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);

    public static final RegistryObject<ItemSinkBlock> ITEM_SINK = BLOCKS.register("item_sink", ItemSinkBlock::new);
    public static final RegistryObject<BlockItem> ITEM_SINK_ITEM = ITEMS.register("item_sink", () -> new BlockItem(ITEM_SINK.get(), new Item.Properties()));

    public static final RegistryObject<BlockEntityType<ItemSinkBlockEntity>> ITEM_SINK_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("item_sink", () -> BlockEntityType.Builder.of(ItemSinkBlockEntity::new, ITEM_SINK.get()).build(null));

    public static final RegistryObject<MenuType<ItemSinkMenu>> ITEM_SINK_MENU =
            MENU_TYPES.register("item_sink", () -> IForgeMenuType.create(ItemSinkMenu::new));

    public static final RegistryObject<CreativeModeTab> ITEM_SINK_TAB = CREATIVE_MODE_TABS.register("item_sink", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.item_sink"))
            .icon(() -> ITEM_SINK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> output.accept(ITEM_SINK_ITEM.get()))
            .build());

    public static final RegistryObject<SoundEvent> ELECTRIC_MOTOR_BUZZ =
            SOUND_EVENTS.register("electric_motor_buzz", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "electric_motor_buzz")));

    public ItemSink(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        modEventBus.addListener(this::onConfigLoaded);
        modEventBus.addListener(this::onConfigReloaded);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        MinecraftForge.EVENT_BUS.addListener(this::onTagsUpdated);
        migrateLegacyConfig();
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void migrateLegacyConfig() {
        var configDirectory = net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get();
        var legacy = configDirectory.resolve("create_item_sink-common.toml");
        var current = configDirectory.resolve("item_sink-common.toml");
        if (java.nio.file.Files.exists(legacy) && !java.nio.file.Files.exists(current)) {
            try {
                java.nio.file.Files.copy(legacy, current);
            } catch (java.io.IOException error) {
                throw new IllegalStateException("Could not migrate Item Sink configuration", error);
            }
        }
    }

    private void onConfigLoaded(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == Config.SPEC) {
            if (Config.compactLegacyDefaults()) {
                Config.SPEC.save();
            }
            Config.invalidateExchanges();
        }
    }

    private void onConfigReloaded(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.SPEC) {
            Config.invalidateExchanges();
        }
    }

    private void onTagsUpdated(TagsUpdatedEvent event) {
        Config.invalidateExchanges();
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!Config.hasValidExchange()) {
            event.getEntity().sendSystemMessage(Component.translatable(
                    "message.item_sink.no_valid_outputs", "Create: Numismatics",
                    Component.translatable("item_sink.configuration.item_sink.values.outputItem"),
                    "item_sink-common.toml"
            ));
        }
    }

}
