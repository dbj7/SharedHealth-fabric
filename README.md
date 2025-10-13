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

## 🔧 Requirements

- **Minecraft:** 1.21.10 or later  
- **Fabric Loader:** 0.15.0 or newer  
- **Fabric API:** latest version

---

## 🧠 Notes

- The mod does **not** add any client-side content — it’s purely a server-side gameplay mod.
- It’s compatible with most gameplay or combat mods, as it only hooks into health change and death events.
- Ideal for **hardcore co-op challenges**, **team-based adventures**, or **streaming events**.

---

## 🧩 Future Plans

- Configurable damage scaling (share only a percentage of damage).  
- Option to disable synchronized healing.  
- Support for modded health systems.

---

## 🧑‍💻 Author

Created by **djb7** — open for suggestions and contributions!  
If you find a bug or have a feature request, please open an issue on GitHub.

---

> “If one falls, we all fall.” — SharedHealth Philosophy 🩸
