package vice.kotrt_core.mixins.RecipeCaching;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.kotrt_core.KoTRTCore;

import java.util.*;

@Mixin({RecipeManager.class})
public class RecipeCachingMixin
{
    private final Map<Long, IRecipe<IInventory>> recipeCache = new HashMap<>();
    private final Map<Long, List<IRecipe<IInventory>>> recipeListCache = new HashMap<>();

    private final Random rand = new Random();

    @Inject(method = "getRecipeFor", at = @At("HEAD"), cancellable = true)
    private <C extends IInventory, T extends IRecipe<C>> void onGetRecipe(IRecipeType<T> recipeTypeIn, C inventoryIn, World worldIn, CallbackInfoReturnable<Optional<IRecipe<IInventory>>> c)
    {
        if (this.rand.nextInt(500) == 0 || inventoryIn == null || inventoryIn.getContainerSize() <= 0)
            return;

        IRecipe<IInventory> recipe = this.recipeCache.get(calcHash(inventoryIn, recipeTypeIn));

        if (recipe == null || !recipeTypeIn.tryMatch(recipe, worldIn, inventoryIn).isPresent())
            return;

        c.setReturnValue(Optional.of(recipe));
    }

    @Inject(method = "getRecipeFor", at = @At("RETURN"))
    private <C extends IInventory, T extends IRecipe<C>> void onEndGetRecipe(IRecipeType<T> recipeTypeIn, C inventoryIn, World worldIn, CallbackInfoReturnable<Optional<T>> c)
    {
        Optional<T> val = c.getReturnValue();

        if (!val.isPresent() || inventoryIn == null || inventoryIn.getContainerSize() <= 0)
            return;

        long hash = calcHash(inventoryIn, recipeTypeIn);
        if (hash != -1L)
        {
            this.recipeCache.put(hash, (IRecipe<IInventory>) val.get());
        }
    }

    @Inject(method = "getRecipesFor", at = @At("HEAD"), cancellable = true)
    private <C extends IInventory, T extends IRecipe<C>> void onGetRecipes(IRecipeType<T> recipeTypeIn, C inventoryIn, World worldIn, CallbackInfoReturnable<ArrayList<IRecipe<IInventory>>> c)
    {
        if (this.rand.nextInt(500) == 0 || inventoryIn == null || inventoryIn.getContainerSize() <= 0)
            return;

        List<IRecipe<IInventory>> recipes = this.recipeListCache.get(calcHash(inventoryIn, recipeTypeIn));

        if (recipes != null && !recipes.isEmpty() && recipeTypeIn.tryMatch(recipes.get(0), worldIn, inventoryIn).isPresent())
            c.setReturnValue(new ArrayList<>(recipes));
    }

    @Inject(method = "getRecipesFor", at = @At("RETURN"))
    private <C extends IInventory, T extends IRecipe<C>> void onEndgetRecipes(IRecipeType<T> recipeTypeIn, C inventoryIn, World worldIn, CallbackInfoReturnable<List<T>> c)
    {
        List<?> list = c.getReturnValue();
        if (list.isEmpty() || inventoryIn == null || inventoryIn.getContainerSize() <= 0)
            return;

        long hash = calcHash(inventoryIn, recipeTypeIn);
        if (hash != -1L)
            this.recipeListCache.put(hash, new ArrayList<>((Collection<? extends IRecipe<IInventory>>) list));
    }

    private long calcHash(IInventory inventory, IRecipeType type)
    {
        if (inventory == null)
            return type.hashCode();

        long hash = type.hashCode();
        boolean onlyAir = true;

        if (inventory.hashCode() != System.identityHashCode(inventory))
            hash = 31L * hash + inventory.hashCode();

        int size = inventory.getContainerSize();
        for (int i = 0; i < size; i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack != null && !stack.isEmpty()) {
                hash = 31L * hash + i;
                hash = 31L * hash + stack.getItem().hashCode();
                hash = hash * 31L + stack.getCount();
                hash = 31L * hash + stack.getDamageValue();
                if (stack.hasTag())
                {
                    assert stack.getTag() != null;
                    hash = 31L * hash + stack.getTag().hashCode();
                }
                onlyAir = false;
            }
        }
        if (onlyAir)
            return -1L;
        return hash;
    }
}