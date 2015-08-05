package com.codingforcookies.betterrecords.src.items;

import com.codingforcookies.betterrecords.src.BetterRecords;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class TileEntityFrequencyTuner extends TileEntity implements IInventory, IUpdatePlayerListBox {
	public ItemStack crystal = null;
	public float crystalFloaty = 0F;
	
	public TileEntityFrequencyTuner() { }

	public void setRecord(ItemStack par1ItemStack) {
		if(par1ItemStack != null)
			crystal = par1ItemStack.copy();
		else
			crystal = null;
	}
	
	public void update() {
		if(crystal != null)
			crystalFloaty += 0.86F;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if(compound.hasKey("crystal"))
			setRecord(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("crystal")));
	}
	
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setInteger("rotation", getBlockMetadata());
		compound.setTag("crystal", getStackTagCompound(crystal));
	}

	public NBTTagCompound getStackTagCompound(ItemStack stack) {
		NBTTagCompound tag = new NBTTagCompound();
		if(stack != null)
			stack.writeToNBT(tag);
		return tag;
	}
	
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(pos, 1, nbt);
	}
	
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)  { 
		readFromNBT(pkt.getNbtCompound());
		Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(pos);
	}
	
	public int getSizeInventory() {
		return 1;
	}
	
	public ItemStack getStackInSlot(int slot) {
		return crystal;
	}
	
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if(stack != null)
			if(stack.stackSize <= amt)
				setInventorySlotContents(slot, null);
			else
				stack = stack.splitStack(amt);
				if(stack.stackSize == 0)
					setInventorySlotContents(slot, null);
		return stack;
	}
	
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if(stack != null)
			setInventorySlotContents(slot, null);
		return stack;
	}
	
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		setRecord(itemStack);
	}
	
	public int getInventoryStackLimit() {
		return 1;
	}
	
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64;
	}
	
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return itemStack.getItem() == BetterRecords.itemFreqCrystal && (!itemStack.hasTagCompound() || !itemStack.getTagCompound().hasKey("url"));
	}

	@Override
	public String getName(){
		return "Frequency Tuner";
	}

	@Override
	public boolean hasCustomName(){
		return true;
	}

	@Override
	public IChatComponent getDisplayName(){
		return null;
	}

	@Override
	public void openInventory(EntityPlayer player){ }

	@Override
	public void closeInventory(EntityPlayer player){ }

	@Override
	public int getField(int id){
		return 0;
	}

	@Override
	public void setField(int id, int value){}

	@Override
	public int getFieldCount(){
		return 0;
	}

	@Override
	public void clear(){}
	
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return newState.getBlock() == Blocks.air ? true : false;
	}
}