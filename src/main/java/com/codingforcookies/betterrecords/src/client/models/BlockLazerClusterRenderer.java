package com.codingforcookies.betterrecords.src.client.models;

import org.lwjgl.opengl.GL11;

import com.codingforcookies.betterrecords.src.StaticInfo;
import com.codingforcookies.betterrecords.src.client.ClientProxy;
import com.codingforcookies.betterrecords.src.items.TileEntityLazerCluster;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class BlockLazerClusterRenderer extends TileEntitySpecialRenderer {
	public BlockLazerClusterRenderer() { }

	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale, int unknown) {
		if(!(te instanceof TileEntityLazerCluster))
			return;

		TileEntityLazerCluster tileEntityLazerCluster = (TileEntityLazerCluster)te;

		GL11.glPushMatrix();
		{
			GL11.glDisable(GL11.GL_LIGHTING);//Makes it all white ^_^
			if(te.getBlockMetadata() == 0) {
				GL11.glTranslatef((float) x + .5F, (float) y - .5F, (float) z + .5F);
				GL11.glRotatef(360, 0, 0, 1);
			}else if(te.getBlockMetadata() == 1) {
				GL11.glTranslatef((float) x + 1.5F, (float) y + .5F, (float) z + .5F);
				GL11.glRotatef(90, 0, 0, 1);
			}else if(te.getBlockMetadata() == 2) {
				GL11.glTranslatef((float) x - 0.5F, (float) y + .5F, (float) z + .5F);
				GL11.glRotatef(270, 0, 0, 1);
			}else if(te.getBlockMetadata() == 3) {
				GL11.glTranslatef((float) x + .5F, (float) y + .5F, (float) z + 1.5F);
				GL11.glRotatef(-90, 5, 0, 0);
			}else if(te.getBlockMetadata() == 4) {
				GL11.glTranslatef((float) x + .5F, (float) y + .5F, (float) z - .5F);
				GL11.glRotatef(90, 5, 0, 0);
			}else{
				GL11.glTranslatef((float) x + .5F, (float) y + 1.5F, (float) z + .5F);
				GL11.glRotatef(180, 0, 0, 1);
			}
			if(tileEntityLazerCluster.r != 0.0F && tileEntityLazerCluster.g != 0.0F && tileEntityLazerCluster.b != 0.0F) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4f(tileEntityLazerCluster.r, tileEntityLazerCluster.g, tileEntityLazerCluster.b, (ClientProxy.flashyMode == 1 ? .2F : .4F));
			}
			bindTexture(StaticInfo.modelLazerClusterRes);
			StaticInfo.modelLazerCluster.render((Entity)null, 0F, 0F, 0F, 0.0F, 0.0F, 0.0625F);
			if(tileEntityLazerCluster.r != 0.0F && tileEntityLazerCluster.g != 0.0F && tileEntityLazerCluster.b != 0.0F) {
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glEnable(GL11.GL_LIGHTING);
			
			GL11.glTranslatef(0.0F, 1.0F, 0.0F);
			if(tileEntityLazerCluster.bass != 0 && ClientProxy.flashyMode > 0) {
				GL11.glPushMatrix();
				{
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_BLEND);
					RenderHelper.disableStandardItemLighting();
					
					GL11.glLineWidth(tileEntityLazerCluster.bass / 2);
					
					for(float pitch = 0F; pitch < 9F; pitch += 1F) {
						GL11.glRotatef(200F / 3, 0F, 1F, 0F);
						for(float yaw = 0F; yaw < 18F; yaw += 1F) {
							GL11.glRotatef(200F / 9, 0F, 0F, 1F);
							GL11.glBegin(GL11.GL_LINE_STRIP);
							{
								GL11.glColor4f(tileEntityLazerCluster.r, tileEntityLazerCluster.g, tileEntityLazerCluster.b, (ClientProxy.flashyMode == 1 ? .2F : .4F));
								GL11.glVertex2f(0F, 0F);
								
								float xx = (float)Math.cos(pitch * (Math.PI / 180)) * 20F;
								float yy = (float)Math.sin(yaw * (Math.PI / 180)) * 20F;
								
								GL11.glVertex2f(xx, yy);
							}
							GL11.glEnd();
						}
					}
					
					GL11.glLineWidth(1F);
					
					RenderHelper.enableStandardItemLighting();
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_LIGHTING);
				}
				GL11.glPopMatrix();
				
				GL11.glColor4f(1F, 1F, 1F, 1F);
			}
		}
		GL11.glPopMatrix();
	}
}