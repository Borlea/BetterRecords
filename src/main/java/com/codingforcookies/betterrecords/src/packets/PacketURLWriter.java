package com.codingforcookies.betterrecords.src.packets;

import com.codingforcookies.betterrecords.src.items.TileEntityFrequencyTuner;
import com.codingforcookies.betterrecords.src.items.TileEntityRecordEtcher;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketURLWriter implements IPacket {
	BlockPos pos;
	int size, color = -999;
	String name, url, localName, author;
	
	public PacketURLWriter() { }
	
	public PacketURLWriter(BlockPos pos, String name, String url, String localName, int size) {
		this.pos = pos;
		this.name = name;
		this.url = url;
		this.localName = localName;
		this.size = size;
	}
	
	public PacketURLWriter(BlockPos pos, String name, String url, String localName, int size, int color, String author) {
		this.pos = pos;
		this.name = name;
		this.url = url;
		this.localName = localName;
		this.size = size;
		this.color = color;
		this.author = author;
	}
	
	public void readBytes(ByteBuf bytes) {
		String recieved = ByteBufUtils.readUTF8String(bytes);
		String[] str = recieved.split("\2477");
		pos = new BlockPos(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
		name = str[3];
		url = str[4];
		localName = str[5];
		size = Integer.parseInt(str[6]);
		
		if(str.length > 7) {
			color = Integer.parseInt(str[7]);
			author = str[8];
		}
	}
	
	public void writeBytes(ByteBuf bytes) {
		ByteBufUtils.writeUTF8String(bytes, pos.getX() + "\2477" + pos.getY() + "\2477" + pos.getZ() + "\2477" + name + "\2477" + url + "\2477" + localName + "\2477" + size + (color != -999 ? "\2477" + color + "\2477" + author : ""));
	}
	
	public void executeClient(EntityPlayer player) { }
	
	public void executeServer(EntityPlayer player) {
		TileEntity te = player.worldObj.getTileEntity(pos);
		if(te == null || !(te instanceof TileEntityRecordEtcher || te instanceof TileEntityFrequencyTuner))
			return;
		
		if(te instanceof TileEntityRecordEtcher) {
			TileEntityRecordEtcher tileEntityRecordEtcher = (TileEntityRecordEtcher)te;
			ItemStack itemStack = tileEntityRecordEtcher.record;
			if(itemStack != null) {
				if(itemStack.getTagCompound() == null)
					itemStack.setTagCompound(new NBTTagCompound());
				itemStack.getTagCompound().setString("name", name);
				itemStack.getTagCompound().setString("url", url);
				itemStack.getTagCompound().setString("local", localName);
				itemStack.getTagCompound().setInteger("size", size);
				if(color != -999) {
					itemStack.getTagCompound().setInteger("color", color);
					itemStack.getTagCompound().setString("author", author);
				}
				player.worldObj.markBlockForUpdate(pos);
			}
		}else if(te instanceof TileEntityFrequencyTuner) {
			TileEntityFrequencyTuner tileEntityFrequencyTuner = (TileEntityFrequencyTuner)te;
			ItemStack itemStack = tileEntityFrequencyTuner.crystal;
			if(itemStack != null) {
				if(itemStack.getTagCompound() == null)
					itemStack.setTagCompound(new NBTTagCompound());
				itemStack.getTagCompound().setString("url", url);
				itemStack.getTagCompound().setString("local", localName);
				if(color != -999)
					itemStack.getTagCompound().setInteger("color", color);
				player.worldObj.markBlockForUpdate(pos);
			}
		}
	}
}