package ten3.core.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import ten3.lib.tile.option.RedstoneMode;
import ten3.util.*;
import ten3.core.item.Spanner;
import ten3.lib.tile.CmTileMachine;
import ten3.lib.tile.option.FaceOption;
import ten3.lib.client.RenderHelper;

import java.util.ArrayList;
import java.util.List;

public class HudSpanner extends Screen {

	static int w;
	static int h;

	public HudSpanner() {
		super(TranslateKeyUtil.make(""));
	}

	public void render(boolean catchIt, Player player, PoseStack s, BlockPos pos, BlockEntity t,
			Direction d) {

		s.pushPose();

		w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		init(Minecraft.getInstance(), w, h);// &*&

		Component tc = TranslateKeyUtil.translated("ten3.info.spanner.mode",
				"ten3.info.mode." + ItemUtil.getTag(player.getMainHandItem(), "mode"));

		int hp = player.isCreative() ? (int) (h / 3 * 2.6) : (int) (h / 3 * 2.42);
		// RenderHelper.render(s, w / 2 - 29, hp - 3, 58, 13, 256, 256, 0, 198, TConst.guiHandler);
		RenderHelper.renderCString(s, w / 2, hp, ExcUtil.safeInt(TranslateKeyUtil.GOLD.getColor()),
				tc);

		if (!catchIt)
			return;

		MutableComponent c1 = TranslateKeyUtil.translated("ten3.info.spanner.dire.energy");
		MutableComponent c2 = TranslateKeyUtil.translated("ten3.info.spanner.dire.item");
		MutableComponent c3 = TranslateKeyUtil.translated("ten3.info.spanner.dire.redstone");
		MutableComponent c4 = TranslateKeyUtil.translated("ten3.info.spanner.work_radius")
				.append(TranslateKeyUtil.make(String.valueOf(ClientHolder.radius.get(pos))));
		((CmTileMachine) t).levelIn = ExcUtil.safeInt(ClientHolder.level.get(pos));
		Component c0 = ((CmTileMachine) t).getDisplayWith().append(TranslateKeyUtil.make(" ("))
				.append(TranslateKeyUtil.translated("dire." + d.getSerializedName()))
				.append(TranslateKeyUtil.make(")"));

		ArrayList<Integer> ene = ClientHolder.energy.get(pos);
		ArrayList<Integer> itm = ClientHolder.item.get(pos);
		int red = ExcUtil.safeInt(ClientHolder.redstone.get(pos));

		int di = DireUtil.direToInt(d);

		if (ene != null && ene.get(di) != null) {
			c1.append(TranslateKeyUtil.translated("ten3.info." + FaceOption.toStr(ene.get(di))));
		}

		if (itm != null && itm.get(di) != null) {
			c2.append(TranslateKeyUtil.translated("ten3.info." + FaceOption.toStr(itm.get(di))));
		}

		if (red == RedstoneMode.LOW) {
			c3.append(TranslateKeyUtil.translated("ten3.info.low"));
		} else if (red == RedstoneMode.HIGH) {
			c3.append(TranslateKeyUtil.translated("ten3.info.high"));
		} else {
			c3.append(TranslateKeyUtil.translated("ten3.info.off"));
		}

		int x = w / 2;
		int y = h / 2 + h / 10;

		renderComponentTooltip(s, List.of(c0, c1, c2, c3, c4), x, y);

		s.popPose();

	}

	public static class RenderCallback implements HudRenderCallback {

		@Override
		@SuppressWarnings("all")
		public void onHudRender(PoseStack matrixStack, float tickDelta) {
			Player player = Minecraft.getInstance().player;
			if (player == null)
				return;

			ItemStack i = player.getMainHandItem();
			if (!(i.getItem() instanceof Spanner))
				return;

			Level world = player.level;
			if (world == null)
				return;
			HitResult result = Minecraft.getInstance().hitResult;
			if (result instanceof BlockHitResult r) {
				Direction d = r.getDirection();
				BlockPos hitPos = r.getBlockPos();
				BlockEntity t = world.getBlockEntity(hitPos);
				new HudSpanner().render(t instanceof CmTileMachine, player, matrixStack, hitPos, t,
						d);
				ParticleSpawner.spawnClt(ParticleSpawner.RANGE, hitPos.getX() + Math.random(),
						hitPos.getY() + Math.random(), hitPos.getZ() + Math.random(), 1);

			}
		}

	}

}
