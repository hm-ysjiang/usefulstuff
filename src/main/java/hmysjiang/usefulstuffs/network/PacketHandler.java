package hmysjiang.usefulstuffs.network;

import hmysjiang.usefulstuffs.Reference;
import hmysjiang.usefulstuffs.network.handler.*;
import hmysjiang.usefulstuffs.network.packet.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	public static SimpleNetworkWrapper INSTANCE;
	
	public static void init() {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
		
		int disc = 0;
		INSTANCE.registerMessage(FLDeadHandler.class, FLDead.class, disc++, Side.SERVER);
		INSTANCE.registerMessage(GuiButtonPressedHandler.class, GuiButtonPressed.class, disc++, Side.SERVER);
		INSTANCE.registerMessage(GuiSortPressedHandler.class, GuiSortPressed.class, disc++, Side.SERVER);
	}
	
}
