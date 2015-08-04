package com.codingforcookies.betterrecords.src;

import org.apache.commons.lang3.text.WordUtils;

import com.codingforcookies.betterrecords.src.client.BetterCreativeTab;
import com.codingforcookies.betterrecords.src.gui.GuiHandler;
import com.codingforcookies.betterrecords.src.items.BlockFrequencyTuner;
import com.codingforcookies.betterrecords.src.items.BlockLazer;
import com.codingforcookies.betterrecords.src.items.BlockLazerCluster;
import com.codingforcookies.betterrecords.src.items.BlockRadio;
import com.codingforcookies.betterrecords.src.items.BlockRecordEtcher;
import com.codingforcookies.betterrecords.src.items.BlockRecordPlayer;
import com.codingforcookies.betterrecords.src.items.BlockRecordSpeaker;
import com.codingforcookies.betterrecords.src.items.BlockStrobeLight;
import com.codingforcookies.betterrecords.src.items.ItemFreqCrystal;
import com.codingforcookies.betterrecords.src.items.ItemRecordWire;
import com.codingforcookies.betterrecords.src.items.ItemRecordWireCutter;
import com.codingforcookies.betterrecords.src.items.ItemURLMultiRecord;
import com.codingforcookies.betterrecords.src.items.ItemURLRecord;
import com.codingforcookies.betterrecords.src.items.TileEntityFrequencyTuner;
import com.codingforcookies.betterrecords.src.items.TileEntityLazer;
import com.codingforcookies.betterrecords.src.items.TileEntityLazerCluster;
import com.codingforcookies.betterrecords.src.items.TileEntityRadio;
import com.codingforcookies.betterrecords.src.items.TileEntityRecordEtcher;
import com.codingforcookies.betterrecords.src.items.TileEntityRecordPlayer;
import com.codingforcookies.betterrecords.src.items.TileEntityRecordSpeaker;
import com.codingforcookies.betterrecords.src.items.TileEntityStrobeLight;
import com.codingforcookies.betterrecords.src.packets.ChannelHandler;
import com.codingforcookies.betterrecords.src.packets.PacketHandler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = BetterRecords.ID, version = "@VERSION@", useMetadata = true, name = "Better Records",
acceptableRemoteVersions = "@CHANGE_VERSION@", acceptedMinecraftVersions = "@MC_VERSION@", acceptableSaveVersions = "@CHANGE_VERSION@")
public class BetterRecords {
	public static final String ID = "betterrecords";
	public static final String VERSION = "@VERSION@";
	
	@Instance(value = ID)
	public static BetterRecords instance;
	
	@SidedProxy(clientSide = "com.codingforcookies.betterrecords.src.client.ClientProxy", serverSide = "com.codingforcookies.betterrecords.src.CommonProxy")
	public static CommonProxy proxy;
	
	public static final BetterCreativeTab recordsTab = new BetterCreativeTab();
	
	public static final ItemURLRecord itemURLRecord = (ItemURLRecord)new ItemURLRecord().setUnlocalizedName("urlrecord").setCreativeTab(recordsTab);
	public static final ItemURLMultiRecord itemURLMultiRecord = (ItemURLMultiRecord)new ItemURLMultiRecord().setUnlocalizedName("urlmultirecord").setCreativeTab(recordsTab);
	public static final ItemFreqCrystal itemFreqCrystal = (ItemFreqCrystal)new ItemFreqCrystal().setUnlocalizedName("freqcrystal").setCreativeTab(recordsTab);
	
	
	public static final ItemRecordWire itemRecordWire = (ItemRecordWire)new ItemRecordWire().setUnlocalizedName("recordwire").setCreativeTab(recordsTab);
	public static final ItemRecordWireCutter itemRecordCutters = (ItemRecordWireCutter)new ItemRecordWireCutter().setUnlocalizedName("recordwirecutters").setCreativeTab(recordsTab);
	
	
	
	public static final BlockRecordEtcher blockRecordEtcher = (BlockRecordEtcher)new BlockRecordEtcher().setUnlocalizedName("recordetcher").setHardness(1.5F).setResistance(5.5F).setCreativeTab(recordsTab);
	public static final BlockRecordPlayer blockRecordPlayer = (BlockRecordPlayer)new BlockRecordPlayer().setUnlocalizedName("recordplayer").setHardness(1F).setResistance(5F).setCreativeTab(recordsTab);
	public static final BlockFrequencyTuner blockFrequencyTuner = (BlockFrequencyTuner)new BlockFrequencyTuner().setUnlocalizedName("frequencytuner").setHardness(1.5F).setResistance(5.5F).setCreativeTab(recordsTab);
	public static final BlockRadio blockRadio = (BlockRadio)new BlockRadio().setUnlocalizedName("shoutcastradio").setHardness(2F).setResistance(6.3F).setCreativeTab(recordsTab);
	
