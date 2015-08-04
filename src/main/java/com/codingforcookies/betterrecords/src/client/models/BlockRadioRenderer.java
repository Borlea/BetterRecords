package com.codingforcookies.betterrecords.src.client.models;

import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import com.codingforcookies.betterrecords.src.StaticInfo;
import com.codingforcookies.betterrecords.src.betterenums.IRecordWireManipulator;
import com.codingforcookies.betterrecords.src.betterenums.RecordConnection;
import com.codingforcookies.betterrecords.src.items.TileEntityRadio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class BlockRadioRenderer extends TileEntitySpecialRenderer {
	public BlockRadioRenderer() { }
	
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale, int unknown) {
		if(!(te instanceof TileEntityRadio))
			return;
		
		TileEntityRadio tileEntityRadio = (TileEntityRadio)te;

		if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof IRecordWireManipulator) {
			GL11.glPushMatrix();
			{
				GL11.glTranslatef((float)x + .5F, (float)y + .5F, (float)z + .5F);
				
				if(tileEntityRadio.getConnections().size() != 0) {
					GL11.glColor3f(0F, 0F, 0F);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					
					GL11.glLineWidth(2F);
					for(RecordConnection rec : tileEntityRadio.getConnections()) {
						int x1 = -(tileEntityRadio.getPos().getX() - rec.pos2.getX());
						int y1 = -(tileEntityRadio.getPos().getY() - rec.pos2.getY());
						int z1 = -(tileEntityRadio.getPos().getZ() - rec.pos2.getZ());
						GL11.glPushMatrix();
						{
							GL11.glBegin(GL11.GL_LINE_STRIP);
							{
								GL11.glVertex3f(0F, 0F, 0F);
								GL11.glVertex3f(x1, y1, z1);
							}
							GL11.glEnd();
						}
						GL11.glPopMatrix();
					}
					
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glColor3f(1F, 1F, 1F);
				}
				GL11.glScalef(.01F, -.01F, .01F);
				GL11.glRotatef(-Minecraft.getMinecraft().getRenderManager().playerViewY - 180F, 0F, 1F, 0F);
				if(tileEntityRadio.formTreble.size() != 0) {
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glPushMatrix();
					{
						try{
							GL11.glColor3f(0F, 1F, 1F);
							float increment = 25F;
							int waveSize = tileEntityRadio.formTreble.size();
							float oldX = -50F;
							float oldY = -125F;
							float xIndex = -50;
							for(int i = 0; i < waveSize; i += increment){
								if(tileEntityRadio.formTreble.size() < i) break;
								float scaledSample = tileEntityRadio.formTreble.get(i);
								float yIndex = scaledSample - 125F;
								GL11.glBegin(GL11.GL_LINE_STRIP);
								{
									GL11.glVertex3f(oldX, oldY, 0F);
									GL11.glVertex3f(xIndex, yIndex, 0F);
									oldX = xIndex;
									oldY = yIndex;
									xIndex++;
								}
								GL11.glEnd();
							}
							GL11.glColor3f(1F, 1F, 0F);
							waveSize = tileEntityRadio.formBass.size();
							oldX = -50F;
							oldY = -200F;
							xIndex = -50;
							for(int i = 0; i < waveSize; i += increment){
								if(tileEntityRadio.formBass.size() < i) break;
								float scaledSample = tileEntityRadio.formBass.get(i);
								float yIndex = scaledSample - 200F;
								GL11.glBegin(GL11.GL_LINE_STRIP);
								{
									GL11.glVertex3f(oldX, oldY, 0F);
									GL11.glVertex3f(xIndex, yIndex, 0F);
									oldX = xIndex;
									oldY = yIndex;
									xIndex++;
								}
								GL11.glEnd();
							}
						}catch(Exception e){
							System.err.println("Waveform error. This is normal, its due to a desync between the music thread, and the main thread!");
						}
					}
					GL11.glPopMatrix();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glColor3f(1F, 1F, 1F);
				}

	            GL11.glColor3f(1F, 1F, 1F);
				int currentY = tileEntityRadio.wireSystemInfo.size() * -10 - 75;
				FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
				fontRenderer.drawString("Play Radius: " + tileEntityRadio.getSongRadius(), -fontRenderer.getStringWidth("Play Radius: " + tileEntityRadio.getSongRadius()) / 2, currentY, 0xFFFFFF);
				for(Entry<String, Integer> nfo : tileEntityRadio.wireSystemInfo.entrySet()) {
					currentY += 10;
					fontRenderer.drawString(nfo.getValue() + "x " + nfo.getKey(), -fontRenderer.getStringWidth(nfo.getValue() + "x " + nfo.getKey()) / 2, currentY, 0xFFFFFF);
				}
			}
			GL11.glPopMatrix();
		}
		
		GL11.glPushMatrix();
		{
			GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			if(te.getBlockMetadata() == 3 || te.getBlockMetadata() == 4)
				GL11.glRotatef(te.getBlockMetadata() * (90) - 90, 0.0F, 1.0F, 0.0F);
			else if(te.getBlockMetadata() == 5)
				GL11.glRotatef(te.getBlockMetadata() * (90), 0.0F, 1.0F, 0.0F);
			bindTexture(StaticInfo.modelRadioRes);
			StaticInfo.modelRadio.render((Entity)null, tileEntityRadio.openAmount, tileEntityRadio.crystalFloaty, 0F, 0.0F, 0.0F, 0.0625F, tileEntityRadio.crystal);
		}
		GL11.glPopMatrix();
	}
}