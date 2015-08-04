package com.codingforcookies.betterrecords.src.packets;

import com.codingforcookies.betterrecords.src.client.sound.SoundHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketSoundStop implements IPacket {
	BlockPos pos;
	int dimension;
	
	public PacketSoundStop() { }
	
	public PacketSoundStop(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public void readBytes(ByteBuf bytes) {
		String recieved = ByteBufUtils.readUTF8String(bytes);
		String[] str = recieved.split("\\]");
		pos = new BlockPos(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
		dimension = Integer.parseInt(str[3]);
	}
	
	public void writeBytes(ByteBuf bytes) {
		ByteBufUtils.writeUTF8String(bytes, pos.getX() + "]" + pos.getY() + "]" + pos.getZ() + "]" + dimension);
	}
	
	public void executeClient(EntityPlayer player) {
		if(SoundHandler.soundPlaying.containsKey(pos.getX() + "," + pos.getY() + "," + pos.getZ() + "," + dimension))
			SoundHandler.soundPlaying.remove(pos.getX() + "," + pos.getY() + "," + pos.getZ() + "," + dimension);
	}
	
	public void executeServer(EntityPlayer player) { }
}