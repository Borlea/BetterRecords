package com.codingforcookies.betterrecords.src.betterenums;

import net.minecraft.util.BlockPos;

public class RecordConnection {
	public BlockPos pos1, pos2;
	public boolean fromHome = false;
	
	public RecordConnection(BlockPos pos, boolean fromHome) {
		if(fromHome)
			setConnection1(pos);
		else
			setConnection2(pos);
		this.fromHome = fromHome;
	}
	
	public RecordConnection(String string) {
		String[] str = string.split(",");
		pos1 = new BlockPos(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
		pos2 = new BlockPos(Integer.parseInt(str[3]), Integer.parseInt(str[4]), Integer.parseInt(str[5]));

	}
	
	public String toString() {
		return pos1.getX() + "," + pos1.getY() + "," + pos1.getZ() + "," + pos2.getX() + "," + pos2.getY() + "," + pos2.getZ();
	}

	public RecordConnection setConnection1(BlockPos pos1) {
		this.pos1 = pos1;
		return this;
	}
	
	public RecordConnection setConnection2(BlockPos pos2) {
		this.pos2 = pos2;
		return this;
	}

	public boolean sameInitial(BlockPos pos) {
		if(pos1 == null)
			return false;
		return pos1.equals(pos);
	}

	public boolean same(BlockPos pos1, BlockPos pos2) {
		return this.pos1.equals(pos1) && this.pos2.equals(pos2);
	}

	public boolean same(RecordConnection rec) {
		return pos1.equals(rec.pos1) && pos2.equals(rec.pos2);
	}

	public boolean sameBetween(RecordConnection rec) {
		return pos1.equals(rec.pos2) && pos2.equals(rec.pos1);
	}
}