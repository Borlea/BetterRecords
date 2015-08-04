package com.codingforcookies.betterrecords.src.items;

import java.util.Random;

import com.codingforcookies.betterrecords.src.BetterRecords;
import com.codingforcookies.betterrecords.src.BetterUtils;
import com.codingforcookies.betterrecords.src.betterenums.ConnectionHelper;
import com.codingforcookies.betterrecords.src.betterenums.IRecord;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWire;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireManipulator;
import com.codingforcookies.betterrecords.src.client.BetterEventHandler;
import com.codingforcookies.betterrecords.src.client.ClientProxy;
import com.codingforcookies.betterrecords.src.client.sound.SoundHandler;
import com.codingforcookies.betterrecords.src.packets.PacketHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRecordPlayer extends BlockContainer {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockRecordPlayer() {
		super(Material.wood);
		setBlockBounds(.025F, 0F, .025F, .975F, .975F, .975F);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		world.markBlockForUpdate(pos);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IRecordWireManipulator)
			return false;
		
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity == null || !(tileEntity instanceof TileEntityRecordPlayer))
			return false;
		
		TileEntityRecordPlayer tileEntityRecordPlayer = (TileEntityRecordPlayer)tileEntity;
		
		if(player.isSneaking()) {
			if(world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock() == Blocks.air) {
				tileEntityRecordPlayer.opening = !tileEntityRecordPlayer.opening;
				world.markBlockForUpdate(pos);
				if(tileEntityRecordPlayer.opening)
					world.playSoundEffect(pos.getX(), (double)pos.getY() + 0.5D, pos.getZ(), "random.chestopen", 0.5F, world.rand.nextFloat() * 0.2F + 3F);
				else
					world.playSoundEffect(pos.getX(), (double)pos.getY() + 0.5D, pos.getZ(), "random.chestclosed", 0.5F, world.rand.nextFloat() * 0.2F + 3F);
			}
		}else if(tileEntityRecordPlayer.opening) {
			if(tileEntityRecordPlayer.record != null) {
				if(!world.isRemote)
					dropItem(world, pos);
				tileEntityRecordPlayer.setRecord(null);
				
				world.markBlockForUpdate(pos);
			}else if(player.getHeldItem() != null && (player.getHeldItem().getItem() == Items.diamond || (player.getHeldItem().getItem() instanceof IRecord && ((IRecord)player.getHeldItem().getItem()).isRecordValid(player.getHeldItem())))) {
				if(player.getHeldItem().getItem() == Items.diamond) {
					ItemStack itemStack = new ItemStack(BetterRecords.itemURLRecord);
					itemStack.setTagCompound(new NBTTagCompound());
					itemStack.getTagCompound().setString("name", "easteregg.ogg");
					itemStack.getTagCompound().setString("url", "http://files.enjin.com/788858/SBear'sMods/Songs/easteregg.ogg");
					itemStack.getTagCompound().setString("local", "Darude - Sandstorm");
					itemStack.getTagCompound().setInteger("color", 0x53EAD7);
					tileEntityRecordPlayer.setRecord(itemStack);
					world.markBlockForUpdate(pos);
					player.getHeldItem().stackSize--;
				}else{
					tileEntityRecordPlayer.setRecord(player.getHeldItem());
					world.markBlockForUpdate(pos);
					
					player.getHeldItem().stackSize--;
					if(!world.isRemote)
						((IRecord)player.getHeldItem().getItem()).onRecordInserted(tileEntityRecordPlayer, player.getHeldItem());
				}
			}
		}
		
		return true;
	}
	
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		if(world.isRemote && !ClientProxy.tutorials.get("recordplayer")) {
			BetterEventHandler.tutorialText = "Shift+Right Click while holding nothing to open and close the record player!";
			BetterEventHandler.tutorialTime = System.currentTimeMillis() + 10000;
			ClientProxy.tutorials.put("recordplayer", true);
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
		if(tileEntity == null || !(tileEntity instanceof TileEntityRecordPlayer))
			return;
		
		TileEntityRecordPlayer tileEntityRecordPlayer = (TileEntityRecordPlayer)tileEntity;
		ItemStack item = tileEntityRecordPlayer.record;
		
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
			
			tileEntityRecordPlayer.record = null;
			PacketHandler.sendSoundStopToAllFromServer(tileEntityRecordPlayer.getPos(), world.provider.getDimensionId());
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
		return new TileEntityRecordPlayer();
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