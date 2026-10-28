# Translations

Language files are in `src/main/resources/assets/item_sink/lang`.
They cover the 143 locales in the Minecraft 1.20.1 asset index, including regional, historical and fictional options.

These are initial machine-assisted translations and authored adaptations, with regional and script variants where appropriate. Gothic, Ngiiwà, Tzotzil and Viossa Ponder captions use compact localized labels and diagrams. Native-speaker review and corrections are welcome.

Keep translation keys, formatting placeholders such as `%1$s`, and configuration identifiers unchanged. `en_us.json` is the source text.

Run `gradlew.bat verifyTranslations` on Windows or `./gradlew verifyTranslations` elsewhere to check coverage, keys and placeholders. The regular build also runs this check.
