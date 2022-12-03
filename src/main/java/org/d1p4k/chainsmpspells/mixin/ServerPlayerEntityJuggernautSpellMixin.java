package org.d1p4k.chainsmpspells.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.d1p4k.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
import org.d1p4k.chainsmpspells.accessor.ServerPlayerEntityJuggernautModeAccessor;
import org.d1p4k.chainsmpspells.packet.s2c.SpellS2CPacket;
import org.d1p4k.chainsmpspells.spell.spells.JuggernautSpell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityJuggernautSpellMixin implements ServerPlayerEntityJuggernautModeAccessor  {
    @Shadow public abstract boolean damage(DamageSource source, float amount);

    long juggernautTick = 0L;
    @Inject(method = "onDeath", at = @At("RETURN"))
    public void removeJuggernautModeOnDeath(DamageSource damageSource, CallbackInfo ci) {
        JuggernautSpell.clearJuggernautItems(this.getPlayer());
        juggernautTick = 0L;
    }

    @Inject(method = "copyFrom", at = @At("RETURN"))
    public void copyJuggernautMode(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if(alive) {
            this.setJuggernautModeTick(ServerPlayerEntityJuggernautModeAccessor.access(oldPlayer).getJuggernautTick());
        }
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void removeJuggernautItemsOnLeave(CallbackInfo ci) {
        JuggernautSpell.clearJuggernautItems(this.getPlayer());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void decrementJuggernautTick(CallbackInfo ci) {
        long second = juggernautTick % 20;
        if(juggernautTick > 0) {
            if(second == 0) {
                System.out.println(juggernautTick + " | " + second);
                ServerPlayerEntity player = getPlayer();
                sendPacket(Math.round(juggernautTick / 20f));
                if(ServerPlayerEntityJuggernautModeAccessor.access(player).isNotInJuggernautMode()) {
                    clearJuggernautItems();
                    juggernautTick = 0;
                    return;
                }
            }
            juggernautTick--;

        }


    }

    public void sendPacket(int count) {
        var packet = new SpellS2CPacket(getPlayer());
        //TODO: Write better serialiser and deserialiser.
        packet.write((byte) 7);
        packet.getBuf().writeInt(count);
        packet.send();
    }


    private void clearJuggernautItems() {
        ServerPlayerEntity player = getPlayer();
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


    @Override
    public void setJuggernautModeTick(long ticks) {
        juggernautTick = ticks;
    }

    @Override
    public long getJuggernautTick() {
        return juggernautTick;
    }


    @Override
    public boolean isInJuggernautMode() {
        return (juggernautTick > 0L);
    }
    @Override
    public boolean isNotInJuggernautMode() {
        return !isInJuggernautMode();
    }
    @Override
    public ServerPlayerEntity getPlayer() {
        return (ServerPlayerEntity) (Object) this;
    }
}
