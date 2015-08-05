package com.codingforcookies.betterrecords.src.items;

import java.util.Random;

import com.codingforcookies.betterrecords.src.BetterRecords;
import com.codingforcookies.betterrecords.src.betterenums.ConnectionHelper;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWire;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireManipulator;
import com.codingforcookies.betterrecords.src.client.BetterEventHandler;
import com.codingforcookies.betterrecords.src.client.ClientProxy;
import com.codingforcookies.betterrecords.src.packets.PacketHandler;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRadio extends BlockContainer {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public BlockRadio() {
		super(Material.wood);
		setBlockBounds(0.13F, 0F, 0.2F, 0.87F, 0.98F, 0.8F);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess block, BlockPos pos) {
		switch(block.getTileEntity(pos).getBlockMetadata()) {
			case 0:
			case 2:
				setBlockBounds(0.13F, 0F, 0.2F, 0.87F, 0.98F, 0.8F);
				break;
			case 1:
			case 3:
				setBlockBounds(0.2F, 0F, 0.13F, 0.8F, 0.98F, 0.87F);
				break;
		}
	}
	
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		world.markBlockForUpdate(pos);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IRecordWireManipulator)
			return false;
		
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity == null || !(tileEntity instanceof TileEntityRadio))
			return false;
		
		TileEntityRadio tileEntityRadio = (TileEntityRadio)tileEntity;
		
		if(player.isSneaking()) {
			tileEntityRadio.opening = !tileEntityRadio.opening;
			world.markBlockForUpdate(pos);
			if(tileEntityRadio.opening)
				world.playSoundEffect(pos.getX(), (double)pos.getY() + 0.5D, pos.getZ(), "random.chestopen", 0.2F, world.rand.nextFloat() * 0.2F + 3F);
			else
				world.playSoundEffect(pos.getX(), (double)pos.getY() + 0.5D, pos.getZ(), "random.chestclosed", 0.2F, world.rand.nextFloat() * 0.2F + 3F);
		}else if(tileEntityRadio.opening) {
			if(tileEntityRadio.crystal != null) {
				if(!world.isRemote)
					dropItem(world, pos);
				tileEntityRadio.setCrystal(null);
	
				world.markBlockForUpdate(pos);
			}else if(player.getHeldItem() != null && (player.getHeldItem().getItem() == BetterRecords.itemFreqCrystal && player.getHeldItem().getTagCompound() != null && player.getHeldItem().getTagCompound().hasKey("url"))) {
				tileEntityRadio.setCrystal(player.getHeldItem());
				world.markBlockForUpdate(pos);
	
				player.getHeldItem().stackSize--;
	
				if(!world.isRemote)
					PacketHandler.sendRadioPlayToAllFromServer(tileEntityRadio.getPos(), world.provider.getDimensionId(), tileEntityRadio.getSongRadius(), tileEntityRadio.crystal.getTagCompound().getString("name"), tileEntityRadio.crystal.getTagCompound().getString("url"));
			}
		}
		
		return true;
	}
	
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		
		if(world.isRemote && !ClientProxy.tutorials.get("radio")) {
			BetterEventHandler.tutorialText = "Insert a tuned frequency crystal to start streaming!";
			BetterEventHandler.tutorialTime = System.currentTimeMillis() + 10000;
			ClientProxy.tutorials.put("radio", true);
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
	
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		dropItem(world, pos);
		super.breakBlock(world, pos, state);
	}
	
	private void dropItem(World world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity == null || !(tileEntity instanceof TileEntityRadio))
			return;
		
		TileEntityRadio tileEntityRadio = (TileEntityRadio)tileEntity;
		ItemStack item = tileEntityRadio.crystal;
		
		if(item != null) {
			Random rand = new Random();
			
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			
			EntityItem entityItem = new EntityItem(world, pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));
			
			if(item.hasTagCompound())
				entityItem.getEntityItem().setTagCompound((NBTTagCompound)item.getTagCompound().copy());
			
			entityItem.motionX = rand.nextGaussian() * 0.05F;
			entityItem.motionY = rand.nextGaussian() * 0.05F + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * 0.05F;
			world.spawnEntityInWorld(entityItem);
			item.stackSize = 0;
			
			tileEntityRadio.crystal = null;
			PacketHandler.sendSoundStopToAllFromServer(tileEntityRadio.getPos(), world.provider.getDimensionId());
		}
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
		return new TileEntityRadio();
	}
	
    public IBlockState getStateFromMeta(int meta){
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y){
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state){
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[] {FACING});
    }
}