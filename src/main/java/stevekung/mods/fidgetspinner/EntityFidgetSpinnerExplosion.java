package stevekung.mods.fidgetspinner;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFidgetSpinnerExplosion extends EntityFidgetSpinner
{
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityFidgetSpinnerExplosion.class, DataSerializers.VARINT);

    public EntityFidgetSpinnerExplosion(World world)
    {
        super(world);
    }

    public EntityFidgetSpinnerExplosion(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }

    public EntityFidgetSpinnerExplosion(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(EntityFidgetSpinnerExplosion.TYPE, 0);
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 5);
        }
        for (int i = 0; i < 20; i++)
        {
            FidgetSpinnerMod.PROXY.spawnParticle(this.posX, this.posY, this.posZ, this.rand.nextGaussian() * 0.05D, this.rand.nextDouble() * 0.1D, this.rand.nextGaussian() * 0.05D, FidgetSpinnerMod.FIDGET_SPINNER_THROWABLE, this.getType());
        }
        if (!this.world.isRemote)
        {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0.5F, true);
            this.setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("Type", this.getType());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.setType(nbt.getInteger("Type"));
    }

    @Override
    public int getType()
    {
        return this.dataManager.get(EntityFidgetSpinnerExplosion.TYPE);
    }

    @Override
    public void setType(int type)
    {
        this.dataManager.set(EntityFidgetSpinnerExplosion.TYPE, type);
    }
}