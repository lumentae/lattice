package dev.lumentae.lattice;

import net.neoforged.bus.api.IEventBus;

@net.neoforged.fml.common.Mod(Constants.MOD_ID)
public class Lattice {
    public Lattice(IEventBus eventBus) {
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        Mod.init();
    }
}
