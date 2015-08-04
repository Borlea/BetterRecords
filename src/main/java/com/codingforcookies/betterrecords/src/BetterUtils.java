package com.codingforcookies.betterrecords.src;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BetterUtils {
	public static void markBlockDirty(World par1World, BlockPos pos) {
		if(!par1World.isAirBlock(pos))
			par1World.getChunkFromBlockCoords(pos).setChunkModified();
	}
}