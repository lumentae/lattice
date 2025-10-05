# Lattice
> [!IMPORTANT]  
> Fabric doesn't work anymore in v1.2.0 and up!

Lattice is a Minecraft mod intended to be used on SMP servers to manage many useful features for players

## Features
- Player nickname and status management
- Night vision toggle for players
- Server configuration management (save/reload)
- Message of the Day (MOTD) rotation
- Detection of illegal/cheat mods
- Decorators for enhanced in-game experience (e.g., time/status decorators)
- Configurable PvP settings per player
- Server and end opening dates

## Commands
Below is a list of all available commands and their functionality:

### `/nv`
- **Toggles Night Vision**

### `/lattice <action>`
- **Performs administrative actions** (requires permission level 2)
- `<action>` can be:
  - `save` — Saves the config
  - `reload` — Reloads the config
- Example: `/lattice save`

### `/nick [nickname]`
- **Sets or removes your nickname**
- If you pass your actual Minecraft name as the nickname, your nickname is removed
- If called without arguments, shows your current nickname

### `/status [<status>|reset]`
- **Changes your status message**
- If you pass `reset` as the status, your status is removed
- If called without arguments, shows your current status message

## Configuration
- Config file is stored as `config.json` under lattice in the server config directory
- You can change the time, when the end or the server will be opened
- MOTDs and a list of illegal mods can be configured
- Each player has configurable options:
  - PvP enabled/disabled
  - Nickname
  - Status message
