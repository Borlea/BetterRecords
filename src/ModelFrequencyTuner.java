// Date: 10/7/2014 7:20:15 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package net.minecraft.src;

public class ModelFrequencyTuner extends ModelBase
{
  //fields
    ModelRenderer Base;
    ModelRenderer Platform;
    ModelRenderer Stand;
    ModelRenderer TunerBridge;
    ModelRenderer TunerWeight;
    ModelRenderer Tuner;
  
  public ModelFrequencyTuner()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      Base = new ModelRenderer(this, 0, 0);
      Base.addBox(-5F, 0F, -6F, 10, 9, 12);
      Base.setRotationPoint(0F, 15F, 0F);
      Base.setTextureSize(64, 32);
      Base.mirror = true;
      setRotation(Base, 0F, 0F, 0F);
      Platform = new ModelRenderer(this, 0, 21);
      Platform.addBox(-4F, -1F, -5F, 8, 1, 8);
      Platform.setRotationPoint(0F, 15F, 0F);
      Platform.setTextureSize(64, 32);
      Platform.mirror = true;
      setRotation(Platform, 0F, 0F, 0F);
      Stand = new ModelRenderer(this, 44, 0);
      Stand.addBox(-0.5F, -2.5F, 4F, 1, 3, 1);
      Stand.setRotationPoint(0F, 15F, 0F);
      Stand.setTextureSize(64, 32);
      Stand.mirror = true;
      setRotation(Stand, 0F, 0F, 0F);
      TunerBridge = new ModelRenderer(this, 44, 4);
      TunerBridge.addBox(-0.5F, -1F, 4F, 1, 6, 1);
      TunerBridge.setRotationPoint(0F, 15F, 0F);
      TunerBridge.setTextureSize(64, 32);
      TunerBridge.mirror = true;
      setRotation(TunerBridge, 1.041001F, 0F, 0F);
      TunerWeight = new ModelRenderer(this, 48, 0);
      TunerWeight.addBox(-1.5F, -2F, 5.5F, 3, 1, 2);
      TunerWeight.setRotationPoint(0F, 15F, 0F);
      TunerWeight.setTextureSize(64, 32);
      TunerWeight.mirror = true;
      setRotation(TunerWeight, 0F, 0F, 0F);
      Tuner = new ModelRenderer(this, 48, 3);
      Tuner.addBox(-0.5F, -2F, 3F, 1, 1, 2);
      Tuner.setRotationPoint(0F, 15F, 0F);
      Tuner.setTextureSize(64, 32);
      Tuner.mirror = true;
      setRotation(Tuner, 1.029744F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    Base.render(f5);
    Platform.render(f5);
    Stand.render(f5);
    TunerBridge.render(f5);
    TunerWeight.render(f5);
    Tuner.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5);
  }

}
