package com.codingforcookies.betterrecords.src.client;

import com.codingforcookies.betterrecords.src.BetterRecords;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BetterCreativeTab extends CreativeTabs {
	public BetterCreativeTab() {
		super("betterrecords");
	}
	
	public Item getTabIconItem() {
		return BetterRecords.itemURLRecord;
	}
}