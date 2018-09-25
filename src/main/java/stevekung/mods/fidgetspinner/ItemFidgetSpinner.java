package stevekung.mods.fidgetspinner;

import javax.annotation.Nullable;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFidgetSpinner extends Item
{
    public ItemFidgetSpinner(String name)
    {
        super();
        this.setHasSubtypes(true);
        this.setUnlocalizedName(name);
        this.addPropertyOverride(new ResourceLocation("active"), new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack itemStack, @Nullable World world, @Nullable EntityLivingBase entity)
            {
                if (entity == null)
                {
                    return 0.0F;
                }
                else
                {
                    return itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Active") ? 1.0F : 0.0F;
                }
            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Active"))
        {
            return new ActionResult(EnumActionResult.PASS, itemStack);
        }
        if (!itemStack.hasTagCompound())
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean("Active", true);
            itemStack.setTagCompound(nbt);
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        if (world.rand.nextInt(1000) == 0 && itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Active"))
        {
            itemStack.setTagCompound(null);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab()
    {
        return CreativeTabs.MISC;
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab == CreativeTabs.MISC || tab == CreativeTabs.SEARCH)
        {
            for (int i = 0; i < 16; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int i = itemStack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(i).getUnlocalizedName();
    }
}