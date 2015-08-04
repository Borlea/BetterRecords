package com.codingforcookies.betterrecords.src;

import com.codingforcookies.betterrecords.src.client.models.ModelFrequencyTuner;
import com.codingforcookies.betterrecords.src.client.models.ModelLGSpeaker;
import com.codingforcookies.betterrecords.src.client.models.ModelLazer;
import com.codingforcookies.betterrecords.src.client.models.ModelLazerCluster;
import com.codingforcookies.betterrecords.src.client.models.ModelMDSpeaker;
import com.codingforcookies.betterrecords.src.client.models.ModelRadio;
import com.codingforcookies.betterrecords.src.client.models.ModelRecordEtcher;
import com.codingforcookies.betterrecords.src.client.models.ModelRecordPlayer;
import com.codingforcookies.betterrecords.src.client.models.ModelSMSpeaker;
import com.codingforcookies.betterrecords.src.client.models.ModelStrobeLight;

import net.minecraft.util.ResourceLocation;

public class StaticInfo {
	public static final ResourceLocation GUIRecordEtcher = new ResourceLocation(BetterRecords.ID, "textures/gui/recordetcher.png");
	public static final ResourceLocation GUIFrequencyTuner = new ResourceLocation(BetterRecords.ID, "textures/gui/frequencytuner.png");
	
	public static final ModelRecordEtcher modelRecordEtcher = new ModelRecordEtcher();
	public static final ResourceLocation modelRecordEtcherRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/recordetcher.png");
	public static final ModelRecordPlayer modelRecordPlayer = new ModelRecordPlayer();
	public static final ResourceLocation modelRecordPlayerRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/recordplayer.png");
	public static final ModelFrequencyTuner modelFrequencyTuner = new ModelFrequencyTuner();
	public static final ResourceLocation modelFrequencyTunerRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/frequencytuner.png");
	public static final ModelRadio modelRadio = new ModelRadio();
	public static final ResourceLocation modelRadioRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/radio.png");
	
	public static final ModelSMSpeaker modelSMSpeaker = new ModelSMSpeaker();
	public static final ResourceLocation modelSMSpeakerRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/smspeaker.png");
	public static final ModelMDSpeaker modelMDSpeaker = new ModelMDSpeaker();
	public static final ResourceLocation modelMDSpeakerRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/mdspeaker.png");
	public static final ModelLGSpeaker modelLGSpeaker = new ModelLGSpeaker();
	public static final ResourceLocation modelLGSpeakerRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/lgspeaker.png");

	public static final ModelStrobeLight modelStrobeLight = new ModelStrobeLight();
	public static ResourceLocation modelStrobeLightRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/strobelight.png");
	public static final ModelLazer modelLazer = new ModelLazer();
	public static ResourceLocation modelLazerRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/lazer.png");
	public static final ModelLazerCluster modelLazerCluster = new ModelLazerCluster();
	public static ResourceLocation modelLazerClusterRes = new ResourceLocation(BetterRecords.ID, "textures/blocks/lazercluster.png");
}