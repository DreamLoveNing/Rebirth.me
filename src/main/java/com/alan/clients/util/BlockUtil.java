//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Administrator\Downloads\Minecraft1.12.2 Mappings"!

//Decompiled by Procyon!

package com.alan.clients.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.*;

public final class BlockUtil
{
    private static final Minecraft mc;
    private static final List<Integer> nonValidItems;

    public static Block getBlock(final BlockPos blockPos) {
        return BlockUtil.mc.theWorld.getBlockState(blockPos).getBlock();
    }
    public static Block getBlockAtPos(BlockPos pos) {
        IBlockState blockState = mc.theWorld.getBlockState(pos);
        return blockState.getBlock();
    }
    public static boolean isValidStack(final ItemStack itemStack) {
        final Item item = itemStack.getItem();
        if (item instanceof ItemSlab || item instanceof ItemLeaves || item instanceof ItemSnow || item instanceof ItemBanner || item instanceof ItemFlintAndSteel) {
            return false;
        }
        for (final int item2 : BlockUtil.nonValidItems) {
            if (item.equals(Item.getItemById(item2))) {
                return false;
            }
        }
        return true;
    }

    public static Vec3 floorVec3(final Vec3 vec3) {
        return new Vec3(Math.floor(vec3.xCoord), Math.floor(vec3.yCoord), Math.floor(vec3.zCoord));
    }

    public static Material getMaterial(final BlockPos blockPos) {
        return getBlock(blockPos).getMaterial();
    }

    public static boolean isReplaceable(final BlockPos blockPos) {
        return getMaterial(blockPos).isReplaceable();
    }

    public static IBlockState getState(final BlockPos pos) {
        return BlockUtil.mc.theWorld.getBlockState(pos);
    }

    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    public static String getBlockName(final int id) {
        return Block.getBlockById(id).getLocalizedName();
    }

    public static boolean isFullBlock(final BlockPos blockPos) {
        final AxisAlignedBB axisAlignedBB = getBlock(blockPos).getCollisionBoundingBox((World)BlockUtil.mc.theWorld, blockPos, getState(blockPos));
        return axisAlignedBB != null && axisAlignedBB.maxX - axisAlignedBB.minX == 1.0 && axisAlignedBB.maxY - axisAlignedBB.minY == 1.0 && axisAlignedBB.maxZ - axisAlignedBB.minZ == 1.0;
    }

    public static double getCenterDistance(final BlockPos blockPos) {
        return BlockUtil.mc.thePlayer.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    public static Map<BlockPos, Block> searchBlocks(final int radius) {
        final Map<BlockPos, Block> blocks = new HashMap<BlockPos, Block>();
        for (int x = radius; x > -radius; --x) {
            for (int y = radius; y > -radius; --y) {
                for (int z = radius; z > -radius; --z) {
                    final BlockPos blockPos = new BlockPos(BlockUtil.mc.thePlayer.lastTickPosX + x, BlockUtil.mc.thePlayer.lastTickPosY + y, BlockUtil.mc.thePlayer.lastTickPosZ + z);
                    final Block block = getBlock(blockPos);
                    blocks.put(blockPos, block);
                }
            }
        }
        return blocks;
    }
    public static List<BlockPos> searchBlocks(EntityLivingBase entity, int range) {
        return searchBlocks(entity, range, range, range);
    }
    public static List<BlockPos> searchBlocks(EntityLivingBase entity, int xRange, int yRange, int zRange) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        World world = mc.theWorld;
        BlockPos entityPos = entity.getPosition();

        if (world == null) {
            return foundBlocks;
        }

        int startX = entityPos.getX() - xRange;
        int endX = entityPos.getX() + xRange;
        int startY = entityPos.getY() - yRange;
        int endY = entityPos.getY() + yRange;
        int startZ = entityPos.getZ() - zRange;
        int endZ = entityPos.getZ() + zRange;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = world.getBlockState(pos).getBlock();
                    if (!(block instanceof BlockAir)) {
                        foundBlocks.add(pos);
                    }
                }
            }
        }

        return foundBlocks;
    }
    public static boolean collideBlock(final AxisAlignedBB axisAlignedBB, final ICollide collide) {
        for (int x = MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = getBlock(new BlockPos((double)x, axisAlignedBB.minY, (double)z));
                if (block != null && !collide.collideBlock(block)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean collideBlockIntersects(final AxisAlignedBB axisAlignedBB, final ICollide collide) {
        for (int x = MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final BlockPos blockPos = new BlockPos((double)x, axisAlignedBB.minY, (double)z);
                final Block block = getBlock(blockPos);
                if (block != null && collide.collideBlock(block)) {
                    final AxisAlignedBB boundingBox = block.getCollisionBoundingBox((World)BlockUtil.mc.theWorld, blockPos, getState(blockPos));
                    if (boundingBox != null && BlockUtil.mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static {
        nonValidItems = Arrays.asList(30, 58, 116, 158, 23, 6, 54, 146, 130, 26, 50, 76, 46, 37, 38);
        mc = Minecraft.getMinecraft();
    }

    public interface ICollide
    {
        boolean collideBlock(final Block p0);
    }
}