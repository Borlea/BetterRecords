package com.codingforcookies.betterrecords.src.gui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.lwjgl.opengl.GL11;

import com.codingforcookies.betterrecords.src.LibrarySong;
import com.codingforcookies.betterrecords.src.StaticInfo;
import com.codingforcookies.betterrecords.src.client.ClientProxy;
import com.codingforcookies.betterrecords.src.items.TileEntityRecordEtcher;
import com.codingforcookies.betterrecords.src.packets.PacketHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiRecordEtcher extends GuiContainer {
	TileEntityRecordEtcher tileEntity;
	GuiTextField nameField;
	GuiTextField urlField;
	String error = "";
	
	long checkURLTime = 0;
	boolean checkedURL = false;
	int etchSize = 0;
	
	int selectedLib = -1;
	int page = 0;
	int maxpage = 0;
	
	private JsonElement root = null;
	private JsonObject rootObj = null;
	
	public GuiRecordEtcher(InventoryPlayer inventoryPlayer, TileEntityRecordEtcher tileEntity) {
		super(new ContainerRecordEtcher(inventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
		xSize = 256;
	}
	
	public void initGui() {
		super.initGui();
		nameField = new GuiTextField(0, this.fontRendererObj, 44, 20, 124, 10);
		urlField = new GuiTextField(1, this.fontRendererObj, 44, 35, 124, 10);
		urlField.setMaxStringLength(256);
		
		if(ClientProxy.defaultLibrary.size() == 0 || (ClientProxy.lastCheckType == 0 || ClientProxy.lastCheckType != (Minecraft.getMinecraft().theWorld.isRemote ? 1 : 2))) {
			System.out.println("Loading default library...");
			ClientProxy.lastCheckType = Minecraft.getMinecraft().theWorld.isRemote ? 1 : 2;
			try {
				HttpURLConnection request = (HttpURLConnection)new URL(ClientProxy.defaultLibraryURL).openConnection();
				request.connect();
				
				if(request.getResponseCode() == 200) {
					JsonParser jp = new JsonParser();
					JsonElement root = jp.parse(new InputStreamReader((InputStream)request.getContent()));
					JsonObject rootobj = root.getAsJsonObject();
					for(Entry<String, JsonElement> entry : rootobj.entrySet()) {
						if(entry.getValue().isJsonObject()) {
							JsonObject obj = entry.getValue().getAsJsonObject();
							ClientProxy.defaultLibrary.add(new LibrarySong(entry.getKey(), obj.get("author").getAsString(), obj.get("name").getAsString(), obj.get("url").getAsString(), Integer.parseInt(obj.get("color").getAsString().replaceFirst("#", ""), 16)));
						}
					}
				}
				
				request.disconnect();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("Loading available encodings...");
			try {
				HttpURLConnection request = (HttpURLConnection)new URL("https://raw.githubusercontent.com/stumblinbear/Versions/master/betterrecords/encodings.txt").openConnection();
				request.connect();
				
				if(request.getResponseCode() == 200) {
					ClientProxy.encodings = new ArrayList<String>();
					
					BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)request.getContent()));
					String line;
					while((line = br.readLine()) != null)
						ClientProxy.encodings.add(line);
					br.close();
				}
				
				request.disconnect();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if(ClientProxy.lastCheckType == 1) {
				System.out.println("Loading local library...");
				
				loadLocalLibrary();
			}
		}
		
		maxpage = (int)Math.ceil(ClientProxy.defaultLibrary.size() / 14);
	}
	
	private void loadLocalLibrary() {
		try {
			if(!ClientProxy.localLibrary.exists()) {
				ClientProxy.localLibrary.createNewFile();
				
				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter(ClientProxy.localLibrary));
					writer.write("{}");
				} finally {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			JsonParser jp = new JsonParser();
			root = jp.parse(new InputStreamReader(new FileInputStream(ClientProxy.localLibrary)));
			rootObj = root.getAsJsonObject();
			
			for(Entry<String, JsonElement> entry : rootObj.entrySet()) {
				if(entry.getValue().isJsonObject()) {
					JsonObject obj = entry.getValue().getAsJsonObject();
					ClientProxy.defaultLibrary.add(0, new LibrarySong(entry.getKey(), obj.get("author").getAsString(), obj.get("name").getAsString(), obj.get("url").getAsString(), Integer.parseInt(obj.get("color").getAsString().replaceFirst("#", ""), 16)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void keyTyped(char par1, int par2) throws IOException {
		checkedURL = false;
		checkURLTime = System.currentTimeMillis() + 2000;
		
		if(nameField.isFocused())
			nameField.textboxKeyTyped(par1, par2);
		else if(urlField.isFocused())
			urlField.textboxKeyTyped(par1, par2);
		else
			super.keyTyped(par1, par2);
	}
	
	protected void mouseClicked(int par1, int par2, int par3) throws IOException {
		super.mouseClicked(par1, par2, par3);
		int x = par1 - (width - xSize) / 2;
		int y = par2 - (height - ySize) / 2;
		
		nameField.mouseClicked(x, y, par3);
		urlField.mouseClicked(x, y, par3);
		
		if(error == "Ready to etch!" && x >= 44 && x <= 75 && y >= 51 && y <= 66) {
			if(selectedLib != -1) {
				LibrarySong sel = ClientProxy.defaultLibrary.get(selectedLib);
				try {
					PacketHandler.sendURLWriteFromClient(tileEntity.getPos(), sel.name, sel.url, sel.local, new URL(sel.url).openConnection().getContentLength() / 1024 / 1024, sel.color, sel.author);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				String superName = FilenameUtils.getName(urlField.getText());
				superName = superName.split("#")[0];
				superName = superName.split("\\?")[0];
				
				String superLocal = nameField.getText().trim();
				
				if(ClientProxy.lastCheckType == 1) {
					boolean exists = false;
					for(LibrarySong sng : ClientProxy.defaultLibrary) {
						if(sng.local.equals(superLocal)) {
							exists = true;
							break;
						}
					}
					
					if(!exists) {
						JsonObject elmnt = new JsonObject();
						elmnt.addProperty("author", Minecraft.getMinecraft().thePlayer.getCommandSenderEntity().getName());
						elmnt.addProperty("name", superName);
						elmnt.addProperty("url", urlField.getText());
						elmnt.addProperty("color", "#FFFFFF");
						if(rootObj == null)
							loadLocalLibrary();
						if(rootObj != null) {
							rootObj.add(superLocal, elmnt);
							
							ClientProxy.defaultLibrary.add(0, new LibrarySong(superLocal, Minecraft.getMinecraft().thePlayer.getCommandSenderEntity().getName(), superName, urlField.getText(), Integer.parseInt("FFFFFF", 16)));
							
							if(!ClientProxy.localLibrary.exists()) {
								if(ClientProxy.localLibrary.getParentFile().mkdirs())
									try {
										ClientProxy.localLibrary.createNewFile();
									} catch (IOException e) {
										e.printStackTrace();
									}
							}
							
							BufferedWriter writer = null;
							try {
								writer = new BufferedWriter(new FileWriter(ClientProxy.localLibrary));
								writer.write(rootObj.toString());
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								try {
									writer.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				
				PacketHandler.sendURLWriteFromClient(tileEntity.getPos(), superName, urlField.getText(), superLocal, etchSize);
			}
		}
		
		if(x >= 175 && x <= 195 && y >= 150 && y <= 159) {
			if(page > 0)
				page--;
		}else if(x >= 229 && x <= 249 && y >= 150 && y <= 159) {
			if(page < maxpage)
				page++;
		}
		
		for(int i = 0; i < 14; i++) {
			int offsetI = page * 14 + i;
			if(offsetI > ClientProxy.defaultLibrary.size() - 1)
				break;
			if(x >= 178 && x <= 245 && y >= 9 + i * 10 && y <= 17 + i * 10) {
				if(selectedLib == offsetI) {
					selectedLib = -1;
					nameField.setEnabled(true);
					urlField.setEnabled(true);
				}else{
					selectedLib = offsetI;
					nameField.setEnabled(false);
					urlField.setEnabled(false);
				}
			}
		}
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRendererObj.drawString("Record Etcher", 8, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		
		fontRendererObj.drawString("Name: ", 10, 21, 4210752);
		fontRendererObj.drawString("URL: ", 10, 36, 4210752);
		
		int mx = par1 - (width - xSize) / 2;
		int my = par2 - (height - ySize) / 2;
		fontRendererObj.drawStringWithShadow("Etch", 50, 53, (error == "Ready to etch!" ? (mx >= 44 && mx <= 75 && my >= 51 && my <= 66 ? 0xFFFF55 : 0xFFFFFF) : 0x555555));

		GL11.glPushMatrix();
		{
			GL11.glScalef(.5F, .5F, 1F);
			fontRendererObj.drawString(error, 332 - fontRendererObj.getStringWidth(error), 109, (error == "Ready to etch!" ? 0x229922 : 0x992222));
		}
		GL11.glPopMatrix();
		
		nameField.drawTextBox();
		urlField.drawTextBox();
		
		if(tileEntity.record == null)
			error = "No record to etch";
		else if(tileEntity.record.hasTagCompound() && tileEntity.record.getTagCompound().hasKey("url"))
			error = "Not a blank record";
		else if(selectedLib != -1)
			error = "Ready to etch!";
		else if(nameField.getText().length() == 0)
			error = "Please insert a name";
		else if(nameField.getText().length() < 3)
			error = "Name is not long enough";
		else if(urlField.getText().length() == 0)
			error = "Please insert a URL";
		else if(!checkedURL) {
			error = "Validating...";
			
			if(checkURLTime < System.currentTimeMillis()) {
				checkURLTime = 0;
				
				URL url;
				try {
					url = new URL(urlField.getText().replace(" ", "%20"));
					URLConnection connection = url.openConnection();
					
					if(connection instanceof HttpURLConnection) {
						((HttpURLConnection)connection).setRequestMethod("HEAD");
						connection.connect();
						if(((HttpURLConnection)connection).getResponseCode() == 200) {
							if(connection.getContentLength() / 1024 / 1024 > (ClientProxy.downloadMax != 100 ? ClientProxy.downloadMax : 102400)) {
								error = "Max file size is " + ClientProxy.downloadMax + "mb";
							}
						}else
							error = "URL unavailable (Does it exist?)";
					}else{
						if(Minecraft.getMinecraft().theWorld.isRemote) {
							connection.connect();
							if(connection.getContentLength() == 0)
								error = "Invalid file";
						}else
							error = "Local files are singleplayer only!";
					}
					
					if(error != "") {
						etchSize = connection.getContentLength() / 1024 / 1024;
						String contentType = connection.getContentType();
						if(ClientProxy.encodings.contains(contentType))
							error = "Ready to etch!";
						else
							error = "The content type " + contentType + " is not supported!";
					}
				} catch (MalformedURLException e) {
					error = "Invalid URL";
				} catch (IOException e) {
					error = "IO Exception";
				}
				checkedURL = true;
			}
		}
		
		fontRendererObj.drawString((page + 1) + "/" + (maxpage + 1), 195 + fontRendererObj.getStringWidth((page + 1) + "/" + (maxpage + 1)) /2, 151, 4210752);
		
		for(int i = 0; i < 14; i++) {
			int offsetI = page * 14 + i;
			if(offsetI > ClientProxy.defaultLibrary.size() - 1)
				break;
			if(mx >= 178 && mx <= 245 && my >= 9 + i * 10 && my <= 17 + i * 10) {
				GL11.glPushMatrix();
				{
					List<String> txt = new ArrayList<String>();
					txt.add(ClientProxy.defaultLibrary.get(offsetI).local);
					txt.add("\2477By: " + ClientProxy.defaultLibrary.get(offsetI).author);
					drawHoveringText(txt, mx, my, fontRendererObj);
				}
				GL11.glPopMatrix();
			}
		}
	}
	
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		mc.renderEngine.bindTexture(StaticInfo.GUIRecordEtcher);
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		drawTexturedModalRect(x + 44, y + 51, 0, (error == "Ready to etch!" ? 166 : 178), 33, 12);
		
		int mx = par2 - x;
		int my = par3 - y;

		for(int i = 0; i < 14; i++) {
			int offsetI = page * 14 + i;
			if(offsetI > ClientProxy.defaultLibrary.size() - 1)
				break;
			GL11.glPushMatrix();
			{
				GL11.glTranslatef(x, y, 0F);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				RenderHelper.disableStandardItemLighting();
				if(selectedLib == offsetI) {
					GL11.glBegin(GL11.GL_QUADS);
					{
						GL11.glColor3f(.7F, .7F, .7F);
						GL11.glVertex2f(248, 8 + i * 10);
						GL11.glVertex2f(176, 8 + i * 10);
						GL11.glVertex2f(176, 18 + i * 10);
						GL11.glVertex2f(248, 18 + i * 10);
					}
					GL11.glEnd();
				}
				GL11.glBegin(GL11.GL_QUADS);
				{
					Color color = new Color(ClientProxy.defaultLibrary.get(offsetI).color);
					GL11.glColor3f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
					GL11.glVertex2f(185, 9 + i * 10);
					GL11.glVertex2f(178, 9 + i * 10);
					GL11.glVertex2f(178, 17 + i * 10);
					GL11.glVertex2f(185, 17 + i * 10);
				}
				GL11.glEnd();
				GL11.glEnable(GL11.GL_TEXTURE_2D);

				GL11.glPushMatrix();
				{
					GL11.glScalef(.5F, .5F, 1F);
					fontRendererObj.drawString(ClientProxy.defaultLibrary.get(offsetI).local, 375, 22 + i * 20, mx >= 178 && mx <= 245 && my >= 9 + i * 10 && my <= 17 + i * 10 ? 0xFFFF00 : 4210752);
				}
				GL11.glPopMatrix();
			}
			GL11.glPopMatrix();
		}
	}
}