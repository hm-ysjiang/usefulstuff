package hmysjiang.usefulstuffs.tileentity;

import hmysjiang.usefulstuffs.blocks.BlockTFlipFlop;
import hmysjiang.usefulstuffs.init.ModBlocks;
import hmysjiang.usefulstuffs.utils.helper.LogHelper;
import hmysjiang.usefulstuffs.utils.helper.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTFlipFlop extends TileEntity implements ITickable {
	
	private boolean q, buf;
	private EnumFacing output;
	private EnumFacing input;
	private EnumFacing reset;
	
	public TileEntityTFlipFlop() {
		q = false;
		buf = false;
	}
	
	public void setSides(IBlockState state) {
		input = getInputSide(state);
		reset = input.getOpposite();
		int horizontalIdx = input.getHorizontalIndex() + 1;
		horizontalIdx %= 4;
		output = EnumFacing.getHorizontal(horizontalIdx);
	}
	
	public EnumFacing getInputSide(IBlockState state) {
		switch(ModBlocks.t_flipflop.getMetaFromState(state)) {
		case 0:	return EnumFacing.NORTH;
		case 1:	return EnumFacing.EAST;
		case 2:	return EnumFacing.SOUTH;
		case 3:	return EnumFacing.WEST;
		default:	return EnumFacing.DOWN;
		}
	}

	@Override
	public void update() {
		if (input != null) {	//tile loaded
			if (WorldHelper.isBlockSideBeingPowered(worldObj, pos, input) ^ buf) {
				buf = !buf;
				if (buf) {
					q = !q;
					worldObj.markAndNotifyBlock(pos, worldObj.getChunkFromBlockCoords(pos), worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3);
				}
			}
			if (WorldHelper.isBlockSideBeingPowered(worldObj, pos, reset)) {
				q = false;
			}	
		}
	}
	
	public EnumFacing getOutputSide() {
		return output;
	}
	
	public boolean shouldQOutput() {
		return q;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("Q")) {
			q = compound.getBoolean("Q");
			buf = compound.getBoolean("Buff");
			if (compound.hasKey("Input")) {
				input = EnumFacing.getHorizontal(compound.getInteger("Input"));
				output = EnumFacing.getHorizontal(compound.getInteger("Output"));
				reset = EnumFacing.getHorizontal(compound.getInteger("Reset"));	
			}
		}
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("Q", q);
		compound.setBoolean("Buff", buf);
		if (input != null) {
			compound.setInteger("Input", input.getHorizontalIndex());
			compound.setInteger("Output", output.getHorizontalIndex());
			compound.setInteger("Reset", reset.getHorizontalIndex());	
		}
		return super.writeToNBT(compound);
	}

}
