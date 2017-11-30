[![Sponge](https://img.shields.io/badge/sponge-1.10,11,12-yellow.svg)](https://forums.spongepowered.org/t/no-entry-sign-modbanner-ban-undesirable-mods/17681)

# ModBanner
Forge Mod Banner for Sponge

Main page: https://forums.spongepowered.org/t/no-entry-sign-modbanner-ban-undesirable-mods/17681

## Requirements
+ SpongeForge

## How to build

```
$ gradlew build
```

## Config
+ `blacklist` List of blacklisted mods
+ `kickmsg` Message will be displayed to users who has banned mods
+ `kickdelay` A delay its needed to get the player mods, its recommended leave the default delay but you can test to make it lower and see if it works!

## Permission / Commands

### Add/Remove banned mods
+ ``modbanner.command.add`` - /modbanner add <mod> (Add mod to blacklist) (Names dont need be the same, you can use ray to block: [xxray, ExtremRaYXXD] etc)

+ ``modbanner.command.remove`` - /modbanner remove <mod> (Remove mod from blacklist)

+ ``modbanner.command.list`` - /modbanner list (See all blacklisted mods)

+ ``modbanner.command.manage`` - /modbanner (add/remove/list)

### Reload
+ ``modbanner.command.reload`` - /modbanner reload (Reload configuration/banned mods (Not needed if you add mods through command))

### List
+ ``modbanner.command.modinfo.player`` - /modinfo player <player> (See all mods player use)

+ ``modbanner.command.modinfo.mod`` - /modinfo mod <mod> (See all players who used that mod)
