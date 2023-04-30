package dev.louis.chainsmpspells.spell;

import dev.louis.chainsmpspells.ChainSMPSpellsClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import dev.louis.nebula.spell.Spell;
import dev.louis.nebula.spell.SpellType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class TargetingSpell extends Spell {
    @Nullable
    private Entity castedOn;

    public TargetingSpell(SpellType<? extends Spell> spellType, PlayerEntity caster) {
        super(spellType, caster);
        if(caster.getWorld().isClient)ChainSMPSpellsClient.getPlayerInView().ifPresent(this::castedOn);
    }

    @Override
    public boolean isCastable() {
        return castedOn() != null && super.isCastable();
    }

    public Entity castedOn(Entity castedOn) {
        this.castedOn = castedOn;
        return castedOn;
    }
    public Entity castedOn() {
        return castedOn;
    }

    @Override
    public PacketByteBuf writeBuf(PacketByteBuf buf) {
        super.writeBuf(buf);
        Optional<Integer> optionalInteger = castedOn() != null ? Optional.of(castedOn().getId()) : Optional.empty();
        buf.writeOptional(optionalInteger, (PacketByteBuf::writeVarInt));
        return buf;
    }

    @Override
    public PacketByteBuf readBuf(PacketByteBuf buf) {
        super.readBuf(buf);
        Optional<Integer> o = buf.readOptional(PacketByteBuf::readVarInt);
        o.ifPresent((integer -> castedOn(getCaster().getWorld().getEntityById(integer))));
        return buf;
    }
}
