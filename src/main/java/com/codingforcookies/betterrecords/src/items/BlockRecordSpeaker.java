package com.codingforcookies.betterrecords.src.items;

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

public class BlockRecordSpeaker extends BlockContainer {
	public static String[] speakers = new String[]{ "sm", "md", "lg" };
	public int meta = 0;
	
	public BlockRecordSpeaker(int meta) {
		super(Material.wood);
		this.meta = meta;
	}
	
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		world.markBlockForUpdate(pos);
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos pos) {
		switch(meta) {
			case 0:
				setBlockBounds(0.26F, 0.05F, 0.25F, 0.75F, 0.65F, 0.74F);
				break;
			case 1:
				setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.88F, 0.8F);
				break;
			case 2:
				setBlockBounds(0.12F, 0.0F, 0.12F, 0.88F, 1.51F, 0.88F);
				break;
			default:
				break;
		}
	}
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity == null || !(tileEntity instanceof TileEntityRecordSpeaker))
			return;
		
		((TileEntityRecordSpeaker)tileEntity).rotation = placer.rotationYaw;
		((TileEntityRecordSpeaker)tileEntity).type = meta;
		
		if(world.isRemote && !ClientProxy.tutorials.get("speaker")) {
			BetterEventHandler.tutorialText = "Connect this to a record player using speaker wire to increase the play radius!";
			BetterEventHandler.tutorialTime = System.currentTimeMillis() + 10000;
			ClientProxy.tutorials.put("speaker", true);
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
		return new TileEntityRecordSpeaker();
	}
}