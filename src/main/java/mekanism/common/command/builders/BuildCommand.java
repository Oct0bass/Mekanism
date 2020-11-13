package mekanism.common.command.builders;

import com.mojang.brigadier.builder.ArgumentBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import mekanism.common.Mekanism;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;

public class BuildCommand {

    private BuildCommand() {
    }

    public static final ArgumentBuilder<CommandSource, ?> COMMAND = Commands.literal("build")
          .then(Commands.literal("remove")
                .requires(cs -> cs.hasPermissionLevel(2))
                .executes(ctx -> {
                    CommandSource source = ctx.getSource();
                    Entity entity = source.getEntity();
                    if (entity instanceof ServerPlayerEntity) {
                        ServerPlayerEntity player = (ServerPlayerEntity) ctx.getSource().getEntity();
                        BlockRayTraceResult result = MekanismUtils.rayTrace(player, 100);
                        if (result.getType() != RayTraceResult.Type.MISS) {
                            destroy(source.getWorld(), result.getPos());
                        }
                    }
                    return 0;
                }));

    public static void register(String name, StructureBuilder builder) {
        COMMAND.then(Commands.literal(name)
              .requires(cs -> cs.hasPermissionLevel(2))
              .executes(ctx -> {
                  CommandSource source = ctx.getSource();
                  Entity entity = source.getEntity();
                  if (entity instanceof ServerPlayerEntity) {
                      ServerPlayerEntity player = (ServerPlayerEntity) ctx.getSource().getEntity();
                      BlockRayTraceResult result = MekanismUtils.rayTrace(player, 100);
                      if (result.getType() != RayTraceResult.Type.MISS) {
                          BlockPos pos = result.getPos().offset(Direction.UP);
                          builder.build(source.getWorld(), pos);
                      }
                  }
                  return 0;
              }));
    }

    private static void destroy(World world, BlockPos pos) {
        Long2ObjectMap<IChunk> chunkMap = new Long2ObjectOpenHashMap<>();
        Set<BlockPos> traversed = new HashSet<>();
        Queue<BlockPos> openSet = new ArrayDeque<>();
        openSet.add(pos);
        traversed.add(pos);
        while (!openSet.isEmpty()) {
            BlockPos ptr = openSet.poll();
            Optional<BlockState> state = WorldUtils.getBlockState(world, chunkMap, ptr);
            if (state.isPresent() && state.get().getBlock().getRegistryName().getNamespace().startsWith(Mekanism.MODID)) {
                world.removeBlock(ptr, false);
                for (Direction side : EnumUtils.DIRECTIONS) {
                    BlockPos offset = ptr.offset(side);
                    if (traversed.add(offset)) {
                        openSet.add(offset);
                    }
                }
            }
        }
    }
}
