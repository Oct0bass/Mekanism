package mekanism.common.recipe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nonnull;
import mekanism.api.MekanismAPI;
import mekanism.api.gas.Gas;
import mekanism.api.recipes.ChemicalInfuserRecipe;
import mekanism.api.recipes.inputs.GasStackIngredient;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ChemicalInfuserRecipeSerializer<T extends ChemicalInfuserRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    private final IFactory<T> factory;

    public ChemicalInfuserRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }

    @Nonnull
    @Override
    public T read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
        JsonElement leftIngredients = JSONUtils.isJsonArray(json, "leftInput") ? JSONUtils.getJsonArray(json, "leftInput") :
                                      JSONUtils.getJsonObject(json, "leftInput");
        GasStackIngredient leftInput = GasStackIngredient.deserialize(leftIngredients);
        JsonElement rightIngredients = JSONUtils.isJsonArray(json, "rightInput") ? JSONUtils.getJsonArray(json, "rightInput") :
                                       JSONUtils.getJsonObject(json, "rightInput");
        GasStackIngredient rightInput = GasStackIngredient.deserialize(rightIngredients);
        //TODO
        Gas outputGas = MekanismAPI.EMPTY_GAS;
        int outputGasAmount = 0;
        return this.factory.create(recipeId, leftInput, rightInput, outputGas, outputGasAmount);
    }

    @Override
    public T read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
        GasStackIngredient leftInput = GasStackIngredient.read(buffer);
        GasStackIngredient rightInput = GasStackIngredient.read(buffer);
        Gas outputGas = buffer.readRegistryId();
        int outputGasAmount = buffer.readInt();
        return this.factory.create(recipeId, leftInput, rightInput, outputGas, outputGasAmount);
    }

    @Override
    public void write(@Nonnull PacketBuffer buffer, @Nonnull T recipe) {
        recipe.write(buffer);
    }

    public interface IFactory<T extends ChemicalInfuserRecipe> {

        T create(ResourceLocation id, GasStackIngredient leftInput, GasStackIngredient rightInput, Gas outputGas, int outputGasAmount);
    }
}