package net.poo.primordialnight.item.custom;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.poo.primordialnight.procedures.SanctuaryArenaBuilderProcedure;


/**
 * Danilo's Idea: The Void Compass ("Eye of Pytu").
 * Guides the player in The End toward the fixed Sanctuary coordinates where the
 * Totem of the First Night should be erected.
 *
 * Responsible: Dev 4
 * OOP Concepts:
 * - Inheritance: extends Item
 * - Polymorphism: @Override use()
 * - Encapsulation: private static final constants for the Sanctuary coordinates in The End
 *
 * Reference Repository: boss-locator-main (BossCatalog.java / UnlockRequestPacket.java)
 */
public class EndCompassItem extends Item {


    public static final BlockPos SANCTUARY_TARGET_POS = new BlockPos(0, 70, 120);
    private static final int COMPASS_COOLDOWN_TICKS = 20;

    public EndCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack compassStack = player.getItemInHand(usedHand);

        if (level.isClientSide()) {
            return InteractionResultHolder.pass(compassStack);
        }

        // TODO (Dev 4):
        if (!level.dimension().equals(Level.END)) {
            player.displayClientMessage(Component.literal("O Olho de Pytũ só desperta no The End!"), true);
            return InteractionResultHolder.fail(compassStack);
        }
        player.displayClientMessage(Component.literal("Santuário de PytÛ em X: %d,Y: %d,Z: %d".formatted(SANCTUARY_TARGET_POS.getX(), SANCTUARY_TARGET_POS.getY(), SANCTUARY_TARGET_POS.getZ())), true);


        player.getCooldowns().addCooldown(this, COMPASS_COOLDOWN_TICKS);
        return InteractionResultHolder.sidedSuccess(compassStack, level.isClientSide());


    }
}
