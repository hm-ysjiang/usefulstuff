package hmysjiang.usefulstuffs.utils;

import com.google.common.collect.ImmutableMap;

import hmysjiang.usefulstuffs.items.ItemPackingGlue;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
	
	public static boolean hasNoBlockBelow(World world, BlockPos pos) {
		return world.getBlockState(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ())).getMaterial() == Material.AIR;
	}
	
	public static World getWorldFromId(int id) {
		for (WorldServer world:DimensionManager.getWorlds()) {
			if (world.provider.getDimension() == id) {
				return world;
			}
		}
		return null;
	}
	
	public static Object[] getBlockPosFacingEntityLookingAt(EntityLivingBase entity, int range) {
		Vec3d entityEyePos = entity.getPositionVector().add(new Vec3d(0, entity.getEyeHeight(), 0));
		Vec3d startPos = new Vec3d(entityEyePos.x, entityEyePos.y, entityEyePos.z);
		Vec3d gaze = entity.getLookVec().scale(0.05D);
		BlockPos prev = new BlockPos(entityEyePos);
		for (int i = 1 ; i<=range*20 ; i++) {
			BlockPos pos = new BlockPos(startPos.add(gaze.scale(i)));
			if (entity.world.getBlockState(pos).getMaterial() != Material.AIR) {
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
	
	/***
	 * 
	 * @param state
	 * @param tile
	 * @return the durability cost to pick up a block, used in {@link ItemPackingGlue#onItemUseFirst()}
	 */
	public static float getBlockDataDensity(World world, BlockPos pos, IBlockState state, TileEntity tile) {
		float den = 0;
		if (state != null) {
			//Block Hardness
			float hardness = state.getBlockHardness(world, pos);
			den += Math.log10(hardness + 2) / 0.7 * 10;
			
			//Block States
			ImmutableMap<IProperty<?>, Comparable<?>> properties = state.getProperties();
			for (IProperty<?> property : properties.keySet()) {
				if (property instanceof PropertyBool) {
					den += 1;
				}
				else if (property instanceof PropertyInteger) {
					den += 2;
				}
				else if (property instanceof PropertyDirection) {
					den += 3;
				}
				else {
					den += 4;
				}
			}
			
			//Extra cost for tile entities
			if (tile != null) {
				den *= 2;
			}
		}
		return den;
	}
	
	public static int getGroundHeight(World world, int x, int z) {
		if (!world.isAreaLoaded(new BlockPos(x, 64, z), 1)) return -1;
		for (int y = world.getHeight() ; y>=60 ; y--) {
			if (world.getBlockState(new BlockPos(x, y, z)) == Blocks.STONE.getDefaultState() || world.getBlockState(new BlockPos(x, y, z)) == Blocks.DIRT.getDefaultState() || world.getBlockState(new BlockPos(x, y, z)) == Blocks.GRASS.getDefaultState())
				return y;
		}
		return -1;
	}
	
}
