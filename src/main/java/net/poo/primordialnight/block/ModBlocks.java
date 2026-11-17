package net.poo.primordialnight.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.item.ModItems;

import java.util.function.Supplier;

/**
 * Centralized registry for custom blocks using DeferredRegister and Java Generics.
 *
 * OOP Concepts: Generics (<T extends Block>), Functional Interfaces (Supplier<T>).
 * Reference: GUIA_BOAS_PRATICAS_ROMHENRI.md (Section 5.6 & 6.1)
 */

//!EXPLICAÇÃO:
/*como decidimos reaproveitar blocos já existentes do jogo, pra fazer o totem, ou ovo cabeça de dragão, blocos da arena, 
não precisamos cadastrar ou inventar nenhum bloco novo. Todo mod bem estruturado, já deixa o "cartório de blocos"
pronto e conectado na classe principal PrimordialNigthMod,java.
Se decidirmos criar algum bloco decorativo a estrutura já está pronta. */

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = //registrosDeferidos--> fala ao forge "Tudo o que eu anotar nesta lista BLOCKS pertence ao mod primordialnight"
            DeferredRegister.create(ForgeRegistries.BLOCKS, PrimordialNightMod.MOD_ID);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) { //bloco no chao do mine
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) { //bloquinho que o jogador segura
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {//é chamado no inicio de PrimordialNightMod
        BLOCKS.register(eventBus); // entrega a prancheta de blocos ao Forge quando o jogo liga.
    }
}

//!NOTAS:
/*Aqui usamos o conceito de Generics em POO em <T extends Block>. Com a herança conseguimos herdar várias filhas de blocos diretamente, uma a uma,
 mas com o generics, conseguimos herdar qual classe filha de block sem precisar um método separado para cada categoria filha.*/

