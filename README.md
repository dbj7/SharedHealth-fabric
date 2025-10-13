# 🩸 SharedHealth — Fabric Mod

**SharedHealth** is a **Minecraft Fabric mod** that links the health of all players on the server.  
When one player takes damage, every other player loses the same amount of health.  
When one player heals, everyone heals.  
And if one player dies… they all die together. 💀

---

## ⚙️ Features

- 🔗 **Shared health:** all players share a single health pool.
- ❤️ **Synchronized healing:** when one player regenerates, everyone does.
- 💀 **Collective death:** when one player dies, all others die instantly.
- 🧩 **Fabric 1.21+ compatible**
- ⚡ **Lightweight:** uses server events instead of heavy tick loops.

---

## 🪄 How It Works

- The mod keeps track of each player’s health.
- Whenever one player’s health changes, that difference is applied to all others.
- If a player’s health drops to zero, an event triggers that kills all other players immediately.
- All logic runs server-side, so no client installation is required.

---

## 📦 Installation

1. Download the latest `SharedHealth-x.x.x.jar`.
2. Place it in your server’s `mods/` folder (or your local `mods/` folder if testing in singleplayer).
3. Launch Minecraft with **Fabric Loader 1.21+**.
4. Check your console for:
