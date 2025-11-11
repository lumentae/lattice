# Lattice

Lattice is a Minecraft mod intended to be used on SMP servers to manage many useful features for players.  
For it to work, it needs to be installed on the server and the client.

## Features
- Player nickname and status management
- Night vision toggle for players
- Server configuration management (save/reload)
- Message of the Day (MOTD) rotation
- Detection of illegal/cheat mods
- Decorators for enhanced in-game experience (e.g., time/status decorators)
- Configurable PvP settings per player
- Server, end and nether opening dates
- Rules need to be accepted by players to play

## Commands
Below is a list of all available commands and their functionality:

### `/nv`
- **Toggles Night Vision**

### `/nick [nickname]`
- **Sets or removes your nickname**
- If you pass your actual Minecraft name as the nickname, your nickname is removed
- If called without arguments, shows your current nickname

### `/status [<status>|reset]`
- **Changes your status message**
- If you pass `reset` as the status, your status is removed
- If called without arguments, shows your current status message

### `/lattice <action> [subAction] [subActionArg]`
- **Performs administrative actions** (requires permission level 2)
- Available actions:
    - `config` - Manage server configuration
        - `save` - Saves the current configuration to disk
        - `reload` - Reloads the configuration from disk
    - `motd` - Manage Message of the Day
        - `add` - Adds a new MOTD
        - `remove` - Removes a MOTD
        - `list` - Lists all MOTDs
    - `illegal` - Manage illegal mods
        - `add` - Adds a new illegal mod
        - `remove` - Removes an illegal mod
        - `list` - Lists all illegal mods
    - `pvp` - Manage PvP settings
        - `<player>` - Specifies the player
            - `<enable|disable>` - Enables or disables PvP for the specified player
    - `rules` - Manage rules for a player
        - `<player>` - Specifies the player
            - `<enable|disable>` - Accepts or declines rules for the specified player
    - `open` - Manage server and end opening dates
        - `<dimension>` - Specifies what to modify [nether, end, server]
            - `<date>` - Sets the opening date in the format `YYYY-MM-DD hh:mm:ss`
    - `nick` - Manage nickname for a player
        - `<player>` - Specifies the player
            - `remove` - Removes the nickname for a player
        - `<nickname>` - Sets the nickname for a player
    - `status` - Manage status messages for a player
        - `<player>` - Specifies the player
            - `remove` - Removes the status message for a player
            - `<status>` - Sets the status message for a player
    - `help` - Shows a link to the repository
- Examples:
    - `/lattice config save`
    - `/lattice motd add Welcome to the server!`
    - `/lattice illegal list`
    - `/lattice pvp PlayerName enable`
    - `/lattice open end 2024-12-31 23:59:59`
    - `/lattice rules PlayerName disable`
    - `/lattice nick PlayerName remove`
    - `/lattice status PlayerName COOL`

## Configuration
- Config file is stored as `config.json` under lattice in the server config directory
- You can change the time when the end or the server will be opened
- MOTDs and a list of illegal mods can be configured
- Each player has configurable options:
  - PvP enabled/disabled
  - Nickname
  - Status message
  - Rules enabled/disabled
