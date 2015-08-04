package com.codingforcookies.betterrecords.src.packets;

import com.codingforcookies.betterrecords.src.client.sound.SoundHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;


public class PacketRadioPlay implements IPacket {
	BlockPos pos;
	int dimension;
	float playRadius;
	String localName, url;
	
	public PacketRadioPlay() { }
	
	public PacketRadioPlay(BlockPos pos, float playRadius, int dimension, String localName, String url) {
		this.pos = pos;
		this.playRadius = playRadius;
		this.dimension = dimension;
		this.localName = localName;
		this.url = url;
	}
	
	public void readBytes(ByteBuf bytes) {
		String recieved = ByteBufUtils.readUTF8String(bytes);
		String[] str = recieved.split("\2477");
		pos = new BlockPos(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
		playRadius = Float.parseFloat(str[3]);
		dimension = Integer.parseInt(str[4]);
		localName = str[5];
		url = str[6];
	}
	
	public void writeBytes(ByteBuf bytes) {
		ByteBufUtils.writeUTF8String(bytes, pos.getX() + "\2477" + pos.getY() + "\2477" + pos.getZ() + "\2477" + playRadius + "\2477" + dimension + "\2477" + localName + "\2477" + url);
	}
	
	public void executeClient(EntityPlayer player) {
		if(playRadius > 100000 || (float)Math.abs(Math.sqrt(Math.pow(player.posX - pos.getX(), 2) + Math.pow(player.posY - pos.getY(), 2) + Math.pow(player.posZ - pos.getZ(), 2))) < playRadius)
			SoundHandler.playSoundFromStream(pos, dimension, playRadius, localName, url);
	}
	
	public void executeServer(EntityPlayer player) { }
}