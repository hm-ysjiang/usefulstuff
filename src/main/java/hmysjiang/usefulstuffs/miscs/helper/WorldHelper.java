package hmysjiang.usefulstuffs.miscs.helper;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class WorldHelper {

	/**
	 * Check if a block can see sky, transparent blocks doesn't count
	 * Do not check the world is remote or not
	 * @param worldIn
	 * @param pos
	 * @return
	 */
	public static boolean canBlockSeeSky(World worldIn, BlockPos pos) {
		int max_h = worldIn.getHeight();
		for (int y = pos.getY()+1 ; y<max_h ; y++) 
			if (worldIn.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getMaterial() != Material.AIR) 
				return false;
		return true;
	}
	
	public static boolean isWorldRainingOrGoingTo(World worldIn) {
		return worldIn.rainingStrength > 0;
	}
	
	public static World getWorldFromId(int id) {
		for (WorldServer world:DimensionManager.getWorlds()) {
			if (world.provider.getDimension() == id) {
				return world;
			}
		}
		return null;
	}
	
	public static boolean hasNoBlockBelow(World world, BlockPos pos) {
		return world.getBlockState(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ())).getMaterial() == Material.AIR;
	}
	
	public static void updateLightInArea(World worldIn, double posX, double posY, double posZ) {
//		worldIn.markBlockRangeForRenderUpdate(new BlockPos(posX, posY, posZ), new BlockPos(posX, posY, posZ));
//		worldIn.notifyBlockUpdate(new BlockPos(posX, posY, posZ), worldIn.getBlockState(new BlockPos(posX, posY, posZ)), worldIn.getBlockState(new BlockPos(posX, posY, posZ)), 8);
		worldIn.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(
				posX, posY + 1, posZ));
		worldIn.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(
				posX, posY - 1, posZ));
		worldIn.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(
				posX + 1, posY, posZ));
		worldIn.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(
				posX - 1, posY, posZ));
		worldIn.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(
				posX, posY, posZ + 1));
		worldIn.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(
				posX, posY, posZ - 1));
	}
	
	public static BlockPos[] getAdjacentBlockPos(BlockPos pos) {
		return new BlockPos[] {
				new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()),
				new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()),
				new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()),
				new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()),
				new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1),
				new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)
				};
	}
	
	public static Object[] getBlockPosFacingEntityLookingAt(EntityLivingBase entity, int range) {
		Vec3d entityEyePos = entity.getPositionVector().add(new Vec3d(0, entity.getEyeHeight(), 0));
		Vec3d startPos = new Vec3d(entityEyePos.xCoord, entityEyePos.yCoord, entityEyePos.zCoord);
		Vec3d gaze = entity.getLookVec().scale(0.05D);
		BlockPos prev = new BlockPos(entityEyePos);
		for (int i = 1 ; i<=range*20 ; i++) {
			BlockPos pos = new BlockPos(startPos.add(gaze.scale(i)));
			if (entity.worldObj.getBlockState(pos).getMaterial() != Material.AIR) {
				return new Object[] {pos, getRelationBetweenAdjacentBlocks(pos, prev)};
			}
			if (!pos.equals(prev)) {
				prev = new BlockPos(startPos.add(gaze.scale(i)));
			}
		}
		return null;
	}
	
	public static EnumFacing getRelationBetweenAdjacentBlocks(BlockPos dominant, BlockPos recessive) {
		if (dominant.getX() != recessive.getX()) {
			if (dominant.getY() != recessive.getY() || dominant.getZ() != recessive.getZ()) return null;
			if (dominant.getX() > recessive.getX()) return EnumFacing.WEST;
			else return EnumFacing.EAST;
		}
		else if (dominant.getY() != recessive.getY()) {
			if (dominant.getX() != recessive.getX() || dominant.getZ() != recessive.getZ()) return null;
			if (dominant.getY() > recessive.getY()) return EnumFacing.DOWN;
			else return EnumFacing.UP;
		}
		else if (dominant.getZ() != recessive.getZ()) {
			if (dominant.getX() != recessive.getX() || dominant.getY() != recessive.getY()) return null;
			if (dominant.getZ() > recessive.getZ()) return EnumFacing.NORTH;
			else return EnumFacing.SOUTH;
		}
		else return null;
	}
	
	public static BlockPos getPositionFromPosAndFacing(BlockPos pos, EnumFacing facing) {
		switch(facing) {
		case WEST:
			return new BlockPos(pos.getX()-1, pos.getY(), pos.getZ());
		case EAST:
			return new BlockPos(pos.getX()+1, pos.getY(), pos.getZ());
		case NORTH:
			return new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1);
		case SOUTH:
			return new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1);
		case UP:
			return new BlockPos(pos.getX(), pos.getY()+1, pos.getZ());
		case DOWN:
			return new BlockPos(pos.getX(), pos.getY()-1, pos.getZ());
		default: return null;
		}
	}
	
}
