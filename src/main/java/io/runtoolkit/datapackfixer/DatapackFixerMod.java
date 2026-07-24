package io.runtoolkit.datapackfixer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DatapackFixerMod implements ModInitializer {
    public static final String MOD_ID = "datapack_fixer";
    public static final Logger LOGGER = LoggerFactory.getLogger("Datapack Fixer");

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            var diagnostics = new DatapackSyntaxScanner().scan(server.getWorldPath(LevelResource.DATAPACK_DIR));
            if (diagnostics.isEmpty()) {
                LOGGER.info("Datapack Fixer found no supported syntax issues.");
                return;
            }
            LOGGER.warn("Datapack Fixer found {} syntax issue(s). No files were changed.", diagnostics.size());
            diagnostics.forEach(diagnostic -> LOGGER.warn("[{}] {}:{} {} Suggested fix: {}",
                    diagnostic.code(), diagnostic.file(), diagnostic.line(), diagnostic.message(), diagnostic.suggestion()));
        });
    }
}
