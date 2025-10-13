package com.sharedhealth;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//Handling of damage/healing propagation

public class SharedHealthHandler {

    // global state
    private static volatile boolean isActive = false;
    private static MinecraftServer serverInstance = null;

    // map for healing tracking and flag to prevent feedback
    private static final Map<UUID, Float> lastHealth = new ConcurrentHashMap<>();
    private static final Set<UUID> recentlyUpdated = ConcurrentHashMap.newKeySet();

    public static void onServerStarted(MinecraftServer server) {
        serverInstance = server;
    }

    // damage and tick cleanup
    public static void registerEvents() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, damageTaken, blocked) -> {
            if (!isActive) return;
            if (!(entity instanceof ServerPlayerEntity player)) return;

            // feedback check
            if (recentlyUpdated.contains(player.getUuid())) {
                return;
            }
            onPlayerDamaged(player, damageTaken);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!recentlyUpdated.isEmpty()) {
                recentlyUpdated.clear();
            }
            server.getPlayerManager().getPlayerList().forEach(p -> lastHealth.putIfAbsent(p.getUuid(), p.getHealth()));
        });
    }

    public static void setActive(boolean active) {
        isActive = active;
        if (!active) {
            lastHealth.clear();
            recentlyUpdated.clear();
        } else {
            if (serverInstance != null) {
                serverInstance.getPlayerManager().getPlayerList().forEach(p -> lastHealth.put(p.getUuid(), p.getHealth()));
            }
        }
    }

    public static void onPlayerDamaged(ServerPlayerEntity player, float damage) {
        if (!isActive) return;
        if (damage <= 0) return;

        MinecraftServer server = player.getEntityWorld().getServer();
        UUID sourceId = player.getUuid();
        float delta = -damage;
        lastHealth.put(sourceId, player.getHealth());

        for (ServerPlayerEntity other : server.getPlayerManager().getPlayerList()) {
            if (other.getUuid().equals(sourceId)) continue;
            recentlyUpdated.add(other.getUuid());

            float newHealth = other.getHealth() + delta;
            lastHealth.put(other.getUuid(), Math.max(newHealth, 0f));

            if (newHealth <= 0f) {
                other.damage(other.getEntityWorld() , other.getDamageSources().generic(), Float.MAX_VALUE);
            } else {
                other.setHealth(Math.min(newHealth, other.getMaxHealth()));
            }
        }
    }

    public static void onPlayerHealed(ServerPlayerEntity player, float amount) {
        if (amount <= 0) return;

        MinecraftServer server = player.getEntityWorld().getServer();
        UUID sourceId = player.getUuid();
        lastHealth.put(sourceId, player.getHealth());

        for (ServerPlayerEntity other : server.getPlayerManager().getPlayerList()) {
            if (other.getUuid().equals(sourceId)) continue;

            recentlyUpdated.add(other.getUuid());

            float newHealth = other.getHealth() + amount;
            newHealth = Math.min(newHealth, other.getMaxHealth());
            other.setHealth(newHealth);

            lastHealth.put(other.getUuid(), other.getHealth());
        }
    }
}
