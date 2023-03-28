package dev.louis.chainsmpspells.spell;

import com.google.common.collect.ImmutableList;
import dev.louis.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
import dev.louis.chainsmpspells.accessor.ServerPlayerEntityJuggernautModeAccessor;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class JuggernautSpell extends Spell {

    public JuggernautSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
    }

    @Override
    public void cast() {
        if (isCastable()) {
            drainMana();
            ServerPlayerEntityJuggernautModeAccessor accessor =
                    ServerPlayerEntityJuggernautModeAccessor.access((ServerPlayerEntity) getCaster());
            accessor.makeJuggernaut(20*13);


        }
    }

    @Override
    public boolean isCastable() {
        if(getCaster().world.isClient())return super.isCastable();
        return ServerPlayerEntityJuggernautModeAccessor.access((ServerPlayerEntity) getCaster()).isNotInJuggernautMode() && super.isCastable();
    }


    public void sendActionBarUpdate(int time) {
        getCaster().sendMessage(Text.translatable("message.chainsmpspells.cooldown", time), true);
    }

    public static void generateJuggernautItemAndSetToSlot(ServerPlayerEntity player, int slot, Item item, long tick) {
        player.getInventory().setStack(0, generateJuggernautItem(Items.NETHERITE_SWORD, tick));

    }

    public static ItemStack generateJuggernautItem(Item item, long tickWorldtime) {
        return generateJuggernautItem(new ItemStack(item), tickWorldtime);
    }

    public static ItemStack generateJuggernautItem(ItemStack itemStack, long tickWorldtime) {
        if(itemStack.isEnchantable()) {
            enchantMax(itemStack);
        }
        ItemStackJuggernautModeAccessor.access(itemStack).setJuggernautModeTick(tickWorldtime);
        return itemStack;
    }

    public static void enchantMax(ItemStack itemStack) {
        enchantMax(itemStack, List.of(Enchantments.BANE_OF_ARTHROPODS, Enchantments.SMITE, Enchantments.KNOCKBACK, Enchantments.MENDING, Enchantments.FROST_WALKER, Enchantments.FIRE_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.BLAST_PROTECTION));
    }

    public static void enchantMax(ItemStack itemStack, List<Enchantment> excludedEnchantments) {
        var enchantments = new ArrayList<>(Registries.ENCHANTMENT.stream().toList());
        enchantments.removeAll(excludedEnchantments);
        for(Enchantment enchantment : enchantments) {
            if(enchantment.isAcceptableItem(itemStack)) {
                itemStack.addEnchantment(enchantment, enchantment.getMaxLevel());
            }
        }
    }


    public static void clearJuggernautItems(ServerPlayerEntity player) {
        List<DefaultedList<ItemStack>> combinedInventory = ImmutableList.of(player.getInventory().main, player.getInventory().armor, player.getInventory().offHand);
        for (List<ItemStack> list : combinedInventory) {
            for (int i = 0; i < list.size(); ++i) {
                ItemStack itemStack = list.get(i);

                if (itemStack.isEmpty()) continue;
                if (ItemStackJuggernautModeAccessor.access(itemStack).getJuggernautTick() <= 0L)continue;
                list.set(i, ItemStack.EMPTY);
            }
        }
    }

}
