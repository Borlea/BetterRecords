package com.codingforcookies.betterrecords.src.gui;

import com.codingforcookies.betterrecords.src.items.TileEntityFrequencyTuner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFrequencyTuner extends Container {
	protected TileEntityFrequencyTuner tileEntity;
	
	public ContainerFrequencyTuner(InventoryPlayer inventoryPlayer, TileEntityFrequencyTuner te) {
		tileEntity = te;
		
		addSlotToContainer(new SlotValid(tileEntity, 0, 17, 50));
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
	}
	
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return null;
	}
}