package com.codingforcookies.betterrecords.src.items;

import com.codingforcookies.betterrecords.src.BetterUtils;
import com.codingforcookies.betterrecords.src.betterenums.ConnectionHelper;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWire;
import com.codingforcookies.betterrecords.src.client.BetterEventHandler;
import com.codingforcookies.betterrecords.src.client.ClientProxy;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLazerCluster extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockLazerCluster() {
		super(Material.iron);
		setBlockBounds(0.1F, 0F, 0.1F, 0.9F, 0.85F, 0.9F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
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
		if(world.isRemote && !ClientProxy.tutorials.get("lazercluster")) {
			BetterEventHandler.tutorialText = "Connect this to a record player using wire to add some streams of color to your songs!";
			BetterEventHandler.tutorialTime = System.currentTimeMillis() + 10000;
			ClientProxy.tutorials.put("lazercluster", true);
		}
	}
	
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return getDefaultState().withProperty(FACING, facing);
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
		return new TileEntityLazerCluster();
	}

	public IBlockState getStateFromMeta(int meta){
		EnumFacing enumfacing;
		switch (meta & 7){
			case 0:
				enumfacing = EnumFacing.DOWN;
				break;
			case 1:
				enumfacing = EnumFacing.EAST;
				break;
			case 2:
				enumfacing = EnumFacing.WEST;
				break;
			case 3:
				enumfacing = EnumFacing.SOUTH;
				break;
			case 4:
				enumfacing = EnumFacing.NORTH;
				break;
			case 5:
			default:
				enumfacing = EnumFacing.UP;
		}
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	public int getMetaFromState(IBlockState state){
		int i;
		switch (SwitchEnumFacing.FACING_LOOKUP[((EnumFacing) state.getValue(FACING)).ordinal()]){
			case 1:
				i = 1;
				break;
			case 2:
				i = 2;
				break;
			case 3:
				i = 3;
				break;
			case 4:
				i = 4;
				break;
			case 5:
			default:
				i = 5;
				break;
			case 6:
				i = 0;
		}
		return i;
	}

	protected BlockState createBlockState(){
		return new BlockState(this, new IProperty[]{FACING});
	}

	static final class SwitchEnumFacing{

		static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];
		private static final String __OBFID = "CL_00002131";

		static{
			try{
				FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 1;
			}catch(NoSuchFieldError var6){
				;
			}
			try{
				FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 2;
			}catch(NoSuchFieldError var5){
				;
			}
			try{
				FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 3;
			}catch(NoSuchFieldError var4){
				;
			}
			try{
				FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 4;
			}catch(NoSuchFieldError var3){
				;
			}
			try{
				FACING_LOOKUP[EnumFacing.UP.ordinal()] = 5;
			}catch(NoSuchFieldError var2){
				;
			}
			try{
				FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 6;
			}catch(NoSuchFieldError var1){
				;
			}
		}
	}
}