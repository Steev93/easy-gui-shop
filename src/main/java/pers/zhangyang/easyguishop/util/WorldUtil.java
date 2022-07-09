package pers.zhangyang.easyguishop.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.exception.FailureCreateWorldException;

import java.util.Random;

public class WorldUtil {
    public static World getVoidWorld(@NotNull String worldName) throws FailureCreateWorldException {

        WorldCreator worldCreator = new WorldCreator(worldName);


        if (MinecraftVersionUtil.getBigVersion() == 1 && MinecraftVersionUtil.getMiddleVersion() < 13) {
            worldCreator.type(WorldType.FLAT);
            worldCreator.generatorSettings("2;0;1;"); //This is what makes the world empty (void)
            worldCreator.createWorld();
        } else if (MinecraftVersionUtil.getBigVersion() == 1 && MinecraftVersionUtil.getMiddleVersion() < 18) {
            worldCreator.generator(
                    new ChunkGenerator() {
                        @NotNull
                        @Override
                        public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull ChunkGenerator.BiomeGrid biome) {
                            return Bukkit.createChunkData(world);
                        }
                    });
        } else {
            worldCreator.generator(
                    new ChunkGenerator() {
                    });

        }
        World world = worldCreator.createWorld();
        if (world == null) {
            throw new FailureCreateWorldException();
        }
        return world;
    }
}
