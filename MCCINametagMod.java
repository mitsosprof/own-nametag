package me.anastarawneh.mccinametagmod;

import me.anastarawneh.mccinametagmod.config.Config;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MCCINametagMod implements ModInitializer {
    public static final String MODID = "mccinametagmod";
    public static final Logger LOGGER = LogManager.getLogger("OwnNametag");

    @Override
    public void onInitialize() {
        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        LOGGER.info("Own Nametag loaded");
    }

    public static Config getConfig() {
        return AutoConfig.getConfigHolder(Config.class).getConfig();
    }

    public static boolean modEnabled() {
        return getConfig().enabled;
    }
}
