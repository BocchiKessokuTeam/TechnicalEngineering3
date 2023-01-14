package ten3.init;

import java.util.ArrayList;
import java.util.List;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import ten3.TConst;
import ten3.core.machine.cell.CellScreen;
import ten3.core.machine.engine.EngineScreen;
import ten3.core.machine.useenergy.beacon.BeaconScreen;
import ten3.core.machine.useenergy.compressor.CompressorScreen;
import ten3.core.machine.useenergy.farm.FarmScreen;
import ten3.core.machine.useenergy.indfur.IndfurScreen;
import ten3.core.machine.useenergy.mobrip.MobRipScreen;
import ten3.core.machine.useenergy.psionicant.PsionicantScreen;
import ten3.core.machine.useenergy.pulverizer.PulverizerScreen;
import ten3.core.machine.useenergy.quarry.QuarryScreen;
import ten3.core.machine.useenergy.smelter.FurnaceScreen;
import ten3.lib.capability.item.InventoryCm;
import ten3.lib.tile.CmContainerMachine;
import ten3.lib.wrapper.IntArrayCm;
import ten3.lib.wrapper.SlotCm;

public class ContInit {

	public static void regAll() {
		regCont("engine_extraction");
		regCont("engine_metal");
		regCont("engine_biomass");

		regCont("machine_smelter");
		regCont("machine_farm_manager");
		regCont("machine_pulverizer");
		regCont("machine_compressor");
		regCont("machine_beacon_simulator");
		regCont("machine_mob_ripper");
		regCont("machine_quarry");
		regCont("machine_psionicant");
		regCont("machine_induction_furnace");

		regCont("cell");
	}

	public static IntArrayCm createDefaultIntArr() {
		return new IntArrayCm(40);
	}

	public static InventoryCm createDefaultInv(List<? extends SlotCm> slots) {
		return new InventoryCm(40, slots);
	}

	public static void regCont(String id) {
		Registry.register(BuiltInRegistries.MENU, id, create((windowId, inv, data) -> {
			BlockPos pos = data.readBlockPos();
			return new CmContainerMachine(windowId, id,
					TileInit.getType(id).create(pos, inv.player.level.getBlockState(pos)), inv, pos,
					createDefaultIntArr());
		}));
	}

	private static <T extends AbstractContainerMenu> MenuType<T> create(
			IContainerFactory<T> factory) {
		return new MenuType<>(factory);
	}

	private interface IContainerFactory<T extends AbstractContainerMenu>
			extends MenuType.MenuSupplier<T> {
		T create(int windowId, Inventory inv, FriendlyByteBuf data);

		@Override
		default T create(int p_create_1_, Inventory p_create_2_) {
			return create(p_create_1_, p_create_2_, null);
		}
	}

	public static MenuType<?> getType(String id) {
		return BuiltInRegistries.MENU.get(TConst.asResource(id));
	}

	static List<String> translucent = new ArrayList<>();
	static List<String> cutout = new ArrayList<>();

	public static void doBinding() {

		translucent.add("cable");
		translucent.add("pipe");
		translucent.add("cell");


		cutout.add("engine_metal");
		cutout.add("engine_extraction");
		cutout.add("engine_biomass");

		bindScr("engine_metal", EngineScreen::new);
		bindScr("engine_extraction", EngineScreen::new);
		bindScr("engine_biomass", EngineScreen::new);

		bindScr("machine_smelter", FurnaceScreen::new);
		bindScr("machine_farm_manager", FarmScreen::new);
		bindScr("machine_pulverizer", PulverizerScreen::new);
		bindScr("machine_compressor", CompressorScreen::new);
		bindScr("machine_beacon_simulator", BeaconScreen::new);
		bindScr("machine_mob_ripper", MobRipScreen::new);
		bindScr("machine_quarry", QuarryScreen::new);
		bindScr("machine_psionicant", PsionicantScreen::new);
		bindScr("machine_induction_furnace", IndfurScreen::new);

		bindScr("cell", CellScreen::new);

		for (String s : translucent) {
			BlockRenderLayerMap.put(RenderType.translucent(), BlockInit.getBlock(s));
		}
		for (String s : cutout) {
			BlockRenderLayerMap.put(RenderType.cutout(), BlockInit.getBlock(s));
		}

	}


	@SuppressWarnings("unchecked")
	private static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void bindScr(
			String s, MenuScreens.ScreenConstructor<M, U> fac) {
		MenuScreens.register((MenuType<? extends M>) getType(s), fac);
	}

}
