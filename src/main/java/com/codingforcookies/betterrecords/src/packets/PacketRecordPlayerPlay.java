package com.codingforcookies.betterrecords.src.packets;

import java.util.ArrayList;
import java.util.List;

import com.codingforcookies.betterrecords.src.client.sound.Sound;
import com.codingforcookies.betterrecords.src.client.sound.SoundHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;


public class PacketRecordPlayerPlay implements IPacket {
	BlockPos pos;
	int dimension;
	float playRadius;
	boolean repeat;
	
	List<Sound> sounds = null;
	
	public PacketRecordPlayerPlay() { }
	
	public PacketRecordPlayerPlay(BlockPos pos, float playRadius, int dimension, String name, String url, String local, boolean repeat) {
		this.pos = pos;
		this.playRadius = playRadius;
		this.dimension = dimension;
		this.repeat = repeat;
		
		sounds = new ArrayList<Sound>();
		sounds.add(new Sound().setInfo(name, url, local));
	}
	
	public PacketRecordPlayerPlay(BlockPos pos, float playRadius, int dimension, NBTTagCompound nbt, boolean repeat) {
		this.pos = pos;
		this.playRadius = playRadius;
		this.dimension = dimension;
		this.repeat = repeat;
		
		sounds = new ArrayList<Sound>();
		NBTTagList songList = nbt.getTagList("songs", 10);
		for(int i = 0; i < songList.tagCount(); i++)
			sounds.add(new Sound().setInfo(songList.getCompoundTagAt(i).getString("name"), songList.getCompoundTagAt(i).getString("url"), songList.getCompoundTagAt(i).getString("local")));
	}
	
	public void readBytes(ByteBuf bytes) {
		String recieved = ByteBufUtils.readUTF8String(bytes);
		String[] str = recieved.split("\2477");
		pos = new BlockPos(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
		playRadius = Float.parseFloat(str[3]);
		dimension = Integer.parseInt(str[4]);
		repeat = Boolean.parseBoolean(str[5]);

		sounds = new ArrayList<Sound>();
		for(String strr : str[6].split("\2478")) {
			Sound snd = Sound.fromString(strr);
			snd.pos = pos;
			snd.dimension = dimension;
			snd.playRadius = playRadius;
			sounds.add(snd);
		}
	}

	public void writeBytes(ByteBuf bytes) {
		String sndStr = "";
		if(sounds != null)
			for(Sound snd : sounds)
				sndStr += snd.toString() + "\2478";
		ByteBufUtils.writeUTF8String(bytes, pos.getX() + "\2477" + pos.getY() + "\2477" + pos.getZ() + "\2477" + playRadius + "\2477" + dimension + "\2477" + repeat + "\2477" + sndStr.substring(0, sndStr.length() - 2));
	}
	
	public void executeClient(EntityPlayer player) {
		if(playRadius > 100000 || (float)Math.abs(Math.sqrt(Math.pow(player.posX - pos.getX(), 2) + Math.pow(player.posY - pos.getY(), 2) + Math.pow(player.posZ - pos.getZ(), 2))) < playRadius) {
			SoundHandler.playSound(pos, dimension, playRadius, sounds, repeat);
		}
	}
	
	public void executeServer(EntityPlayer player) { }
}