package org.d1p4k.chainsmpspells.spell.spells;

import com.google.common.collect.ImmutableList;
import net.minecraft.enchantment.Enchantment;
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
                if (ItemStackJuggernautModeAccessor.access(itemStack).getJuggernautTick() <= 0L)continue;
                list.set(i, ItemStack.EMPTY);
            }
        }
    }
    public boolean check() {
        return checkKnowledge() && checkMana();
    }

}
