package com.codingforcookies.betterrecords.src.items;

import java.util.List;

import com.codingforcookies.betterrecords.src.BetterRecords;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireHome;
import com.codingforcookies.betterrecords.src.packets.PacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemURLMultiRecord extends ItemURLRecord {

	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack par1ItemStack)  {
		return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
	}
	 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("songs")) {
			NBTTagList songList = par1ItemStack.getTagCompound().getTagList("songs", 10);
			for(int i = 0; i < songList.tagCount(); i++)
				par3List.add("Song #" + (i + 1) + ": " + songList.getCompoundTagAt(i).getString("local"));
		}
		if(par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("repeat") ? par1ItemStack.getTagCompound().getBoolean("repeat") : false) {
			par3List.add("");
			par3List.add("\247eRepeat Enabled");
		}
	}

	public boolean isRecordValid(ItemStack par1ItemStack) {
		return par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("songs");
	}
	
	public void onRecordInserted(IRecordWireHome par1WireHome, ItemStack par2ItemStack) {
		PacketHandler.sendRecordPlayToAllFromServer(par1WireHome.getTileEntity().getPos(), par1WireHome.getTileEntity().getWorld().provider.getDimensionId(), par1WireHome.getSongRadius(), par2ItemStack.getTagCompound(), par2ItemStack.getTagCompound().hasKey("repeat") ? par2ItemStack.getTagCompound().getBoolean("repeat") : false);
	}
}