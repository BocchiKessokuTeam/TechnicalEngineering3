package ten3.init;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.ScreenManager;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import ten3.TConst;
import ten3.TechnicalEngineering;
import ten3.core.machine.engine.EngineScreen;
import ten3.core.machine.engine.biomass.BiomassTile;
import ten3.core.machine.engine.extractor.ExtractorTile;
import ten3.core.machine.engine.metalizer.MetalizerTile;
import ten3.core.machine.cell.CellScreen;
import ten3.core.machine.cell.CellTile;
import ten3.core.machine.useenergy.beacon.BeaconScreen;
import ten3.core.machine.useenergy.beacon.BeaconTile;
import ten3.core.machine.useenergy.compressor.CompressorScreen;
import ten3.core.machine.useenergy.compressor.CompressorTile;
import ten3.core.machine.useenergy.farm.FarmScreen;
import ten3.core.machine.useenergy.farm.FarmTile;
import ten3.core.machine.useenergy.indfur.IndfurScreen;
import ten3.core.machine.useenergy.indfur.IndfurTile;
import ten3.core.machine.useenergy.mobrip.MobRipScreen;
import ten3.core.machine.useenergy.mobrip.MobRipTile;
import ten3.core.machine.useenergy.psionicant.PsionicantScreen;
import ten3.core.machine.useenergy.psionicant.PsionicantTile;
import ten3.core.machine.useenergy.pulverizer.PulverizerScreen;
import ten3.core.machine.useenergy.pulverizer.PulverizerTile;
import ten3.core.machine.useenergy.quarry.QuarryScreen;
import ten3.core.machine.useenergy.quarry.QuarryTile;
import ten3.core.machine.useenergy.smelter.FurnaceScreen;
import ten3.core.machine.useenergy.smelter.FurnaceTile;
import ten3.lib.capability.item.InventoryCm;
import ten3.lib.tile.CmContainer;
import ten3.lib.tile.CmContainerMachine;
import ten3.lib.tile.CmScreen;
import ten3.lib.tile.CmTileMachine;
import ten3.lib.wrapper.IntArrayCm;
import ten3.lib.wrapper.SlotCm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        RegistryObject<MenuType<?>> reg = CONS.register(id, () -> IForgeMenuType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            return new CmContainerMachine(windowId, id,
                    TileInit.getType(id).create(pos, inv.player.level.getBlockState(pos)),
                    inv, pos, createDefaultIntArr());
        }));
    }

    public static MenuType<?> getType(String id) {

        return regs.get(id).get();

    }

    static List<String> translucent = new ArrayList<>();
    static List<String> cutout = new ArrayList<>();

    @SubscribeEvent
    @SuppressWarnings("all")
    public static void doBinding(FMLClientSetupEvent e) {

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
            ItemBlockRenderTypes.setRenderLayer(BlockInit.getBlock(s), RenderType.translucent());
        }
        for (String s : cutout) {
            ItemBlockRenderTypes.setRenderLayer(BlockInit.getBlock(s), RenderType.cutout());
        }

    }

    @SuppressWarnings("all")
    private static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void bindScr(String s,
            MenuScreens.ScreenConstructor<M, U> fac) {
        MenuScreens.register((MenuType<? extends M>) getType(s), fac);
    }

}
