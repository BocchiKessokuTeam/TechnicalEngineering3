package ten3.lib.tile.mac;

import net.minecraft.core.Direction;
import ten3.lib.tile.option.FaceOption;
import ten3.lib.tile.option.Type;
import ten3.lib.wrapper.IntArrayCm;
import ten3.util.DireUtil;
import ten3.util.StorageType;

public class TransferManager {

	CmTileMachine tile;

	public int maxReceiveEnergy;
	public int maxExtractEnergy;
	public int maxReceiveItem;
	public int maxExtractItem;
	public int maxReceiveFluid;
	public int maxExtractFluid;
	public int maxStorageEnergy;

	public int initialEnergyStorage;// initial vars won't be change!
	public int initialEnergyReceive;// initial vars won't be change!
	public int initialEnergyExtract;// initial vars won't be change!
	public int initialItemReceive;
	public int initialItemExtract;
	public int initialFluidReceive;
	public int initialFluidExtract;

	public IntArrayCm energyAllow = new IntArrayCm(6);
	public IntArrayCm itemAllow = new IntArrayCm(6);
	public IntArrayCm fluidAllow = new IntArrayCm(6);

	public TransferManager(CmTileMachine t) {
		tile = t;
	}

	public void setCap(int store) {

		initialEnergyReceive = maxReceiveEnergy = tile.typeOf() == Type.CABLE ? store : store / 200;
		initialEnergyExtract = maxExtractEnergy = tile.typeOf() == Type.CABLE ? store : store / 200;
		initialEnergyStorage = maxStorageEnergy = store;
		initialItemExtract = maxExtractItem = 8;
		initialItemReceive = maxReceiveItem = 8;
		initialFluidExtract = maxExtractFluid = 100;
		initialFluidReceive = maxReceiveFluid = 100;

	}

	public void resetAll() {
		maxExtractItem = initialItemExtract;
		maxReceiveItem = initialItemReceive;
		maxExtractFluid = initialFluidExtract;
		maxReceiveFluid = initialFluidReceive;
		maxExtractEnergy = initialEnergyExtract;
		maxReceiveEnergy = initialEnergyReceive;
		maxStorageEnergy = initialEnergyStorage;// reset all, but data storage them to client!
	}

	public int direCheckFluid(Direction d) {
		if (!tile.hasFaceCapability(StorageType.FLUID, d)) {
			return FaceOption.NONE;
		}
		return fluidAllow.get(DireUtil.direToInt(d));
	}

	public void setOpenFluid(Direction d, int mode) {
		if (tile.hasFaceCapability(StorageType.FLUID, d))
			fluidAllow.set(DireUtil.direToInt(d), mode);
	}

	public int direCheckItem(Direction d) {
		if (!tile.hasFaceCapability(StorageType.ITEM, d)) {
			return FaceOption.NONE;
		}
		return itemAllow.get(DireUtil.direToInt(d));
	}

	public void setOpenItem(Direction d, int mode) {
		if (tile.hasFaceCapability(StorageType.ITEM, d))
			itemAllow.set(DireUtil.direToInt(d), mode);
	}

	public int direCheckEnergy(Direction d) {
		if (!tile.hasFaceCapability(StorageType.ENERGY, d)) {
			return FaceOption.NONE;
		}
		return energyAllow.get(DireUtil.direToInt(d));
	}

	public void setOpenEnergy(Direction d, int mode) {
		if (tile.hasFaceCapability(StorageType.ENERGY, d))
			energyAllow.set(DireUtil.direToInt(d), mode);
	}

}
