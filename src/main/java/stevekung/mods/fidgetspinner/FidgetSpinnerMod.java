package stevekung.mods.fidgetspinner;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stevekung.mods.stevekunglib.utils.CommonRegistryUtils;
import stevekung.mods.stevekunglib.utils.client.ClientRegistryUtils;
import stevekung.mods.stevekunglib.utils.client.ClientUtils;

@Mod(modid = FidgetSpinnerMod.MOD_ID, name = FidgetSpinnerMod.NAME, version = FidgetSpinnerMod.VERSION)
public class FidgetSpinnerMod
{
    public static final String NAME = "Steve's Fidget Spinner";
    public static final String MOD_ID = "steve's_fidget_spinner";
    public static final int MAJOR_VERSION = 1;
    public static final int MINOR_VERSION = 0;
    public static final int BUILD_VERSION = 0;
    public static final String VERSION = FidgetSpinnerMod.MAJOR_VERSION + "." + FidgetSpinnerMod.MINOR_VERSION + "." + FidgetSpinnerMod.BUILD_VERSION;
    public static final String CLIENT_CLASS = "stevekung.mods.fidgetspinner.ClientProxy";
    public static final String SERVER_CLASS = "stevekung.mods.fidgetspinner.CommonProxy";

    @SidedProxy(clientSide = "stevekung.mods.fidgetspinner.ClientProxy", serverSide = "stevekung.mods.fidgetspinner.ServerProxy")
    public static ServerProxy PROXY;

    public static Item FIDGET_SPINNER;
    public static Item FIDGET_SPINNER_THROWABLE;
    public static Item FIDGET_SPINNER_EXPLOSION;

    private static ClientRegistryUtils CLIENT_REGISTRY;
    private static final CommonRegistryUtils COMMON_REGISTRY = new CommonRegistryUtils(FidgetSpinnerMod.MOD_ID);

    static
    {
        if (ClientUtils.isClient())
        {
            FidgetSpinnerMod.CLIENT_REGISTRY = new ClientRegistryUtils(FidgetSpinnerMod.MOD_ID);
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        FidgetSpinnerMod.initModInfo(event.getModMetadata());

        FidgetSpinnerMod.FIDGET_SPINNER = new ItemFidgetSpinner("fidget_spinner");
        FidgetSpinnerMod.FIDGET_SPINNER_THROWABLE = new ItemFidgetSpinnerThrowable("fidget_spinner_throwable");
        FidgetSpinnerMod.FIDGET_SPINNER_EXPLOSION = new ItemFidgetSpinnerExplosion("fidget_spinner_explosion");

        FidgetSpinnerMod.COMMON_REGISTRY.registerItem(FidgetSpinnerMod.FIDGET_SPINNER);
        FidgetSpinnerMod.COMMON_REGISTRY.registerItem(FidgetSpinnerMod.FIDGET_SPINNER_THROWABLE);
        FidgetSpinnerMod.COMMON_REGISTRY.registerItem(FidgetSpinnerMod.FIDGET_SPINNER_EXPLOSION);
        FidgetSpinnerMod.COMMON_REGISTRY.registerNonMobEntity(EntityFidgetSpinner.class, "fidget_spinner", 64, 3, true);
        FidgetSpinnerMod.COMMON_REGISTRY.registerNonMobEntity(EntityFidgetSpinnerExplosion.class, "fidget_spinner_explosion", 64, 3, true);
        ClientRegistryUtils.registerEntityRendering(EntityFidgetSpinner.class, RenderFidgetSpinner::new);
        ClientRegistryUtils.registerEntityRendering(EntityFidgetSpinnerExplosion.class, RenderFidgetSpinner::new);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (ClientUtils.isClient())
        {
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler((itemStack, tintIndex) -> EnumDyeColor.byMetadata(itemStack.getItemDamage()).getColorValue(), FidgetSpinnerMod.FIDGET_SPINNER, FidgetSpinnerMod.FIDGET_SPINNER_THROWABLE, FidgetSpinnerMod.FIDGET_SPINNER_EXPLOSION);

            for (int i = 0; i < 16; i++)
            {
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(FidgetSpinnerMod.FIDGET_SPINNER, i, new ModelResourceLocation(MOD_ID + ":fidget_spinner", "inventory"));
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(FidgetSpinnerMod.FIDGET_SPINNER_THROWABLE, i, new ModelResourceLocation(MOD_ID + ":fidget_spinner", "inventory"));
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(FidgetSpinnerMod.FIDGET_SPINNER_EXPLOSION, i, new ModelResourceLocation(MOD_ID + ":fidget_spinner", "inventory"));
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        for (int i = 0; i < 16; i++)
        {
            if (i != 15)
            {
                GameRegistry.addShapedRecipe(new ResourceLocation(MOD_ID, "fidget_spinner_" + i), new ResourceLocation(MOD_ID, "fidget_spinner"), new ItemStack(FidgetSpinnerMod.FIDGET_SPINNER, 8, 15 - i), "FFF", "FDF", "FFF", 'F', new ItemStack(FidgetSpinnerMod.FIDGET_SPINNER, 1, 0), 'D', new ItemStack(Items.DYE, 1, i));
            }
            else
            {
                GameRegistry.addShapedRecipe(new ResourceLocation(MOD_ID, "fidget_spinner_black"), new ResourceLocation(MOD_ID, "fidget_spinner"), new ItemStack(FidgetSpinnerMod.FIDGET_SPINNER, 16, 0), " P ", "PIP", " P ", 'P', Blocks.IRON_BARS, 'I', Items.IRON_INGOT);
            }
            GameRegistry.addShapedRecipe(new ResourceLocation(MOD_ID, "fidget_spinner_explosion"), null, new ItemStack(FidgetSpinnerMod.FIDGET_SPINNER_EXPLOSION, 8, i), "FFF", "FTF", "FFF", 'F', new ItemStack(FidgetSpinnerMod.FIDGET_SPINNER_THROWABLE, 1, i), 'T', Blocks.TNT);
            GameRegistry.addShapelessRecipe(new ResourceLocation(MOD_ID, "fidget_spinner_throwable"), null, new ItemStack(FidgetSpinnerMod.FIDGET_SPINNER_THROWABLE, 1, i), Ingredient.fromStacks(new ItemStack(FidgetSpinnerMod.FIDGET_SPINNER, 1, i), new ItemStack(Items.SNOWBALL)));
        }
    }

    private static void initModInfo(ModMetadata info)
    {
        info.autogenerated = false;
        info.modId = FidgetSpinnerMod.MOD_ID;
        info.name = FidgetSpinnerMod.NAME;
        info.version = FidgetSpinnerMod.VERSION;
        info.description = "A Small Fidget Spinner mod!";
        info.authorList = Arrays.asList("SteveKunG");
    }
}