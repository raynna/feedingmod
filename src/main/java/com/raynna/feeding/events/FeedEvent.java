package com.raynna.feeding.events;

import com.raynna.feeding.FeedMod;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


@EventBusSubscriber(modid = FeedMod.MOD_ID)
public class FeedEvent {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getTarget() instanceof Player target) {
                handlePlayerFeeding(player, target);
            }
        }
    }

    private static void handlePlayerFeeding(ServerPlayer feeder, Player target) {
        ItemStack foodItem = feeder.getMainHandItem();
        FoodProperties foodProps = foodItem.getItem().getFoodProperties(foodItem, target);

        if (!foodItem.isEmpty() && foodProps != null) {
            if (target.getFoodData().getFoodLevel() < FoodConstants.MAX_FOOD) {
                ItemStack result = target.eat(feeder.level(), foodItem);
                if (result.isEmpty() || result.getCount() < foodItem.getCount()) {
                    foodItem.shrink(1);
                    sendFeedingMessages(feeder, target);
                }
            } else {
                feeder.sendSystemMessage(Component.literal(target.getName().getString() + " doesn't have any hunger."));
            }
        }
    }

    private static void sendFeedingMessages(Player feeder, Player target) {
        feeder.sendSystemMessage(Component.literal("You have fed " + target.getName().getString() + "."));
        target.sendSystemMessage(Component.literal(feeder.getName().getString() + " has fed you."));
    }

    public static void register() {
        NeoForge.EVENT_BUS.register(FeedEvent.class);
    }
}
