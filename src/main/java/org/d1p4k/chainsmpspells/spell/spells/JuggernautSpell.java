package org.d1p4k.chainsmpspells.spell.spells;

import com.google.common.collect.ImmutableList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.registry.Registry;
import org.d1p4k.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
import org.d1p4k.chainsmpspells.accessor.ServerPlayerEntityJuggernautModeAccessor;
import org.d1p4k.chainsmpspells.mixin.ServerWorldAccessor;
import org.d1p4k.chainsmpspells.packet.s2c.SpellS2CPacket;
import org.d1p4k.chainsmpspells.scheduler.RepeatingTask;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class JuggernautSpell extends AbstractSpell {
    public static Identifier spellId = new Identifier("chainsmpspells" , "juggernaut");
    public JuggernautSpell(ServerPlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
    }

    public JuggernautSpell(ServerPlayerEntity player, Identifier spellIdentifier) {
        this(player, spellIdentifier,20);
    }

    @Override
    public void cast() {


        ServerPlayerEntityJuggernautModeAccessor accessor = ServerPlayerEntityJuggernautModeAccessor.access(player);
        System.out.print(accessor.getJuggernautTick());
        if (check() && accessor.isNotInJuggernautMode()) {
            player.getInventory().dropAll();
            accessor.setJuggernautModeTick(20*90);

            var tick = ((ServerWorldAccessor)player.getWorld()).getWorldProperties().getTime();

            player.getInventory().setStack(0, generateJuggernautItem(Items.NETHERITE_SWORD, tick));
            player.getInventory().setStack(1, generateJuggernautItem(Items.NETHERITE_AXE, tick));
            player.getInventory().setStack(2, generateJuggernautItem(Items.BOW, tick));

            ItemStack golden_apple = generateJuggernautItem(Items.GOLDEN_APPLE, tick);
            golden_apple.setCount(26);
            player.getInventory().setStack(3, golden_apple);


            ItemStack arrow = generateJuggernautItem(Items.ARROW, tick);
            arrow.setCount(1);
            player.getInventory().setStack(10, arrow);

            player.getInventory().armor.set(0, generateJuggernautItem(Items.NETHERITE_BOOTS, tick));
            player.getInventory().armor.set(1, generateJuggernautItem(Items.NETHERITE_LEGGINGS, tick));
            player.getInventory().armor.set(2, generateJuggernautItem(Items.NETHERITE_CHESTPLATE, tick));
            player.getInventory().armor.set(3, generateJuggernautItem(Items.NETHERITE_HELMET, tick));



        }
    }

    @Override
    public Identifier getID() {
        return new Identifier("chainsmpspells", "juggernaut");
    }


    public void startCounter() {
        ServerPlayerEntityJuggernautModeAccessor accessor = ServerPlayerEntityJuggernautModeAccessor.access(player);

        new RepeatingTask(20) {
            final UUID uuid = player.getUuid();
            int count = 90;
            @Override
            public void run() {
                var a = player.getWorld().getServer().getPlayerManager().getPlayer(uuid);
                if(a == null) {
                    this.cancel();
                    return;
                }
                if(ServerPlayerEntityJuggernautModeAccessor.access(a).isNotInJuggernautMode()) {
                    clearJuggernautItems(a);
                    this.cancel();
                    return;
                }
                if (count < 0) {
                    clearJuggernautItems(a);
                    accessor.getPlayer().kill();
                    this.cancel();
                    return;
                }
                sendPacket(count);
                count--;
            }
        };
    }

    public void sendPacket(int count) {
        var packet = new SpellS2CPacket(player);
        //TODO: Write better serialiser and deserialiser.
        packet.write((byte) 7);
        packet.getBuf().writeInt(count);
        packet.send();
    }

    private static ItemStack generateJuggernautItem(Item item, long tickWorldtime) {
        return generateJuggernautItem(new ItemStack(item), tickWorldtime);
    }

    private static ItemStack generateJuggernautItem(ItemStack itemStack, long tickWorldtime) {
        if(itemStack.isEnchantable()) {
            enchantMax(itemStack);
        }
        ItemStackJuggernautModeAccessor.access(itemStack).setJuggernautModeTick(tickWorldtime);
        return itemStack;
    }

    private static void enchantMax(ItemStack itemStack) {
        enchantMax(itemStack, List.of(Enchantments.BANE_OF_ARTHROPODS, Enchantments.SMITE, Enchantments.KNOCKBACK, Enchantments.MENDING, Enchantments.FROST_WALKER, Enchantments.FIRE_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.BLAST_PROTECTION));
    }

    private static void enchantMax(ItemStack itemStack, List<Enchantment> excludedSpells) {
        var enchantments = new ArrayList<>(Registries.ENCHANTMENT.stream().toList());
        enchantments.removeAll(excludedSpells);
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
    public boolean check() {
        return checkKnowledge() && checkMana();
    }

}
