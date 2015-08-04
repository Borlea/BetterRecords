package com.codingforcookies.betterrecords.src.packets;

import com.codingforcookies.betterrecords.src.betterenums.ConnectionHelper;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWire;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireHome;
import com.codingforcookies.betterrecords.src.betterenums.RecordConnection;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;


public class PacketWireConnection implements IPacket {
	private RecordConnection connection;
	
	public PacketWireConnection() { }
	
	public PacketWireConnection(RecordConnection connection) {
		this.connection = connection;
	}
	
	public void readBytes(ByteBuf bytes) {
		connection = new RecordConnection(ByteBufUtils.readUTF8String(bytes));
	}
	
	public void writeBytes(ByteBuf bytes) {
		ByteBufUtils.writeUTF8String(bytes, connection.toString());
	}
	
	public void executeClient(EntityPlayer player) { }
	
	public void executeServer(EntityPlayer player) {
		TileEntity te1 = player.worldObj.getTileEntity(connection.pos1);
		TileEntity te2 = player.worldObj.getTileEntity(connection.pos2);
		if(te1 instanceof IRecordWire && te2 instanceof IRecordWire) {
			if(!(te1 instanceof IRecordWireHome && te2 instanceof IRecordWireHome)) {
				ConnectionHelper.addConnection(player.worldObj, (IRecordWire)te1, connection);
				ConnectionHelper.addConnection(player.worldObj, (IRecordWire)te2, connection);
			}
		}
	}
}