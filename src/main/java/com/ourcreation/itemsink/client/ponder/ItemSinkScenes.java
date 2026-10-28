package com.ourcreation.itemsink.client.ponder;

import com.ourcreation.itemsink.Config;
import com.ourcreation.itemsink.block.ItemSinkBlock;
import com.ourcreation.itemsink.block.entity.ItemSinkBlockEntity;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class ItemSinkScenes {
    private static final BlockPos SINK = new BlockPos(3, 2, 3);
    private static final BlockPos INPUT_HOPPER = SINK.above();
    private static final BlockPos INPUT_CHEST = INPUT_HOPPER.above();
    private static final BlockPos OUTPUT_HOPPER = SINK.below();
    private static final BlockPos OUTPUT_CHEST = OUTPUT_HOPPER.west();

    private ItemSinkScenes() {
    }

    public static void basics(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("item_sink_basics", "Using the Item Sink");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(OUTPUT_HOPPER, SINK), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(70)
                .text("Item Sinks turn unwanted items into currency, one item per processing cycle.")
                .pointAt(util.vector().centerOf(SINK))
                .placeNearTarget();
        scene.idle(80);

        Vec3 front = util.vector().blockSurface(SINK, Direction.SOUTH);
        scene.overlay().showControls(front, Pointing.RIGHT, 60).rightClick();
        scene.overlay().showText(90)
                .text("Right-click to open the machine. Place an accepted item in the input slot on the left.")
                .pointAt(front)
                .placeNearTarget()
                .attachKeyFrame();
        scene.idle(100);

        Example example = example();
        scene.overlay().showControls(front, Pointing.RIGHT, 50).withItem(example.input());
        moveItem(scene, front.add(-2, 0.5, 0), front, example.input(), 20);
        scene.world().modifyBlockEntity(SINK, ItemSinkBlockEntity.class,
                sink -> sink.getInventory().setStackInSlot(ItemSinkBlockEntity.INPUT_SLOT, example.input().copy()));
        scene.overlay().showText(90)
                .text("Only configured inputs with an available currency output are accepted. With JEI installed, check Item Sink Conversion recipes.")
                .pointAt(front)
                .placeNearTarget();
        scene.idle(100);

        showPower(scene, util, 100);
        scene.overlay().showText(100)
                .text("By default, the Item Sink needs Forge Energy (FE) from a generator or battery. Power can enter through any side; the energy bar is on the left of the screen.")
                .pointAt(util.vector().blockSurface(SINK, Direction.EAST))
                .placeNearTarget()
                .colored(PonderPalette.BLUE)
                .attachKeyFrame();
        scene.idle(110);

        scene.overlay().showText(90)
                .text("The machine lights up while processing. The progress arrow fills as it consumes FE. Time and FE cost depend on the item's value tier.")
                .pointAt(front)
                .placeNearTarget();
        animateConversion(scene, example);
        scene.idle(40);

        scene.overlay().showControls(front, Pointing.RIGHT, 70).withItem(example.output());
        scene.overlay().showText(90)
                .text("Collect the currency from the two output slots on the right. Each completed cycle consumes one input item.")
                .pointAt(front)
                .placeNearTarget()
                .colored(PonderPalette.GREEN)
                .attachKeyFrame();
        moveItem(scene, front, front.add(-1.5, 0.5, 0), example.output(), 25);
        scene.world().modifyBlockEntity(SINK, ItemSinkBlockEntity.class,
                sink -> sink.getInventory().setStackInSlot(ItemSinkBlockEntity.OUTPUT_SLOT_1, ItemStack.EMPTY));
        scene.idle(75);

        scene.overlay().showText(140)
                .text("Default outputs are Create: Numismatics coins. Without Numismatics, set outputItem to an installed item, such as minecraft:emerald, in item_sink-common.toml.")
                .pointAt(front)
                .placeNearTarget();
        scene.idle(150);
        scene.markAsFinished();
    }

    public static void automation(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("item_sink_automation", "Automating the Item Sink");
        scene.configureBasePlate(0, 0, 7);
        scene.scaleSceneView(0.85F);
        scene.setSceneOffsetY(-0.5F);
        scene.showBasePlate();
        scene.world().setBlock(OUTPUT_HOPPER, Blocks.HOPPER.defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.WEST), false);
        scene.world().showSection(util.select().position(SINK), Direction.DOWN);
        scene.idle(20);

        scene.world().showSection(util.select().fromTo(INPUT_HOPPER, INPUT_CHEST), Direction.DOWN);
        scene.overlay().showText(90)
                .text("Feed accepted items into the Item Sink with a hopper. This chest and hopper supply the input slot from above.")
                .pointAt(util.vector().centerOf(INPUT_HOPPER))
                .placeNearTarget()
                .colored(PonderPalette.INPUT)
                .attachKeyFrame();
        Example example = example();
        scene.world().modifyBlockEntity(INPUT_CHEST, ChestBlockEntity.class,
                chest -> chest.setItem(0, example.input().copyWithCount(16)));
        scene.idle(40);
        moveItem(scene, util.vector().centerOf(INPUT_CHEST).add(0, 0, 0.7),
                util.vector().centerOf(SINK).add(0, 0, 0.7), example.input(), 25);
        scene.world().modifyBlockEntity(INPUT_CHEST, ChestBlockEntity.class,
                chest -> chest.setItem(0, example.input().copyWithCount(15)));
        scene.world().modifyBlockEntity(SINK, ItemSinkBlockEntity.class,
                sink -> sink.getInventory().setStackInSlot(ItemSinkBlockEntity.INPUT_SLOT, example.input().copy()));
        scene.idle(35);

        scene.world().showSection(util.select().fromTo(OUTPUT_CHEST, OUTPUT_HOPPER), Direction.UP);
        scene.overlay().showText(90)
                .text("A second hopper extracts currency into a chest. Automation extracts only from the two output slots, so unprocessed inputs stay inside.")
                .pointAt(util.vector().centerOf(OUTPUT_HOPPER))
                .placeNearTarget()
                .colored(PonderPalette.OUTPUT)
                .attachKeyFrame();
        scene.idle(100);

        showPower(scene, util, 90);
        scene.overlay().showText(90)
                .text("Keep FE supplied while the machine runs. Item insertion, currency extraction and FE input are available on every side.")
                .pointAt(util.vector().blockSurface(SINK, Direction.EAST))
                .placeNearTarget()
                .colored(PonderPalette.BLUE);
        animateConversion(scene, example);
        scene.idle(10);
        scene.world().modifyBlockEntity(SINK, ItemSinkBlockEntity.class,
                sink -> sink.getInventory().setStackInSlot(ItemSinkBlockEntity.OUTPUT_SLOT_1, ItemStack.EMPTY));
        scene.world().modifyBlockEntity(OUTPUT_HOPPER, HopperBlockEntity.class,
                hopper -> hopper.setItem(0, example.output().copy()));
        moveItem(scene, util.vector().centerOf(SINK).add(0, 0, 0.7),
                util.vector().centerOf(OUTPUT_HOPPER).add(0, 0, 0.7), example.output(), 15);
        moveItem(scene, util.vector().centerOf(OUTPUT_HOPPER).add(0, 0, 0.7),
                util.vector().centerOf(OUTPUT_CHEST).add(0, 0, 0.7), example.output(), 15);
        scene.world().modifyBlockEntity(OUTPUT_HOPPER, HopperBlockEntity.class,
                hopper -> hopper.setItem(0, ItemStack.EMPTY));
        scene.world().modifyBlockEntity(OUTPUT_CHEST, ChestBlockEntity.class,
                chest -> chest.setItem(0, example.output().copy()));
        scene.effects().indicateSuccess(OUTPUT_CHEST);
        scene.idle(25);

        scene.world().modifyBlockEntity(SINK, ItemSinkBlockEntity.class, sink -> {
            ItemStack fullStack = example.output().copyWithCount(example.output().getMaxStackSize());
            sink.getInventory().setStackInSlot(ItemSinkBlockEntity.INPUT_SLOT, example.input().copy());
            sink.getInventory().setStackInSlot(ItemSinkBlockEntity.OUTPUT_SLOT_1, fullStack.copy());
            sink.getInventory().setStackInSlot(ItemSinkBlockEntity.OUTPUT_SLOT_2, fullStack.copy());
        });
        scene.world().modifyBlockEntity(OUTPUT_HOPPER, HopperBlockEntity.class, hopper -> {
            for (int slot = 0; slot < hopper.getContainerSize(); slot++) {
                hopper.setItem(slot, example.output().copyWithCount(example.output().getMaxStackSize()));
            }
        });
        scene.world().modifyBlockEntity(OUTPUT_CHEST, ChestBlockEntity.class, chest -> {
            for (int slot = 0; slot < chest.getContainerSize(); slot++) {
                chest.setItem(slot, example.output().copyWithCount(example.output().getMaxStackSize()));
            }
        });
        scene.overlay().showOutline(PonderPalette.RED, new Object(), util.select().position(OUTPUT_CHEST), 110);
        scene.overlay().showOutline(PonderPalette.RED, new Object(), util.select().position(SINK), 110);
        scene.overlay().showText(110)
                .text("Processing pauses when the output slots cannot fit the next payout, or when FE runs out. Keep the output clear and the machine powered.")
                .pointAt(util.vector().centerOf(SINK))
                .placeNearTarget()
                .colored(PonderPalette.RED)
                .attachKeyFrame();
        scene.idle(120);

        scene.world().modifyBlockEntity(OUTPUT_CHEST, ChestBlockEntity.class, ChestBlockEntity::clearContent);
        scene.world().modifyBlockEntity(OUTPUT_HOPPER, HopperBlockEntity.class, HopperBlockEntity::clearContent);
        scene.world().modifyBlockEntity(SINK, ItemSinkBlockEntity.class, sink -> {
            sink.getInventory().setStackInSlot(ItemSinkBlockEntity.OUTPUT_SLOT_1, ItemStack.EMPTY);
            sink.getInventory().setStackInSlot(ItemSinkBlockEntity.OUTPUT_SLOT_2, ItemStack.EMPTY);
        });
        scene.overlay().showText(80)
                .text("Once there is room for the currency and enough FE, processing resumes automatically.")
                .pointAt(util.vector().centerOf(SINK))
                .placeNearTarget()
                .colored(PonderPalette.GREEN);
        showPower(scene, util, 70);
        animateConversion(scene, example);
        scene.idle(30);
        scene.markAsFinished();
    }

    private static void showPower(SceneBuilder scene, SceneBuildingUtil util, int duration) {
        Vec3 port = util.vector().blockSurface(SINK, Direction.EAST);
        scene.overlay().showBigLine(PonderPalette.BLUE, port.add(1.5, 0, 0), port, duration);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.BLUE, new Object(),
                new AABB(SINK).deflate(0.05), duration);
        scene.world().modifyBlockEntity(SINK, ItemSinkBlockEntity.class,
                sink -> sink.getData().set(2, Config.ENERGY_CAPACITY.get()));
    }

    private static void animateConversion(SceneBuilder scene, Example example) {
        // Ponder runs on the client, where the real machine ticker is disabled.
        // Animate a shortened example without changing the player's config or world.
        scene.world().modifyBlock(SINK, state -> state.setValue(ItemSinkBlock.LIT, true), false);
        for (int progress = 1; progress <= 6; progress++) {
            int currentProgress = progress;
            scene.world().modifyBlockEntity(SINK, ItemSinkBlockEntity.class, sink -> {
                sink.getData().set(1, 6);
                sink.getData().set(0, currentProgress);
            });
            scene.idle(10);
        }
        scene.world().modifyBlockEntity(SINK, ItemSinkBlockEntity.class, sink -> {
            sink.getInventory().setStackInSlot(ItemSinkBlockEntity.INPUT_SLOT, ItemStack.EMPTY);
            sink.getInventory().setStackInSlot(ItemSinkBlockEntity.OUTPUT_SLOT_1, example.output().copy());
            sink.getData().set(0, 0);
        });
        scene.world().modifyBlock(SINK, state -> state.setValue(ItemSinkBlock.LIT, false), false);
        scene.effects().indicateSuccess(SINK);
    }

    private static void moveItem(SceneBuilder scene, Vec3 from, Vec3 to, ItemStack stack, int ticks) {
        ElementLink<EntityElement> item = scene.world().createItemEntity(from, Vec3.ZERO, stack.copy());
        scene.world().modifyEntity(item, entity -> entity.setNoGravity(true));
        for (int tick = 1; tick <= ticks; tick++) {
            Vec3 position = from.lerp(to, (double) tick / ticks);
            scene.world().modifyEntity(item, entity -> entity.setPos(position));
            scene.idle(1);
        }
        scene.world().modifyEntity(item, Entity::discard);
    }

    private static Example example() {
        ItemStack flint = new ItemStack(Items.FLINT);
        Config.Exchange exchange = Config.findExchange(flint).orElseGet(() -> Config.exchanges().stream()
                .filter(candidate -> !candidate.createOutput().isEmpty())
                .findFirst().orElse(null));
        if (exchange != null) {
            return new Example(new ItemStack(BuiltInRegistries.ITEM.get(exchange.input())), exchange.createOutput());
        }
        // Keep the guide usable before the pack has configured an available currency.
        return new Example(flint, new ItemStack(Items.EMERALD));
    }

    private record Example(ItemStack input, ItemStack output) {
    }
}
