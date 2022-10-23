package org.d1p4k.chainsmpspells.spell.spells;

import com.google.common.collect.ImmutableList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.d1p4k.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
import org.d1p4k.chainsmpspells.accessor.ServerPlayerEntityJuggernautModeAccessor;
import org.d1p4k.chainsmpspells.mixin.ServerWorldAccessor;
import org.d1p4k.chainsmpspells.packet.s2c.SpellS2CPacket;
import org.d1p4k.chainsmpspells.scheduler.RepeatingTask;
import org.d1p4k.nebula.spell.AbstractSpell;

import java.util.List;
import java.util.UUID;

public class JuggernautSpell extends AbstractSpell {
    public static Identifier spellId = new Identifier("css" , "juggernaut");
    public JuggernautSpell(ServerPlayerEntity player, Identifier spellIdentifier, int cost) {
        super(player, spellIdentifier, cost);
    }

    public JuggernautSpell(ServerPlayerEntity player, Identifier spellIdentifier) {
        this(player, spellIdentifier,20);
    }

    @Override
    public void cast() {
        var tick = ((ServerWorldAccessor)player.getWorld()).getWorldProperties().getTime();
        var test = new ItemStack(Items.DIAMOND_SWORD);
        ItemStackJuggernautModeAccessor.access(test).setJuggernautModeTick(tick);
        player.getInventory().setStack(0, test);
        if(true)return;


        ServerPlayerEntityJuggernautModeAccessor accessor = ServerPlayerEntityJuggernautModeAccessor.access(player);
        if (check() && !accessor.isInJuggernautMode()) {
            accessor.setInJuggernautMode(true);
            player.getInventory().dropAll();
            //addItems();
            startCounter();

        }
    }
    public ItemStack addItemWithEnchant(ItemStack itemStack, long tick, Enchantment... enchantments) {
        for(Enchantment enchantment : enchantments) {
            itemStack.addEnchantment(enchantment, enchantment.getMaxLevel());
        }
        ItemStackJuggernautModeAccessor.access(itemStack).setJuggernautModeTick(tick);
        return itemStack;
    }
    /**public void addItems() {
        var sword = new ItemStack(Items.NETHERITE_SWORD);
        sword.addEnchantment(Enchantments.SHARPNESS, 5);
        sword.addEnchantment(Enchantments.FIRE_ASPECT, 2);
        sword.addEnchantment(Enchantments.LOOTING, 3);
        sword.addEnchantment(Enchantments.SWEEPING, 3);
        player.getInventory().main.set(0,sword);
        ItemStackJuggernautModeAccessor.access(sword).setJuggernautMode(true);

        var axe = new ItemStack(Items.NETHERITE_AXE);
        axe.addEnchantment(Enchantments.SHARPNESS, 5);
        axe.addEnchantment(Enchantments.EFFICIENCY, 5);
        axe.addEnchantment(Enchantments.SILK_TOUCH, 5);
        player.getInventory().main.set(1,axe);
        ItemStackJuggernautModeAccessor.access(axe).setJuggernautMode(true);

        var bow = new ItemStack(Items.BOW);
        bow.addEnchantment(Enchantments.POWER, 5);
        bow.addEnchantment(Enchantments.PUNCH, 2);
        bow.addEnchantment(Enchantments.FLAME, 1);
        bow.addEnchantment(Enchantments.INFINITY, 1);
        bow.addEnchantment(Enchantments.BINDING_CURSE, 2);
        player.getInventory().main.set(2,bow);
        ItemStackJuggernautModeAccessor.access(bow).setJuggernautMode(true);
        addConsumables();
        addShield();
        addArmor();
    }
    private void addConsumables() {
        var a = new ItemStack(Items.GOLDEN_APPLE);
        a.setCount(32);
        ItemStackJuggernautModeAccessor.access(a).setJuggernautMode(true);
        player.getInventory().main.set(3, a);

        var b = new ItemStack(Items.ARROW);
        ItemStackJuggernautModeAccessor.access(b).setJuggernautMode(true);
        player.getInventory().main.set(4,b);
    }
    private void addArmor() {
        var helmet = new ItemStack(Items.NETHERITE_HELMET);
        helmet.addEnchantment(Enchantments.PROTECTION, 4);
        helmet.addEnchantment(Enchantments.AQUA_AFFINITY, 1);
        helmet.addEnchantment(Enchantments.RESPIRATION, 3);
        helmet.addEnchantment(Enchantments.BINDING_CURSE, 2);
        ItemStackJuggernautModeAccessor.access(helmet).setJuggernautMode(true);
        player.getInventory().armor.set(3, helmet);

        var chestplate = new ItemStack(Items.NETHERITE_CHESTPLATE);
        chestplate.addEnchantment(Enchantments.PROTECTION, 4);
        chestplate.addEnchantment(Enchantments.BINDING_CURSE, 2);
        ItemStackJuggernautModeAccessor.access(chestplate).setJuggernautMode(true);
        player.getInventory().armor.set(2, chestplate);

        var leggins = new ItemStack(Items.NETHERITE_LEGGINGS);
        leggins.addEnchantment(Enchantments.PROTECTION, 4);
        leggins.addEnchantment(Enchantments.SWIFT_SNEAK, 3);
        leggins.addEnchantment(Enchantments.BINDING_CURSE, 2);
        ItemStackJuggernautModeAccessor.access(leggins).setJuggernautMode(true);
        player.getInventory().armor.set(1, leggins);

        var boots = new ItemStack(Items.NETHERITE_BOOTS);
        boots.addEnchantment(Enchantments.PROTECTION, 4);
        boots.addEnchantment(Enchantments.SOUL_SPEED, 3);
        boots.addEnchantment(Enchantments.DEPTH_STRIDER, 3);
        boots.addEnchantment(Enchantments.FEATHER_FALLING, 4);
        boots.addEnchantment(Enchantments.BINDING_CURSE, 2);
        ItemStackJuggernautModeAccessor.access(boots).setJuggernautMode(true);
        player.getInventory().armor.set(0, boots);
    }
    private void addShield() {
        var shield = new ItemStack(Items.SHIELD);
        ItemStackJuggernautModeAccessor.access(shield).setJuggernautMode(true);
        player.getInventory().offHand.set(0, shield);
    }**/


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
    public static void clearJuggernautItems(ServerPlayerEntity player) {
        List<DefaultedList<ItemStack>> combinedInventory = ImmutableList.of(player.getInventory().main, player.getInventory().armor, player.getInventory().offHand);
        for (List<ItemStack> list : combinedInventory) {
            for (int i = 0; i < list.size(); ++i) {
                ItemStack itemStack = list.get(i);

                if (itemStack.isEmpty()) continue;
                /**System.out.println(list.get(i).getName().getString() + "| Is Juggernaut |" + ItemStackJuggernautModeAccessor.access(itemStack).getJuggernautMode());
                if (!ItemStackJuggernautModeAccessor.access(itemStack).getJuggernautMode())continue;**/
                list.set(i, ItemStack.EMPTY);
            }
        }
    }
    public static void markJuggernautItems(ItemStack itemStack) {

    }
    public boolean check() {
        return checkKnowledge() && checkMana();
    }

}
