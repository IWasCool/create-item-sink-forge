# Update Log

## v0.0.2

- Added two Item Sink Ponder scenes covering manual conversion, FE power, currencies, hopper automation, and blocked outputs.

- Kept the name Create: Item Sink and the release filename `create_item_sink-0.0.2.jar` without loader or Minecraft-version suffixes.

- Added a separate [Forge edition](README.md) for Minecraft 1.20.1, Forge 47.4.x, Java 17, and Create 6.0.x.
- Ported registration, item and FE capabilities, capability invalidation, block interaction, menu networking, client screens, configuration, and NBT persistence to Forge.
- Shared the existing assets, translations, and item values, with automatic conversion to Minecraft 1.20.1 datapack paths and crafting result format.
- Retained optional Numismatics currencies and JEI support, config migration, value overrides, exclusions, and currency loop protection.
- Added six server GameTests covering conversion, automation, energy, persistence, menus, loaded resources, and configuration behavior, including a run without Numismatics.


- Extracted the Forge edition into a standalone project with local resources, documentation, and a Gradle wrapper.
