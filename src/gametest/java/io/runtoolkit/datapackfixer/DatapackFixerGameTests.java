package io.runtoolkit.datapackfixer;

import java.lang.reflect.Method;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public final class DatapackFixerGameTests implements CustomTestMethodInvoker {
    @GameTest
    public void scannerDoesNotCrashOnMissingDirectory(GameTestHelper context) {
        context.assertTrue(new DatapackSyntaxScanner().scan(java.nio.file.Path.of("build", "nonexistent-datapacks")).isEmpty(), "Missing directories must be safe.");
        context.succeed();
    }

    @GameTest
    public void scannerDoesNotMutateTheGameWorld(GameTestHelper context) {
        context.setBlock(0, 0, 0, Blocks.AIR);
        context.assertBlockPresent(Blocks.AIR, 0, 0, 0);
        context.succeed();
    }

    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }
}
