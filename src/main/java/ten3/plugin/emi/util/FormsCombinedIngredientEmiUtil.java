package ten3.plugin.emi.util;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import ten3.lib.recipe.FormsCombinedIngredient;

public class FormsCombinedIngredientEmiUtil {

	public static EmiIngredient ofIngredient(FormsCombinedIngredient ingredient) {
		return switch (ingredient.form) {
			case "item" ->
				switch (ingredient.type) {
					case "tag" ->
						EmiIngredient.of(TagKey.create(Registry.ITEM_REGISTRY, ingredient.key),
								ingredient.amountOrCount);
					case "static" ->
						EmiStack.of(Registry.ITEM.get(ingredient.key), ingredient.amountOrCount);
					default ->
						null;
				};
			case "fluid" ->
				switch (ingredient.type) {
					case "tag" -> new FluidTagEmiIngredient(TagKey.create(Registry.FLUID_REGISTRY, ingredient.key),
							ingredient.amountOrCount);
					case "static" -> EmiStack.of(Registry.FLUID.get(ingredient.key), ingredient.amountOrCount);
					default ->
						null;
				};
			default ->
				null;
		};
	}

	public static EmiStack ofStack(FormsCombinedIngredient ingredient) {
		return switch (ingredient.form) {
			case "item" ->
				switch (ingredient.type) {
					case "static" ->
						EmiStack.of(Registry.ITEM.get(ingredient.key), ingredient.amountOrCount);
					default ->
						null;
				};
			case "fluid" ->
				switch (ingredient.type) {
					case "static" -> EmiStack.of(Registry.FLUID.get(ingredient.key), ingredient.amountOrCount);
					default ->
						null;
				};
			default ->
				null;
		};
	}

}
