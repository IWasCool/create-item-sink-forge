# Development and configuration

Version **0.0.2** targets **Minecraft 1.20.1**, **Forge 47.4.0 or newer within 47.x**, **Java 17**, and **Create 6.0.x**. Development and verification use Forge 47.4.10 and Create 6.0.8.

## Install

Build the release jar, then put `build/libs/create_item_sink-0.0.2.jar` from this folder into your Forge 1.20.1 instance's `mods` directory on the client and server. Install Create 6.0.x for Forge 1.20.1 as well.

**Create: Numismatics 1.1.0 for Forge 1.20.1 is optional.** The default exchange tiers use its coins. Without it, configure other currency items, such as `minecraft:emerald`, in `config/item_sink-common.toml`; tiers with missing output items remain disabled. JEI 15.20 or newer within 15.x is optional.

The block, item, tags, recipes, sounds, and config keep the `item_sink` namespace. The port retains configurable FE costs and processing times, inventory automation, the machine screen, JEI conversions, config migration, exclusions, explicit price overrides, and datapack values. Its textures, sounds, translations, and item value catalogue are included in this standalone project. Items belonging to absent mods or newer Minecraft versions are ignored.

## Using the Item Sink

Hover over the Item Sink in your inventory or JEI and hold the Ponder key (default **W**). Its two animated scenes explain manual use, FE power, currency outputs, hopper automation, and why processing can pause.

Right-click the placed machine to open its screen. Accepted items go in the left input slot; currency appears in the two output slots on the right. Supply Forge Energy (FE) from a generator or battery unless `requireEnergy` is disabled. Hoppers and other item handlers can insert inputs and extract currency on any side; FE can also enter from any side.

The Ponder examples use an available configured exchange, or illustrate flint and emeralds if no currency has been configured yet. Without Numismatics, set each desired tier's `outputItem` to an installed item in `config/item_sink-common.toml` before using the machine. JEI's **Item Sink Conversion** category lists available conversions, processing times, and FE costs.

## Build and verify

Open this folder as a Gradle project in IntelliJ IDEA. Run these commands from this folder using its included Gradle wrapper:

```powershell
.\gradlew.bat build
.\gradlew.bat runGameTestServer
.\gradlew.bat runGameTestServer -PwithoutNumismatics
```

The build downloads Forge and the matching development dependencies; all mod dependencies are declared in `build.gradle`. Gradle provisions a Java 17 toolchain if needed. `build/libs/` contains the reobfuscated release jar; `build/devlibs/` is for development.

The six server GameTests cover loaded tags, the crafting recipe, default and missing currencies, JEI conversion data, sided item/energy capabilities and their lifecycle, powered conversion, blocked outputs, NBT persistence, menu shift click and distance, exclusions, explicit overrides, and currency loop prevention. The test mod is excluded from the release jar.

Launch a development client with `.\gradlew.bat runClient`, or a server with `.\gradlew.bat runServer`. Edit `config/item_sink-common.toml` for configuration.

## Datapacks

Follow the [item value guide](item-values.md) with Minecraft 1.20.1 paths:

- Item value tags: `data/item_sink/tags/items/value_1.json` (plural `items`).
- Crafting recipes: `data/item_sink/recipes/item_sink.json`.
- Block loot: `data/item_sink/loot_tables/blocks/item_sink.json`.
- A datapack for 1.20.1 uses `pack_format: 15`.

The resources in `src/main/resources` already use these 1.20.1 paths and the crafting result's `item` key. Custom Forge tags can be referenced directly, for example `#forge:ingots/tin`.

Use this mod in a Minecraft 1.20.1 world. Worlds using the former `create_item_sink` registry namespace require manual migration to `item_sink` before loading.
