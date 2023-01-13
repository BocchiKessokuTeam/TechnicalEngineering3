package ten3.core.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public interface IBaseRecipeCm<T extends Container> extends Recipe<T> {

    int time();

    List<ItemStack> output();

    int inputLimit(ItemStack stack);

	@Override
	default boolean isSpecial() {
		return true;
	}
}
