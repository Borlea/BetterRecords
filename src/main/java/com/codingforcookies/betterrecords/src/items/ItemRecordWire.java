package com.codingforcookies.betterrecords.src.items;

import com.codingforcookies.betterrecords.src.betterenums.ConnectionHelper;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWire;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireHome;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireManipulator;
import com.codingforcookies.betterrecords.src.betterenums.RecordConnection;
import com.codingforcookies.betterrecords.src.packets.PacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRecordWire extends Item implements IRecordWireManipulator {
	public static RecordConnection connection;
	
	public ItemRecordWire() {
		
	}
	
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer playerIn, World par3World, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!par3World.isRemote)
			return true;
		
		TileEntity te = par3World.getTileEntity(pos);
		if(te == null || !(te instanceof IRecordWire))
			return false;
		
		if(connection == null) {
			connection = new RecordConnection(pos, te instanceof IRecordWireHome);
		}else{
			float x1 = -(float)(pos.getX() - (connection.fromHome ? connection.pos1.getX() : connection.pos2.getX()));
			float y1 = -(float)(pos.getY() - (connection.fromHome ? connection.pos1.getY() : connection.pos2.getY()));
			float z1 = -(float)(pos.getZ() - (connection.fromHome ? connection.pos1.getZ() : connection.pos2.getZ()));
			
			if((int)Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2) + Math.pow(z1, 2)) > 7 || connection.sameInitial(pos)) {
				connection = null;
				return true;
			}
			
			if(!connection.fromHome)
				connection.setConnection1(pos);
			else
				connection.setConnection2(pos);
			
			TileEntity te1 = par3World.getTileEntity(connection.pos1);
			TileEntity te2 = par3World.getTileEntity(connection.pos2);
			
			if(te2 instanceof IRecordWire) {
				if(!(te1 instanceof IRecordWireHome && te2 instanceof IRecordWireHome)) {
					ConnectionHelper.addConnection(te.getWorld(), (IRecordWire)te1, connection);
					ConnectionHelper.addConnection(te.getWorld(), (IRecordWire)te2, connection);
					PacketHandler.sendWireConnectionFromClient(connection);
					par1ItemStack.stackSize--;
				}
			}
			
			connection = null;
		}
		return true;
	}
}