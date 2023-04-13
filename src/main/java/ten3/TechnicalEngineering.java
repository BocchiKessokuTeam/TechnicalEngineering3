package ten3;

import java.util.List;

import net.minecraft.core.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.world.item.CreativeModeTab;
import ten3.core.client.HudSpanner;
import ten3.core.network.Network;
import ten3.init.*;
import ten3.lib.item.ItemGroupProvider;

public class TechnicalEngineering implements ModInitializer, ClientModInitializer {

	@Override
	public void onInitialize(ModContainer mod) {
		BlockInit.regAll();
		TileInit.regAll();
		ContInit.regAll();
		ItemInit.regAll();
		RecipeInit.regAll();
		Network.register();
		initItemGroups();
	}

	@ClientOnly
	@Override
	public void onInitializeClient(ModContainer mod) {
		ContInit.regClient();
		// TODO
		FluidInit.clientInit();
		HudRenderCallback.EVENT.register(HudSpanner::onRender);
		Network.registerClient();
	}

	public void initItemGroups() {
		List<CreativeModeTab> tabs = Registry.ITEM.stream()
				.filter(item -> item instanceof ItemGroupProvider p && p.getTab() != null)
				.map(item -> ((ItemGroupProvider) item).getTab()).toList();

		tabs.forEach(tab -> ItemGroupEvents.modifyEntriesEvent(tab)
				.register(e -> Registry.ITEM.stream()
						.filter(item -> item instanceof ItemGroupProvider p && p.getTab() == tab)
						.forEachOrdered(e::accept)));
	}

}
