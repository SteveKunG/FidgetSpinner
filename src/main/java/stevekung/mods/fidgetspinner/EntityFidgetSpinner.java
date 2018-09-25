package stevekung.mods.fidgetspinner;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFidgetSpinner extends EntityThrowable
{
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityFidgetSpinner.class, DataSerializers.VARINT);

    public EntityFidgetSpinner(World world)
    {
        super(world);
    }

    public EntityFidgetSpinner(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }

    public EntityFidgetSpinner(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(EntityFidgetSpinner.TYPE, 0);
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 5);
        }
        if (!this.world.isRemote)
        {
            EntityItem item = new EntityItem(this.world, this.posX, this.posY, this.posZ, new ItemStack(FidgetSpinnerMod.FIDGET_SPINNER_THROWABLE, 1, this.getType()));
            this.world.spawnEntity(item);
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

    public int getType()
    {
        return this.dataManager.get(EntityFidgetSpinner.TYPE);
    }

    public void setType(int type)
    {
        this.dataManager.set(EntityFidgetSpinner.TYPE, type);
    }
}