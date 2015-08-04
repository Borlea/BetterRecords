package com.codingforcookies.betterrecords.src.packets;

import java.util.EnumMap;

import com.codingforcookies.betterrecords.src.betterenums.RecordConnection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	public static EnumMap<Side, FMLEmbeddedChannel> channels;
	
	public static void sendWireConnectionFromClient(RecordConnection connection) {
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeOutbound(new PacketWireConnection(connection));
	}
	
	public static void sendURLWriteFromClient(BlockPos pos, String name, String url, String localName, int size, int color, String author) {
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeOutbound(new PacketURLWriter(pos, name, url, localName, size, color, author));
	}

	public static void sendURLWriteFromClient(BlockPos pos, String name, String url, String localName, int size) {
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeOutbound(new PacketURLWriter(pos, name, url, localName, size));
	}
	
	public static void sendRecordPlayToAllFromServer(BlockPos pos, int dimension, float playRadius, NBTTagCompound nbt, boolean repeat) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeOutbound(new PacketRecordPlayerPlay(pos, playRadius, dimension, nbt, repeat));
	}
	
	public static void sendRecordPlayToAllFromServer(BlockPos pos, int dimension, float playRadius, String name, String url, String local, boolean repeat) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeOutbound(new PacketRecordPlayerPlay(pos, playRadius, dimension, name, url, local, repeat));
	}

	public static void sendSoundStopToAllFromServer(BlockPos pos, int dimension) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeOutbound(new PacketSoundStop(pos, dimension));
	}
	
	public static void sendRadioPlayToAllFromServer(BlockPos pos, int dimension, float playRadius, String local, String url) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeOutbound(new PacketRadioPlay(pos, playRadius, dimension, local, url));
	}
}