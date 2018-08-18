package hmysjiang.usefulstuffs.blocks.tflipflop;

import hmysjiang.usefulstuffs.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTFlipFlop extends TileEntity {

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
		notifyNeighbors(world, pos);
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

	public void updateSignal() {
		if (input != null) {	//tile loaded
			if (world.isSidePowered(pos.offset(input), input) ^ buf) {
				buf = !buf;
				if (buf) {
					q = !q;
					world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), world.getBlockState(pos), world.getBlockState(pos), 3);
					notifyNeighbors(world, pos);
				}
			}
			if (world.isSidePowered(pos.offset(reset), reset) && q) {
				q = false;
				world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), world.getBlockState(pos), world.getBlockState(pos), 3);
				notifyNeighbors(world, pos);
			}
		}
	}
	
	void notifyNeighbors(World world, BlockPos pos) {
		if (world != null && this.getBlockType() instanceof BlockTFlipFlop) {
			world.notifyNeighborsOfStateChange(pos, this.blockType, false);
			world.notifyNeighborsOfStateChange(pos.offset(this.output), this.blockType, false);
			world.notifyNeighborsOfStateChange(pos.offset(this.output.getOpposite()), this.blockType, false);
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
