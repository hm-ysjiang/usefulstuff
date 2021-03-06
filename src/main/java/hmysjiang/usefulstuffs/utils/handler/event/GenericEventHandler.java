package hmysjiang.usefulstuffs.utils.handler.event;

import java.util.Random;

import org.apache.http.util.EntityUtils;

import hmysjiang.usefulstuffs.ConfigManager;
import hmysjiang.usefulstuffs.utils.helper.LogHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@EventBusSubscriber
public class GenericEventHandler {
	
	private static Random rnd = new Random();
	
	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event) {
		if (ConfigManager.chickenDropsFeather) {
			EntityLivingBase living = event.getEntityLiving();
			if (!living.world.isRemote) {
				if (living != null && living.getClass().equals(EntityChicken.class) && !living.isChild()) {
					if (!living.onGround && living.motionY < -0.11 && rnd.nextInt(300) == 0) {
						living.world.spawnEntity(new EntityItem(living.world, living.posX, living.posY, living.posZ, new ItemStack(Items.FEATHER)));
					}
				}
			}	
		}
	}
	
}
