package dev.louis.chainsmpspells.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import dev.louis.chainsmpspells.accessor.ItemStackJuggernautModeAccessor;
import dev.louis.chainsmpspells.accessor.ServerPlayerEntityJuggernautModeAccessor;
import dev.louis.chainsmpspells.spell.JuggernautSpell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.louis.chainsmpspells.spell.JuggernautSpell.generateJuggernautItem;
import static dev.louis.chainsmpspells.spell.JuggernautSpell.generateJuggernautItemAndSetToSlot;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityJuggernautSpellMixin implements ServerPlayerEntityJuggernautModeAccessor  {

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    int juggernautTick = 0;
    boolean isJuggernaut = false;


    private void endJuggernaut(boolean kill) {
        JuggernautSpell.clearJuggernautItems(getPlayer());
        juggernautTick = 0;
        isJuggernaut = false;
        if(kill) {
            getPlayer().damage(getPlayer().getDamageSources().magic(), 100f);
            getPlayer().setHealth(0);
        }
    }
    @Inject(method = "onDeath", at = @At("RETURN"))
    public void removeJuggernautModeOnDeath(DamageSource damageSource, CallbackInfo ci) {
        endJuggernaut(false);
    }

    @Inject(method = "copyFrom", at = @At("RETURN"))
    public void copyJuggernautMode(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if(alive) {
            this.setJuggernautModeTick(ServerPlayerEntityJuggernautModeAccessor.access(oldPlayer).getJuggernautTick());
        }
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void removeJuggernautItemsOnLeave(CallbackInfo ci) {
        if(isInJuggernautMode()) {
            endJuggernaut(true);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void decrementJuggernautTick(CallbackInfo ci) {
        if(isInJuggernautMode()) {
            if(juggernautTick < 0){
                endJuggernaut(true);
                return;
            }
            if(juggernautTick < 200) {
                if(juggernautTick % 40 == 0) {
                    sendSound();
                }
            }
            if(juggernautTick < 50) {
                if(juggernautTick % 5 == 0) {
                    sendSound();
                }
            }
            juggernautTick--;
        }
    }


    public void makeJuggernaut(int ticks) {
        getPlayer().getInventory().dropAll();
        juggernautTick = ticks;
        isJuggernaut = true;

        var tick = ((ServerWorldAccessor)getPlayer().getWorld()).getWorldProperties().getTime();
        var player = getPlayer();
        generateJuggernautItemAndSetToSlot(player, 0, Items.NETHERITE_SWORD, tick);
        generateJuggernautItemAndSetToSlot(player, 1, Items.NETHERITE_AXE, tick);
        generateJuggernautItemAndSetToSlot(player, 2, Items.BOW, tick);

        ItemStack golden_apple = generateJuggernautItem(Items.GOLDEN_APPLE, tick);
        golden_apple.setCount(26);
        player.getInventory().setStack(3, golden_apple);


        ItemStack arrow = generateJuggernautItem(Items.ARROW, tick);
        arrow.setCount(1);
        getPlayer().getInventory().setStack(10, arrow);

        player.getInventory().armor.set(0, generateJuggernautItem(Items.NETHERITE_BOOTS, tick));
        player.getInventory().armor.set(1, generateJuggernautItem(Items.NETHERITE_LEGGINGS, tick));
        player.getInventory().armor.set(2, generateJuggernautItem(Items.NETHERITE_CHESTPLATE, tick));
        player.getInventory().armor.set(3, generateJuggernautItem(Items.NETHERITE_HELMET, tick));
    }




    @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
    public void noJuggernautItemDrop(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        //TODO: Keep Item in Inventory of Player.
        if(ItemStackJuggernautModeAccessor.access(stack).isJuggernautItem()) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }

    public void sendActionBarUpdate(int time) {
        this.sendMessage(Text.translatable("message.cts.cooldown", time), true);
    }

    private void sendSound() {
        getPlayer().getWorld().playSound(null, getPlayer().getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), SoundCategory.PLAYERS);
    }


    @Override
    public void setJuggernautModeTick(int ticks) {
        juggernautTick = ticks;
    }

    @Override
    public int getJuggernautTick() {
        return juggernautTick;
    }


    @Override
    public boolean isInJuggernautMode() {
        return isJuggernaut;
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
