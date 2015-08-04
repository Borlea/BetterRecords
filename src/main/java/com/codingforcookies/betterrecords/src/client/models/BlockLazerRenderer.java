package com.codingforcookies.betterrecords.src.client.models;

import org.lwjgl.opengl.GL11;

import com.codingforcookies.betterrecords.src.StaticInfo;
import com.codingforcookies.betterrecords.src.client.ClientProxy;
import com.codingforcookies.betterrecords.src.items.TileEntityLazer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockLazerRenderer extends TileEntitySpecialRenderer {
	public BlockLazerRenderer() { }

	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale, int unknown) {
		if(!(te instanceof TileEntityLazer))
			return;
		
		TileEntityLazer tileEntityLazer = (TileEntityLazer)te;
		
		GL11.glPushMatrix();
		{
			GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			
			bindTexture(StaticInfo.modelLazerRes);
			StaticInfo.modelLazer.render((Entity)null, tileEntityLazer.bass != 0 ? 1F : 0F, tileEntityLazer.yaw, tileEntityLazer.pitch, 0.0F, 0.0F, 0.0625F);
			
			GL11.glRotatef(-180F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(0.0F, -.926F, 0.0F);
			
			if(tileEntityLazer.bass != 0 && ClientProxy.flashyMode > 0) {
				GL11.glPushMatrix();
				{
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_CULL_FACE);
					
					GL11.glRotatef(-tileEntityLazer.yaw + 180F, 0F, 1F, 0F);
					GL11.glRotatef(-tileEntityLazer.pitch + 90F, 1F, 0F, 0F);
					
					float length = 10F;
					
					/*MovingObjectPosition ray = rayTrace(tileEntityLazer.getWorld(), tileEntityLazer.getPos(), pitch, yaw, length);
					if(ray != null){
						tileEntityLazer.getWorld().setBlockState(ray.getBlockPos(), Blocks.gold_block.getDefaultState());
					}*/
					/*
					float yaw = tileEntityLazer.yaw;
					float pitch = tileEntityLazer.pitch;
					float startX = tileEntityLazer.getPos().getX() - 0.5F;
					float startY = tileEntityLazer.getPos().getY();
					float startZ = tileEntityLazer.getPos().getZ() + .5F;
					float stopX = (float)(startX + length * Math.cos(yaw) * Math.cos(pitch));
					float stopY = (float)(startY + length * Math.sin(yaw));
					float stopZ = (float)(startZ + length * Math.cos(yaw) * Math.sin(pitch));
					
					float distance = (float)Math.round(Math.sqrt(Math.pow(stopX - startX, 2) + Math.pow(stopY - startY, 2) + Math.pow(stopZ - startZ, 2)));
					
					for(float check = 1F; check < distance; check += .1F) {
						length = check;
						int posX = (int)(startX + check * Math.cos(yaw) * Math.cos(pitch));
						int posY = (int)(startY + (check - 2));//(int)(startY + check * Math.sin(yaw)) + 1;
						int posZ = (int)(startZ + check * Math.cos(yaw) * Math.sin(pitch));
						Block block = tileEntityLazer.getWorld().getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
						//tileEntityLazer.getWorld().setBlockState(new BlockPos(posX, posY, posZ), Blocks.gold_block.getDefaultState());
						if(block instanceof BlockAir) {
							//System.out.println(block.getLocalizedName());
							tileEntityLazer.getWorld().setBlockState(new BlockPos(posX, posY, posZ), Blocks.gold_block.getDefaultState());
						}
					}*/

					float width = tileEntityLazer.bass / 400F;
					GL11.glBegin(GL11.GL_QUADS);
					{
						GL11.glColor4f(tileEntityLazer.r, tileEntityLazer.g, tileEntityLazer.b, (ClientProxy.flashyMode == 1 ? .3F : .8F));
						
						GL11.glVertex3f(width, 0F, -width);
						GL11.glVertex3f(-width, 0F, -width);
						GL11.glVertex3f(-width, length, -width);
						GL11.glVertex3f(width, length, -width);

						GL11.glVertex3f(-width, 0F, width);
						GL11.glVertex3f(width, 0F, width);
						GL11.glVertex3f(width, length, width);
						GL11.glVertex3f(-width, length, width);

						GL11.glVertex3f(width, 0F, width);
						GL11.glVertex3f(width, 0F, -width);
						GL11.glVertex3f(width, length, -width);
						GL11.glVertex3f(width, length, width);

						GL11.glVertex3f(-width, 0F, -width);
						GL11.glVertex3f(-width, 0F, width);
						GL11.glVertex3f(-width, length, width);
						GL11.glVertex3f(-width, length, -width);
					}
					GL11.glEnd();
					
					GL11.glEnable(GL11.GL_CULL_FACE);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}
				GL11.glPopMatrix();
				
				GL11.glColor4f(1F, 1F, 1F, 1F);
			}
		}
		GL11.glPopMatrix();
	}
	
	public MovingObjectPosition rayTrace(World world, BlockPos pos, float pitch, float yaw, double distance) {
        Vec3 vec3 = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        Vec3 lookVec = getVectorForRotation(-pitch, -yaw);
        Vec3 addedVector = vec3.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
        return world.rayTraceBlocks(vec3, addedVector, false, false, true);
    }
	
	protected final Vec3 getVectorForRotation(float pitch, float yaw){
        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f3 * f4), (double)f5, (double)(f2 * f4));
    }
	
}