package ten3.lib.recipe;

import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import ten3.init.RecipeInit;
import ten3.lib.capability.item.AdvancedInventory;
import ten3.lib.tile.extension.CmTileMachineRecipe;
import ten3.util.TagUtil;

public class FormsCombinedRecipe implements RandRecipe<Container> {

	protected ResourceLocation reg;
	protected ResourceLocation id;
	public List<FormsCombinedIngredient> input;
	public List<FormsCombinedIngredient> output;
	protected int time;

	public FormsCombinedRecipe(ResourceLocation regName, ResourceLocation idIn,
			List<FormsCombinedIngredient> ip,
			List<FormsCombinedIngredient> op, int cookTimeIn) {

		id = idIn;
		reg = regName;
		time = cookTimeIn;
		input = ip;
		output = op;
	}

	@Override
	public boolean matches(Container inv, Level worldIn) {
		if (!(inv instanceof AdvancedInventory))
			return false;
		CmTileMachineRecipe mac = (CmTileMachineRecipe) ((AdvancedInventory) inv).tile;

		for (FormsCombinedIngredient i : input) {
			if (!i.check(mac))
				return false;
		}
		return true;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonNullList = NonNullList.create();
		for (FormsCombinedIngredient i : input) {
			if (i != null) {
				nonNullList.add(i.toOriginStackIngredients());
			}
		}
		return nonNullList;
	}

	@Override
	public int inputLimit(ItemStack stack) {
		for (FormsCombinedIngredient lst : input) {
			if (lst.matchItems.contains(stack.getItem())) {
				return lst.amountOrCount;
			} else if (lst.ifTagItem != null
					&& TagUtil.containsItem(stack.getItem(), lst.ifTagItem)) {
				return lst.amountOrCount;
			}
		}
		return 0;
	}

	@Override
	public int time() {
		return time;
	}

	@Override
	public ItemStack assemble(Container p_44001_) {
		return ItemStack.EMPTY;
	}

	public List<FormsCombinedIngredient> output() {
		return output;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeType<?> getType() {
		return RecipeInit.getRcpType(reg.getPath());
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipeInit.getRcp(getId().getPath());
	}

}
