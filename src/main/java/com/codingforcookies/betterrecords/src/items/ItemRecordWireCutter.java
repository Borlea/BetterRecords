package com.codingforcookies.betterrecords.src.items;

import com.codingforcookies.betterrecords.src.betterenums.ConnectionHelper;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWire;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireHome;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireManipulator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRecordWireCutter extends Item implements IRecordWireManipulator {
	public ItemRecordWireCutter() {
		setMaxStackSize(1);
	}
	
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer playerIn, World par3World, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		TileEntity te = par3World.getTileEntity(pos);
		if(te == null || !(te instanceof IRecordWire) || te instanceof IRecordWireHome)
			return false;
		
		if(par3World.isRemote)
			return true;
		
		ConnectionHelper.clearConnections(te.getWorld(), (IRecordWire)te);
		return true;
	}
}