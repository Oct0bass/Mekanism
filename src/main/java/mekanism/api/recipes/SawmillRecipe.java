package mekanism.api.recipes;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import mekanism.api.annotations.NonNull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.apache.commons.lang3.tuple.Pair;

public class SawmillRecipe implements Predicate<@NonNull ItemStack> {

    protected static Random RANDOM = new Random();

    private final Ingredient input;

    private final ItemStack mainOutputDefinition;

    private final ItemStack secondaryOutputDefinition;

    private final double secondaryChance;

    public SawmillRecipe(Ingredient input, ItemStack mainOutputDefinition, ItemStack secondaryOutputDefinition, double secondaryChance) {
        this.input = input;
        this.mainOutputDefinition = mainOutputDefinition;
        this.secondaryOutputDefinition = secondaryOutputDefinition;
        this.secondaryChance = secondaryChance;
    }

    @Override
    public boolean test(@NonNull ItemStack stack) {
        return this.input.apply(stack);
    }

    public ChanceOutput getOutput(ItemStack input) {
        return new ChanceOutput(RANDOM.nextDouble());
    }

    public List<ItemStack> getMainOutputDefinition() {
        return Collections.singletonList(mainOutputDefinition);
    }

    public List<ItemStack> getSecondaryOutputDefinition() {
        return Collections.singletonList(secondaryOutputDefinition);
    }

    public double getSecondaryChance() {
        return secondaryChance;
    }

    public Ingredient getInput() {
        return input;
    }

    public class ChanceOutput {
        protected final double rand;

        public ChanceOutput(double rand) {
            this.rand = rand;
        }

        public ItemStack getMainOutput() {
            return mainOutputDefinition.copy();
        }

        public ItemStack getSecondaryOutput() {
            if (rand <= secondaryChance) {
                return secondaryOutputDefinition.copy();
            }
            return ItemStack.EMPTY;
        }
    }
}
