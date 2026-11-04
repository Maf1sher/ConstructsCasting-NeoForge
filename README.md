# Construct's Casting

Unofficial NeoForge 1.21.1 port of [Construct's Casting](https://github.com/snackerpirater/ConstructsCasting),
originally by [SnackPirate](https://github.com/snackerpirater).

Construct's Casting is a [Tinkers' Construct](https://github.com/SlimeKnights/TinkersConstruct)
add-on that bridges Tinkers' tools with
[Iron's Spells 'n Spellbooks](https://github.com/iron431/Irons-Spells-n-Spellbooks).
It adds modifiable spellbooks and staffs, magic tool materials and traits,
spell-power and dispulsion modifiers, boons, and a smeltery chain for magical
essences and inks.

## Features

- **Modifiable spellbooks** — Traveller's, Plated, and Slimy spellbooks built
  from covers, pages, and plating. Spell slots are inscribed with Iron's
  Spellbooks scrolls, and the books work as Curios.
- **Staffs and weapons** — the Wand and Battlestaff cast spells on right click,
  the Sculk Staff supports blocking and charging, and the Flamberge trades raw
  damage for defense and mobility.
- **Materials and traits** — Arcanium, Exilite, Mithril, and Pyrium, plus
  magical cloth, bone, and rod materials. Adornment parts carry school traits
  such as Arcane, Mana Upgrade, and spell specialization.
- **Modifiers** — school upgrades (`abyssal`, `aqua`, `blood`, `eldritch`,
  `ender`, `evocation`, `fire`, `holy`, `ice`, `lightning`, `nature`, `sound`,
  `technomancy`), dispulsions, casting, imbued, spellblade, conserving, dragonspells,
  and more.
- **Boons** — affinity slots that hold spell-school upgrades on tools.
- **Smeltery integration** — essences and inks for every school, molten Arcanium,
  Exilite, Mithril, Pyrium, arcane salvage, pearls, coral, and stews, with casting
  and alloy recipes.
- **Spells** — Freeze cools casting tables and basins, and Invert flips gravity.

## Compatibility

The base mod requires Tinkers' Construct and Iron's Spellbooks. Optional content
loads only when the matching mod is installed:

- [Cataclysm Spellbooks](https://www.curseforge.com/minecraft/mc-mods/cataclysm-spellbooks)
  — abyssal and technomancy upgrades and essences.
- [Alshanex's Familiars](https://www.curseforge.com/minecraft/mc-mods/alshanexs-familiars)
  — sound upgrades and essence.
- Traveloptics — aqua upgrades and essence; no 1.21.1 build is known, so this
  content stays dormant.

## Requirements

- Minecraft 1.21.1 with NeoForge 21.1.264 or newer.
- Tinkers' Construct 3.12.1 or newer; Mantle 1.12 or newer comes with it.
- Iron's Spells 'n Spellbooks 1.21.1-3.16.3 or newer, with its dependencies
  (irons_lib, Curios, GeckoLib, and Player Animator).

## Building

Requires JDK 21. The project builds against Mantle and Tinkers' Construct, so
clone all three repositories into the same folder:

- [Mantle-NeoForge](https://github.com/Maf1sher/Mantle-NeoForge)
- [TinkersConstruct-NeoForge](https://github.com/Maf1sher/TinkersConstruct-NeoForge)
- this repository

Then build them in order, starting from that folder.

Linux / macOS:

```bash
cd Mantle-NeoForge && ./gradlew build
cd ../TinkersConstruct-NeoForge && ./gradlew build
cd ../ConstructsCasting-NeoForge && ./gradlew build
```

Windows (Command Prompt or PowerShell):

```bat
cd Mantle-NeoForge && gradlew.bat build
cd ..\TinkersConstruct-NeoForge && gradlew.bat build
cd ..\ConstructsCasting-NeoForge && gradlew.bat build
```

The jar is written to `build/libs/constructs_casting-2.3.0.jar`.

## Credits

- Original mod and author: [SnackPirate](https://github.com/snackerpirater/ConstructsCasting)
- NeoForge 1.21.1 port: [Maf1sher/ConstructsCasting-NeoForge](https://github.com/Maf1sher/ConstructsCasting-NeoForge)
