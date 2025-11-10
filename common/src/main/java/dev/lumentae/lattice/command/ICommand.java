package dev.lumentae.lattice.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public interface ICommand {
    void register(CommandDispatcher<CommandSourceStack> dispatcher);
}