	public static final BlockRecordSpeaker blockSMSpeaker = (BlockRecordSpeaker)new BlockRecordSpeaker(0).setUnlocalizedName("recordspeaker.sm").setHardness(2F).setResistance(7.5F).setCreativeTab(recordsTab);
	public static final BlockRecordSpeaker blockMDSpeaker = (BlockRecordSpeaker)new BlockRecordSpeaker(1).setUnlocalizedName("recordspeaker.md").setHardness(3F).setResistance(8F).setCreativeTab(recordsTab);
	public static final BlockRecordSpeaker blockLGSpeaker = (BlockRecordSpeaker)new BlockRecordSpeaker(2).setUnlocalizedName("recordspeaker.lg").setHardness(4F).setResistance(9.5F).setCreativeTab(recordsTab);
	
	public static final BlockStrobeLight blockStrobeLight = (BlockStrobeLight)new BlockStrobeLight().setUnlocalizedName("strobelight").setHardness(2.75F).setResistance(4F).setCreativeTab(recordsTab);
	public static final BlockLazer blockLazer = (BlockLazer)new BlockLazer().setUnlocalizedName("lazer").setHardness(3.2F).setResistance(4.3F).setCreativeTab(recordsTab);
	public static final BlockLazerCluster blockLazerCluster = (BlockLazerCluster)new BlockLazerCluster().setUnlocalizedName("lazercluster").setHardness(4.8F).setResistance(4.8F).setCreativeTab(recordsTab);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		/*glfwSetDropCallback(window, new GLFWDropCallback() {
			@Override
			public void invoke(long window, int count, long names) {
				printEvent("drop %d file%s", window, count, count == 1 ? "" : "s");

				dropCallbackNamesApply(count, names, new DropConsumerString() {
					@Override
					public void accept(int index, String name) {
						System.out.format("\t%d: %s%n", index + 1, name);
					}
				});
			}
		});*/
		
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		PacketHandler.channels = NetworkRegistry.INSTANCE.newChannel("BetterRecords", new ChannelHandler());
		GameRegistry.registerItem(itemURLRecord, "urlrecord");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemURLRecord, 0, new ModelResourceLocation(ID + ":urlrecord", "inventory"));
		GameRegistry.registerItem(itemURLMultiRecord, "urlmultirecord");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemURLMultiRecord, 0, new ModelResourceLocation(ID + ":urlmultirecord", "inventory"));
		GameRegistry.registerItem(itemFreqCrystal, "freqcrystal");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemFreqCrystal, 0, new ModelResourceLocation(ID + ":freqcrystal", "inventory"));
		GameRegistry.registerItem(itemRecordWire, "recordwire");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemRecordWire, 0, new ModelResourceLocation(ID + ":recordwire", "inventory"));
		GameRegistry.registerItem(itemRecordCutters, "recordwirecutters");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemRecordCutters, 0, new ModelResourceLocation(ID + ":recordwirecutters", "inventory"));
		
		GameRegistry.registerBlock(blockRecordEtcher, "recordetcher");
		registerRender(blockRecordEtcher);
		GameRegistry.registerBlock(blockRecordPlayer, "recordplayer");
		registerRender(blockRecordPlayer);
		GameRegistry.registerBlock(blockFrequencyTuner, "frequencytuner");
		registerRender(blockFrequencyTuner);
		GameRegistry.registerBlock(blockRadio, "shoutcastradio");
		registerRender(blockRadio);
		GameRegistry.registerBlock(blockSMSpeaker, "recordspeaker.sm");
		registerRender(blockSMSpeaker);
		GameRegistry.registerBlock(blockMDSpeaker, "recordspeaker.md");
		registerRender(blockMDSpeaker);
		GameRegistry.registerBlock(blockLGSpeaker, "recordspeaker.lg");
		registerRender(blockLGSpeaker);
		GameRegistry.registerBlock(blockStrobeLight, "strobelight");
		registerRender(blockStrobeLight);
		GameRegistry.registerBlock(blockLazer, "lazer");
		registerRender(blockLazer);
		GameRegistry.registerBlock(blockLazerCluster, "lazercluster");
		registerRender(blockLazerCluster);
		
		GameRegistry.registerTileEntity(TileEntityRecordEtcher.class, "recordetcher");
		GameRegistry.registerTileEntity(TileEntityRecordPlayer.class, "recordplayer");
		GameRegistry.registerTileEntity(TileEntityFrequencyTuner.class, "frequencytuner");
		GameRegistry.registerTileEntity(TileEntityRadio.class, "shoutcastradio");
		GameRegistry.registerTileEntity(TileEntityRecordSpeaker.class, "recordspeaker");
		GameRegistry.registerTileEntity(TileEntityStrobeLight.class, "strobelight");
		GameRegistry.registerTileEntity(TileEntityLazer.class, "lazer");
		GameRegistry.registerTileEntity(TileEntityLazerCluster.class, "lazercluster");
		
		GameRegistry.addRecipe(new RecipeRecord());
		GameRegistry.addRecipe(new RecipeMultiRecord());
		GameRegistry.addRecipe(new RecipeRecordRepeatable());
		GameRegistry.addRecipe(new RecipeColoredRecord());
		
		GameRegistry.addShapedRecipe(new ItemStack(itemFreqCrystal), "RQR", "QDQ", "RQR", 'R', Items.redstone, 'Q', Items.quartz, 'D', Items.diamond);
		GameRegistry.addRecipe(new RecipeColoredFreqCrystal());
		
		GameRegistry.addShapedRecipe(new ItemStack(itemRecordWire, 4), "WWW", "III", "WWW", 'I', Items.iron_ingot, 'W', new ItemStack(Blocks.wool, 1, 15));
		GameRegistry.addShapedRecipe(new ItemStack(itemRecordWire, 4), "WIW", "WIW", "WIW", 'I', Items.iron_ingot, 'W', new ItemStack(Blocks.wool, 1, 15));
		GameRegistry.addShapedRecipe(new ItemStack(itemRecordCutters), "I I", " I ", "WIW", 'I', Items.iron_ingot, 'W', new ItemStack(Blocks.wool, 1, 15));
		
		GameRegistry.addShapedRecipe(new ItemStack(blockRecordEtcher), "HIH", "PQP", "PPP", 'H', Blocks.wooden_slab, 'I', Items.iron_ingot, 'P', Blocks.planks, 'Q', Items.quartz);
		GameRegistry.addShapedRecipe(new ItemStack(blockRecordPlayer), "GGG", "PDP", "PPP", 'G', Blocks.glass_pane, 'P', Blocks.planks, 'D', Blocks.diamond_block);
		GameRegistry.addShapedRecipe(new ItemStack(blockFrequencyTuner), "SHH", "PQP", "PIP", 'H', Blocks.wooden_slab, 'I', Items.iron_ingot, 'S', Items.stick, 'P', Blocks.planks, 'Q', itemFreqCrystal);
		GameRegistry.addShapedRecipe(new ItemStack(blockRadio), "HIH", "PQP", "PHP", 'H', Blocks.wooden_slab, 'I', Items.iron_ingot, 'P', Blocks.planks, 'Q', itemFreqCrystal);
		
		GameRegistry.addShapedRecipe(new ItemStack(blockSMSpeaker), "LLW", "QDW", "LLW", 'L', Blocks.log, 'W', new ItemStack(Blocks.wool, 1, 15), 'D', Items.diamond, 'Q', Items.quartz);
		GameRegistry.addShapedRecipe(new ItemStack(blockMDSpeaker), "LLW", "ESW", "LLW", 'L', Blocks.log, 'W', new ItemStack(Blocks.wool, 1, 15), 'S', blockSMSpeaker, 'E', Items.ender_eye);
		GameRegistry.addShapedRecipe(new ItemStack(blockLGSpeaker), "LLW", "CMW", "LLW", 'L', Blocks.log, 'W', new ItemStack(Blocks.wool, 1, 15), 'M', blockMDSpeaker, 'C', Items.comparator);
		
		GameRegistry.addShapedRecipe(new ItemStack(blockStrobeLight), "GGG", "GRG", "CTC", 'G', Blocks.glass, 'C', Items.comparator, 'R', Blocks.redstone_lamp, 'T', Blocks.redstone_torch);
		GameRegistry.addShapedRecipe(new ItemStack(blockLazer), "LLL", "LQG", "HLH", 'L', Blocks.log, 'H', Blocks.wooden_slab, 'G', Blocks.glass, 'Q', Items.quartz);
		GameRegistry.addShapedRecipe(new ItemStack(blockLazerCluster), "LLL", "LRL", "LLL", 'L', blockLazer, 'R', Items.redstone);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		
	}

	public static String[] getWordWrappedString(int maxWidth, String string) {
		return WordUtils.wrap(string, maxWidth, "\n", false).replace("\\n", "\n").split("\n");
	}
	
	public static void registerRender(Block block){
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(ID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}