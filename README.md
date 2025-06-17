# 🪦 GraveBox v2.0

GraveBox is a robust Minecraft plugin designed to enhance player experience by preventing item loss upon death. Instead of dropping items when players die, GraveBox automatically creates a special container (grave) at the death location that securely stores all inventory items.

## ✨ Key Features

### ⚡ Core Functionality
- **🪦 Automatic Grave Creation:** Instantly generates a container at the exact death location when a player dies
- **🎒 Complete Inventory Preservation:** Transfers ALL items including:
  - Full armor set (helmet, chestplate, leggings, boots)
  - Offhand items (shields, totems, etc.)
  - Main inventory contents
  - Hotbar items
- **📦 Multiple Container Options:** Supports CHEST, BARREL, and SHULKER_BOX
- **🧹 Smart Grave Management:** Automatically removes empty graves to prevent clutter
- **📂 File-based Storage:** Each grave's contents are saved to individual YML files
- **🔍 Virtual Inventory Mode:** Uses virtual inventories for grave contents

### 🛡️ Enhanced Protection Systems
- **💥 Explosion Protection:** Graves survive TNT, creepers, and other explosions
- **🔒 Indestructible Graves:** Optional complete protection against all damage
- **🔑 Owner-Exclusive Access:** Strict access control with configurable messages
- **🚫 Item Blacklisting:** Prevent specific items from being stored in graves

### 📊 Player Statistics System
- **📈 Death Tracking:** Records how many times each player has died
- **🗃️ Database Storage:** SQLite by default
- **📊 Stats Command:** /gravestats shows personal death count
- **⏱️ Timestamp Tracking:** Records when each death occurred

### 💬 Discord Integration
- **📢 Death Notifications:** Real-time alerts to Discord channels
- **🎨 Custom Embeds:** Color-coded messages with rich formatting
- **📍 Location Details:** Includes world and coordinates
- **🖼️ Custom Footer:** Shows plugin version in messages

### 🎨 Customization Options
- **🏷️ Custom Grave Names:** Supports {player} placeholder and color codes
- **🔧 Flexible Material Choices:** Any container block type
- **📤 Item Overflow Handling:** Drops excess items when grave is full
- **⚙️ Blacklist System:** Prevent specific items from being stored

## 📥 Installation Guide

1. 🔽 Download the latest GraveBox.jar from releases
2. 📂 Place in plugins/ folder
3. 🔄 Restart server to generate configs
4. ⚙️ Edit plugins/GraveBox/config.yml as needed
5. 🔃 Reload with /gravebox reload or restart

## ⚙️ Configuration Details

### ⚰️ Grave Settings
```yaml
grave:
  material: "CHEST" # CHEST, BARREL, SHULKER_BOX
  size: "EXTRA_LARGE" # SMALL (9), MEDIUM (18), LARGE (27), EXTRA_LARGE (54)
  explosion-protection: true
  indestructible: true
  auto-remove: true
  drop-items-on-destroy: false
  custom-name: "&6{player}'s Grave"
```

### 📊 Statistics System
```yaml
stats:
  enabled: true
  database:
    type: "SQLITE"
    file: "stats.db"
```

### 📢 Discord Webhook
```yaml
discord:
  enabled: false
  webhook-url: "https://discord.com/api/webhooks/your_webhook_here"
  message: "{player} died at {x}, {y}, {z} in world {world}"
  embed-color: "FF0000"
```

### 💬 Messages
```yaml
messages:
  grave-created: "&aYour items are safe in a grave at &e{x}, {y}, {z}&a in world &e{world}&a."
  not-your-grave: "&cThis is not your grave!"
  cannot-destroy: "&cThis grave is indestructible!"
  grave-emptied: "&aYou retrieved all items from your grave. It has now vanished."
  grave-full: "&cYour grave was too small to hold all your items! Some were dropped nearby."
  stats-message: "&6Your death count: &a"
```

### 🚫 Advanced Settings
```yaml
advanced:
  blacklisted-items:
    - "BEDROCK"
    - "BARRIER"
    - "COMMAND_BLOCK"
    - "CHAIN_COMMAND_BLOCK"
    - "REPEATING_COMMAND_BLOCK"
    - "STRUCTURE_BLOCK"
    - "JIGSAW"
```

## 🔑 Permission Nodes

| Permission | Description | Default |
|------------|-------------|---------|
| gravebox.use | Access grave features | true |
| gravebox.stats | View death statistics | true |
| gravebox.admin | Admin commands | op |
| gravebox.bypass | Bypass all protections | op |

## 🕹️ Usage Guide

### 👨‍💻 Player Commands
- `/gravestats` - View your death count (aliases: deathstats, deaths)

### 👨‍💼 Admin Commands
- `/graveadmin` - Admin commands for GraveBox (reload|stats)

## 🛠️ Technical Specifications

### 📦 Dependencies:
- PaperMC API 1.21
- SQLite JDBC (included)
- Jackson Databind (included)

### ⚙️ API Support:
- PlaceholderAPI (soft dependency)
- Vault (soft dependency)

### 📊 Data Storage:
- SQLite database for statistics
- YML files for grave contents storage

### 🔧 Technical Details:
- Java 17+ required
- Maven build system
- Shaded dependencies to prevent conflicts

## ❓ FAQ

**Q: How do I set up Discord notifications?**  
A: Enable in config.yml and add your webhook URL

**Q: Can graves be destroyed if indestructible is false?**  
A: Yes, with drop-items-on-destroy controlling whether items drop or are deleted

**Q: How do I prevent certain items from being stored?**  
A: Add them to the blacklisted-items in config.yml

**Q: What happens if a grave is placed in an obstructed location?**  
A: The plugin will try to place it one block higher, or drop items normally if no space is available

**Q: Can I change the grave size?**  
A: Yes, through the size option in config (9-54 slots depending on material)

## 🌟 Pro Tips
- Use SHULKER_BOX for portable graves that move with chunks
- Combine with WorldGuard for region-specific protections
- Use PlaceholderAPI to show death stats in scoreboards
- Set up Discord webhooks for staff monitoring
- For large servers, consider monitoring the graves folder size

## 🤝 Support & Contributing
Report issues on GitHub. Contributions welcome via pull requests!

## 📜 License
MIT License - Free for use and modification

## 📌 Version Information
- Current Version: 2.0
- Minecraft Version: 1.21
- API Version: Paper 1.21-R0.1-SNAPSHOT
- Java Version: 17+

## 🏗️ Building from Source
1. Clone the repository
2. Run `mvn clean package`
3. Find the shaded jar in target/ folder

## 🧩 Plugin Integration
GraveBox is designed to work well with:
- Essentials (loads before it to handle death events)
- Vault (for potential economy integration)
- PlaceholderAPI (for stats display)

## ⚠️ Known Limitations
- 54-slot graves only work with CHEST material (as double chests)
- Virtual inventory mode doesn't support hoppers or other block interactions
- File-based storage may become inefficient with extremely high player counts

