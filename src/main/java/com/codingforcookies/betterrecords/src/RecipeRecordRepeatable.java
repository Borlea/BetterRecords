package com.codingforcookies.betterrecords.src;

import com.codingforcookies.betterrecords.src.items.ItemURLRecord;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeRecordRepeatable implements IRecipe {
	public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World) {
		ItemStack record = null;
		boolean comparator = false;
		
		for(int i = 0; i < par1InventoryCrafting.getSizeInventory(); ++i) {
			ItemStack itemstack = par1InventoryCrafting.getStackInSlot(i);
			if(itemstack != null) {
				if(itemstack.getItem() instanceof ItemURLRecord && itemstack.getTagCompound() != null)
					if(record != null)
						return false;
					else
						record = itemstack;
				else if(itemstack.getItem() == Items.comparator)
					if(comparator)
						return false;
					else
						comparator = true;
				else
					return false;
			}
		}
		
		return record != null && comparator;
	}
	
	public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
		ItemStack record = null;
		boolean comparator = false;
		
		for(int i = 0; i < par1InventoryCrafting.getSizeInventory(); i++) {
			ItemStack itemstack = par1InventoryCrafting.getStackInSlot(i);
			if(itemstack != null) {
				if(itemstack.getItem() instanceof ItemURLRecord && itemstack.getTagCompound() != null)
					if(record != null)
						return null;
					else
						record = itemstack;
				else if(itemstack.getItem() == Items.comparator)
					if(comparator)
						return null;
					else
						comparator = true;
				else
					return null;
			}
		}
		
		if(record == null || !comparator)
			return null;
		else{
			ItemStack newRecord = ItemStack.copyItemStack(record);
			
			if(newRecord.getTagCompound() == null)
				newRecord.setTagCompound(new NBTTagCompound());
			
			newRecord.getTagCompound().setBoolean("repeat", true);
			
			return newRecord;
		}
	}
	
	public int getRecipeSize() {
		return 10;
	}

	public ItemStack getRecipeOutput() {
		return null;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting){
        ItemStack[] aitemstack = new ItemStack[inventoryCrafting.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i){
            ItemStack itemstack = inventoryCrafting.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
	}
}