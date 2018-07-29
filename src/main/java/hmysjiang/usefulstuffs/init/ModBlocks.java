package hmysjiang.usefulstuffs.init;

import hmysjiang.usefulstuffs.Reference;
import hmysjiang.usefulstuffs.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
	
	public static Block lightbulb;
	public static Block well;
	public static Block rain_detector;
	public static Block campfire;
//	public static Block lantern;
	public static Block filing_cabinet;
	public static Block glued_box;
	public static Block t_flipflop;
	
	public static void init() {
		lightbulb = new BlockLightBulb();
		well = new BlockWell();
		rain_detector = new BlockRainDetector();
		campfire = new BlockCampfire();
//		lantern = new BlockLantern();
		filing_cabinet = new BlockFilingCabinet();
		glued_box = new BlockGluedBox();
		t_flipflop = new BlockTFlipFlop();
	}
	
	public static void register() {
		registerBlock(lightbulb);
		registerBlock(well);
		registerBlock(rain_detector);
		registerBlock(campfire);
//		registerBlock(lantern);
		registerBlock(filing_cabinet);
		registerBlock(glued_box);
		registerBlock(t_flipflop);
	}
	
	public static void registerRenders() {
		registerRender(lightbulb);
		registerRender(well);
		registerRender(rain_detector);
		registerRender(campfire);
//		registerRender(lantern);
		registerRender(filing_cabinet);
		registerRender(glued_box);
		registerRender(t_flipflop);
	}
	
	public static void registerBlock(Block block) {
		GameRegistry.register(block);
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		GameRegistry.register(item);
	}
	
	public static void registerBlock(Block block, ItemBlock item) {
		GameRegistry.register(block);
		GameRegistry.register(item);
	}
	
	private static void registerRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, block.getUnlocalizedName().substring(18)), "inventory"));
	}
	
}