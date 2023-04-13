package ten3.init.tab;

import net.minecraft.world.item.CreativeModeTab;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import ten3.TConst;
import ten3.init.ItemInit;

public class DefGroup {

	public static final CreativeModeTab BLOCK = QuiltItemGroup.builder(TConst.asResource("block"))
			.icon(() -> ItemInit.getItem("tin_ore").getDefaultInstance()).build();

	public static final CreativeModeTab MAC = QuiltItemGroup.builder(TConst.asResource("machine"))
			.icon(() -> ItemInit.getItem("machine_pulverizer").getDefaultInstance()).build();

	public static final CreativeModeTab ITEM = QuiltItemGroup.builder(TConst.asResource("item"))
			.icon(() -> ItemInit.getItem("tin_ingot").getDefaultInstance()).build();

	public static final CreativeModeTab TOOL = QuiltItemGroup.builder(TConst.asResource("tool"))
			.icon(() -> ItemInit.getItem("photosyn_levelup").getDefaultInstance()).build();

}
