package dev.lumentae.lattice.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static net.minecraft.commands.Commands.literal;

public class NightvisionCommand implements ICommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("nv")
                .executes(commandContext -> {
                    ServerPlayer player = commandContext.getSource().getPlayer();
                    assert player != null;
                    if (player.hasEffect(MobEffects.NIGHT_VISION)) {
                        player.removeEffect(MobEffects.NIGHT_VISION);
                        return Command.SINGLE_SUCCESS;
                    }
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 255, false, false));
                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
