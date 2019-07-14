package mekanism.api.recipes;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiPredicate;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.annotations.NonNull;
import mekanism.api.infuse.InfusionContainer;
import mekanism.api.recipes.inputs.InfusionIngredient;
import mekanism.api.recipes.outputs.OreDictSupplier;
import mekanism.common.util.FieldsAreNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * Created by Thiakil on 14/07/2019.
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class MetallurgicInfuserRecipe implements BiPredicate<InfusionContainer, ItemStack>, OutputDefinition<ItemStack> {

    private final Ingredient itemInput;

    private final InfusionIngredient infusionInput;

    private final ItemStack outputDefinition;

    protected MetallurgicInfuserRecipe(Ingredient itemInput, InfusionIngredient infusionInput, ItemStack outputDefinition) {
        this.itemInput = itemInput;
        this.infusionInput = infusionInput;
        this.outputDefinition = outputDefinition.copy();
    }

    @Override
    public boolean test(InfusionContainer infusionContainer, ItemStack itemStack) {
        return infusionInput.test(infusionContainer) && itemInput.apply(itemStack);
    }

    @Override
    public @NonNull Collection<@NonNull ItemStack> getOutputDefinition() {
        return Collections.singleton(this.outputDefinition);
    }

    public ItemStack getOutput(InfusionContainer inputInfuse, ItemStack inputItem) {
        return this.outputDefinition.copy();
    }

    public InfusionIngredient getInfusionInput() {
        return this.infusionInput;
    }

    public Ingredient getItemInput() {
        return this.itemInput;
    }

    public static class MetallurgicInfuserRecipeOre extends MetallurgicInfuserRecipe {

        private final OreDictSupplier oreOutput;

        public MetallurgicInfuserRecipeOre(Ingredient itemInput, InfusionIngredient infusionInput, String outputOredictName) {
            super(itemInput, infusionInput, ItemStack.EMPTY);
            this.oreOutput = new OreDictSupplier(outputOredictName);
        }

        @Override
        public @NonNull Collection<@NonNull ItemStack> getOutputDefinition() {
            return oreOutput.getPossibleOutputs();
        }

        @Override
        public ItemStack getOutput(InfusionContainer inputInfuse, ItemStack inputItem) {
            return oreOutput.get();
        }
    }
}
