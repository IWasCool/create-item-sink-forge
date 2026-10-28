# CreateCore item values

The catalogue extends the original values without changing any of the 462 original item prices. The existing duplicate `kubejs:polished_spectral_ruby_electron_tube` keeps its effective value of 16, matching the original first-tier-wins behavior.

The scan inventoried all 195 jars in `createnewworldcore-1.21.1/libs`, inspected their bundled archives, and loaded server mods and their dependencies in a separate development run. It found **16,235 registered items across 80 item namespaces**. Of these, 274 already had prices and **15,919 receive new prices**. The other 188 original ids belong to absent/legacy mods and remain in the original defaults for compatibility. Client utilities and libraries that register no items need no exchange entries. When duplicate mod versions exist in Core's `libs`, the scan selects the newest version.

Every registered item is accounted for in [item-values.csv](item-values.csv), with its price, whether it was preserved/added/excluded, and the pricing rationale. [item-value-coverage.json](item-value-coverage.json) lists counts by namespace and tier, the exclusions, and legacy ids missing from this installation. [value-mod-inventory.json](value-mod-inventory.json) records every inspected jar, including bundled mods. [value-registry.json](value-registry.json) is the exact registered-item snapshot used to generate the additions.

## Pricing

New values use the existing 1, 2, 3, 4, 5, 6, 7, 8, 16, 32, 48, and 64 tiers. Common natural and decorative blocks stay near 1; crafted building materials and basic components occupy the lower tiers. Ingots, gems, food, equipment, and machinery follow comparable existing items. Advanced machines, nuclear components, rare collectibles, and progression items occupy the higher tiers. New Core items that replace a legacy KubeJS item inherit its existing price when their item paths match. Incomplete processing intermediates receive value 1. The CSV records individual prices and the pricing rationale from the original catalogue.

These are salvage prices, rather than a guarantee of equal value across every crafting recipe. Modded recipe changes, item components, enchantments, backpack contents, tank contents, and other stored data do not change the price for an item id. Category prices are an initial balance pass; the CSV makes individual adjustments reviewable.

The additions exclude 42 Numismatics currency coins and creative-only technical items, including infinite power sources and command/structure blocks. Coins must not become inputs that mint more coins. Any item configured as a tier's output is also excluded from the bundled inputs. Explicit user tier entries continue to take priority.

## Configuration

Each tier defaults to a single item tag: `#item_sink:value_1`, `#item_sink:value_2`, `#item_sink:value_3`, `#item_sink:value_4`, `#item_sink:value_5`, `#item_sink:value_6`, `#item_sink:value_7`, `#item_sink:value_8`, `#item_sink:value_16`, `#item_sink:value_32`, `#item_sink:value_48`, and `#item_sink:value_64`. Each tag bundles the original defaults and the additional CreateCore items for that value. Missing optional items do not prevent tags from loading.

In `config/item_sink-common.toml`, keep the default tag and append individual item ids or other item tags:

```toml
[item_sink.values]
# Keep the bundled value 1 items and add sticks.
value1Items = ["#item_sink:value_1", "minecraft:stick"]
# Keep the bundled value 4 items and add everything in the tin ingot tag.
value4Items = ["#item_sink:value_4", "#forge:ingots/tin"]
```

Individual item ids take priority over tags, so adding an item id to another tier moves its value. If multiple individual lists contain the same id, the first tier wins; overlapping tags also use the first tier. Exclusions in `item_sink.values.excludedItems` take priority over both. Currency outputs are excluded from tag-derived inputs to prevent currency loops.

When `item_sink-common.toml` is absent, the previous `create_item_sink-common.toml` is copied automatically, and old built-in tag references are renamed to `#item_sink:`. Unchanged old default lists are compacted automatically on config load; customized lists are retained. Existing configs still receive CreateCore additions automatically. Set `includeCreateCoreValues = false` to disable those additions; the default tier tags then use only their original item groups. Explicit item ids and custom tags remain available. Tier currency, quantity, processing time, and energy settings are unchanged. Config and datapack tag reloads invalidate the cached lookup table.

### Add items with a datapack

Create `world/datapacks/my_sink_values/pack.mcmeta`:

```json
{
  "pack": {
    "pack_format": 15,
    "description": "Extra Item Sink values"
  }
}
```

Create `world/datapacks/my_sink_values/data/item_sink/tags/items/value_1.json`:

```json
{
  "replace": false,
  "values": [
    "minecraft:stick",
    { "id": "yourmod:your_item", "required": false }
  ]
}
```

Use `value_2.json`, `value_4.json`, or any other tier filename to extend that value instead. `replace: false` keeps the bundled items. Replace the example mod item with your actual item id; `required: false` allows the pack to load when that mod is absent. Run `/reload` after enabling or editing the datapack. Restart the game to refresh JEI's displayed conversions after changing tags or config.

## Catalogue provenance

The catalogue was originally generated from a Minecraft 1.21.1 / NeoForge mod collection. The committed CSV and JSON files retain that scan's historical inventory. This Forge project includes the resulting values locally; items absent from the installed Forge 1.20.1 mod collection are ignored. No sibling project is needed to build or run this project.
