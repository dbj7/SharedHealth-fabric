package com.sharedhealth;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class sharedhealth implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("[SharedHealth] Initialized!");

        //collective kill setting
        ServerLivingEntityEvents.AFTER_DEATH.register((LivingEntity entity, DamageSource source) -> {
                    if (!(entity instanceof ServerPlayerEntity deadPlayer)) return;

                    var server = deadPlayer.getEntityWorld().getServer();
                    var damageSource = deadPlayer.getDamageSources().outOfWorld();

                    for (ServerPlayerEntity other : server.getPlayerManager().getPlayerList()) {
                        if (other != deadPlayer && other.isAlive()) {
                            other.damage(other.getEntityWorld(), damageSource, Float.MAX_VALUE);
                        }
                    }
                });


            // /shareon and /shareoff command set
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                dispatcher.register(CommandManager.literal("shareon").executes(ctx -> {
                    SharedHealthHandler.setActive(true);
                    ctx.getSource().sendFeedback(() -> Text.of("§aSharedHealth on"), true);
                    return 1;
                }));

                dispatcher.register(CommandManager.literal("shareoff").executes(ctx -> {
                    SharedHealthHandler.setActive(false);
                    ctx.getSource().sendFeedback(() -> Text.of("§cSharedHealth off"), true);
                    return 1;
                }));
            });

            // saves server instance and event register (handled inside handler)
            ServerLifecycleEvents.SERVER_STARTED.register(SharedHealthHandler::onServerStarted);
            SharedHealthHandler.registerEvents();
        }
    }
