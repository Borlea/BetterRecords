package com.codingforcookies.betterrecords.src.items;

import com.codingforcookies.betterrecords.src.BetterUtils;
import com.codingforcookies.betterrecords.src.betterenums.ConnectionHelper;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWire;
import com.codingforcookies.betterrecords.src.client.BetterEventHandler;
import com.codingforcookies.betterrecords.src.client.ClientProxy;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLazer extends BlockContainer {
	public BlockLazer() {
		super(Material.iron);
		setBlockBounds(0.25F, 0F, 0.25F, 0.75F, 0.75F, 0.74F);
	}
	
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te == null || !(te instanceof IRecordWire))
			return 0;
		
		BetterUtils.markBlockDirty(te.getWorld(), te.getPos());
		
        return (((IRecordWire)te).getConnections().size() > 0 ? 5 : 0);
    }
	
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		world.markBlockForUpdate(pos);
	}
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TileEntityLazer) {
			((TileEntityLazer)te).pitch = placer.rotationPitch;
			((TileEntityLazer)te).yaw = placer.rotationYaw;
		}
		
		if(world.isRemote && !ClientProxy.tutorials.get("lazer")) {
			BetterEventHandler.tutorialText = "Connect this to a record player using wire to add a stream of color to your songs!";
			BetterEventHandler.tutorialTime = System.currentTimeMillis() + 10000;
			ClientProxy.tutorials.put("lazer", true);
		}
	}
	
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest){
		if(world.isRemote)
			return super.removedByPlayer(world, pos, player, willHarvest);
		
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof IRecordWire)
			ConnectionHelper.clearConnections(world, (IRecordWire)te);
		
		return super.removedByPlayer(world, pos, player, willHarvest);
	}
	
	@SideOnly(Side.CLIENT)
    public int getRenderType() {
		return -1;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}
	
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityLazer();
	}
}