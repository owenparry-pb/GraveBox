# 🪦 GraveBox

GraveBox is a robust Minecraft plugin designed to enhance player experience by preventing item loss upon death. Instead of dropping items when players die, GraveBox automatically creates a special container (grave) at the death location that securely stores all inventory items.

## ✨ Key Features

### ⚡ Core Functionality
- **🪦 Automatic Grave Creation:** Instantly generates a container at the exact death location when a player dies.
- **🎒 Complete Inventory Preservation:** Transfers all inventory items (including armor and offhand) to the grave container.
- **📦 Multiple Container Options:** Supports various container types configurable via settings.
- **🧹 Smart Grave Management:** Automatically removes empty graves to prevent clutter.

### 🛡️ Protection Systems
- **💥 Explosion Protection:** Option to protect graves from being destroyed by explosions (TNT, creepers, etc.).
- **🔒 Indestructible Graves:** Configurable option to make graves immune to all destruction attempts.
- **🔑 Owner-Exclusive Access:** Only the deceased player can access their own grave.

### 🎨 Customization Options
- **🏷️ Custom Grave Names:** Supports personalized grave names with player name placeholders.
- **🔧 Flexible Material Choices:** Configure which block type to use for graves (chest, barrel, etc.).
- **📤 Item Drop Control:** Choose whether destroyed graves should drop their contents.

### 📢 User Experience
- **📍 Death Location Notification:** Players receive coordinates of their grave location.
- **💬 Clear Messaging:** Customizable messages for all grave interactions.
- **🎭 Visual Banner:** Eye-catching console banner upon plugin activation.

## 📥 Installation Guide

1. 🔽 Download the latest `GraveBox.jar` from the official source.
2. 📂 Place the JAR file in your server's `plugins/` directory.
3. 🔄 Restart your server to generate configuration files.
4. ⚙️ Configure settings in `plugins/GraveBox/config.yml` as needed.
5. 🔃 Reload the plugin or restart the server to apply changes.

## ⚙️ Detailed Configuration Options

The `config.yml` file contains the following customizable settings:

### ⚰️ Grave Settings
```yaml
grave:
  material: "CHEST" # Block type for graves (CHEST, BARREL, etc.)
  explosion-protection: true # Protect graves from explosions
  indestructible: true # Prevent manual grave destruction
  auto-remove: true # Remove graves when emptied by owner
  drop-items-on-destroy: false # Drop items if grave is destroyed
  custom-name: "&6{player}'s Grave" # Custom grave name (supports color codes)
```

### 💬 Message Customization
```yaml
messages:
  grave-created: "&aYour items have been stored in a grave at {x}, {y}, {z}"
  not-your-grave: "&cThis is not your grave!"
  cannot-destroy: "&cGraves cannot be destroyed!"
  grave-emptied: "&aYou've retrieved all items from your grave"
```

## 🔑 Permission Nodes
- `gravebox.use` - Allows a player to create death graves (enabled by default)
- `gravebox.bypass` - Allows bypassing grave protection (for staff/admin use)

## 🕹️ Usage Instructions

### 🎮 For Players
1. 💀 When you die, a grave will appear at your death location.
2. 🗺️ Return to the coordinates provided in the death message.
3. 🖱️ Right-click the grave to access your items.
4. 🧹 Remove all items to automatically clear the grave (if auto-remove is enabled).

### ⚙️ For Administrators
- 🔄 Use `/gravebox reload` to reload configuration without restart.
- 🆙 Use `/gravebox version` to check plugin version.
- 📊 Use `/gravebox stats` to view active graves count.

## 🛠️ Technical Details
- **🪶 Lightweight Design:** Optimized for minimal server impact.
- **🎭 Event-Based System:** Efficient handling of player deaths and grave interactions.
- **🆔 UUID Support:** Properly handles player identification for name changes.
- **🕹️ Compatibility:** Works with most Minecraft versions (1.13+).

## ❓ Frequently Asked Questions

### ❔ Can I change the grave container type?
Yes, modify the `grave.material` setting in `config.yml` with valid block types.

### ❔ How do I protect graves from being looted by others?
The plugin automatically restricts access to the grave owner only.

### ❔ What happens if a grave is destroyed?
Behavior depends on your config - items can be protected or dropped.

### ❔ Does this work with other protection plugins?
Generally yes, but test with specific plugins for compatibility.

### ❔ Can I customize the messages?
Yes, all messages are configurable in the messages section.

---

### 🤝 Contributing
Contributions are welcome! Feel free to open an issue or submit a pull request.

### 📜 License
This project is licensed under the MIT License.

### 🆘 Support
For any issues or feature requests, please create an issue on the GitHub repository.

