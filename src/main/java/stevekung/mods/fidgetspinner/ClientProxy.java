package stevekung.mods.fidgetspinner;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.item.Item;

public class ClientProxy extends ServerProxy
{
    @Override
    public void spawnParticle(double x, double y, double z, double motionX, double motionY, double motionZ, Item item, int meta)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.getRenderViewEntity() != null && mc.effectRenderer != null && mc.world != null)
        {
            int i = mc.gameSettings.particleSetting;
            double d6 = mc.getRenderViewEntity().posX - x;
            double d7 = mc.getRenderViewEntity().posY - y;
            double d8 = mc.getRenderViewEntity().posZ - z;
            double d9 = 16.0D;

            if (i == 1 && mc.world.rand.nextInt(3) == 0)
            {
                i = 2;
            }
            if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9)
            {
                return;
            }
            else if (i > 1)
            {
                return;
            }
            Particle particle = new ParticleBreakingColored(mc.world, x, y, z, motionX, motionY, motionZ, item, meta);
            mc.effectRenderer.addEffect(particle);
        }
    }
}