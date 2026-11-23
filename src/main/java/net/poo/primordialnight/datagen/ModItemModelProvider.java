package net.poo.primordialnight.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PrimordialNightMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
            basicItem(ModItems.END_COMPASS.get());

            withExistingParent(ModItems.YMA_PYTU_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

            withExistingParent(ModItems.VOIDBLADE_SWORD.getId().getPath(), mcLoc("item/handheld"))
                    .texture("layer0", modLoc("item/" + ModItems.VOIDBLADE_SWORD.getId().getPath()));
        }

    }
