package com.codingforcookies.betterrecords.src.items;

import java.util.List;

import com.codingforcookies.betterrecords.src.BetterRecords;
import com.codingforcookies.betterrecords.src.betterenums.IRecord;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireHome;
import com.codingforcookies.betterrecords.src.packets.PacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemURLRecord extends Item implements IRecord {
	
	public ItemURLRecord() {
		setMaxStackSize(1);
	}
	
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack par1ItemStack)  {
		if(par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("local"))
			return par1ItemStack.getTagCompound().getString("local");
		else
			return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("author"))
			par3List.add("By: " + par1ItemStack.getTagCompound().getString("author"));
		if(par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("size"))
			par3List.add("Size: " + par1ItemStack.getTagCompound().getString("size") + "mb");
		if(par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("repeat") ? par1ItemStack.getTagCompound().getBoolean("repeat") : false) {
			par3List.add("");
			par3List.add("\247eRepeat Enabled");
		}
	}
	
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return (par2 == 0 ? 0xFFFFFF : (par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("color") ? par1ItemStack.getTagCompound().getInteger("color") : 0xFFFFFF));
	}
	
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	public boolean isRecordValid(ItemStack par1ItemStack) {
		return par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("name");
	}
	
	public void onRecordInserted(IRecordWireHome par1WireHome, ItemStack par2ItemStack) {
		PacketHandler.sendRecordPlayToAllFromServer(par1WireHome.getTileEntity().getPos(), par1WireHome.getTileEntity().getWorld().provider.getDimensionId(), par1WireHome.getSongRadius(), par2ItemStack.getTagCompound().getString("name"), par2ItemStack.getTagCompound().getString("url"), par2ItemStack.getTagCompound().getString("local"), par2ItemStack.getTagCompound().hasKey("repeat") ? par2ItemStack.getTagCompound().getBoolean("repeat") : false);
	}
}