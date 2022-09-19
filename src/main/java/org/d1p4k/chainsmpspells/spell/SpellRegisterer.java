package org.d1p4k.chainsmpspells.spell;

import net.minecraft.util.Identifier;
import org.d1p4k.chainsmpspells.spell.spells.ArrowSpell;
import org.d1p4k.chainsmpspells.spell.spells.SuicideSpell;
import org.d1p4k.chainsmpspells.spell.spells.TeleportSpell;
import org.d1p4k.nebula.api.NebulaSpellRegisterEntrypoint;
import org.d1p4k.nebula.knowledge.SpellKnowledge;

import java.util.ArrayList;
import java.util.Collection;

public class SpellRegisterer implements NebulaSpellRegisterEntrypoint {
    @Override
    public void registerSpells() {
        Collection<Identifier> spells = new ArrayList<>() {
            {
                add(SuicideSpell.spellId);
                add(TeleportSpell.spellId);
                add(ArrowSpell.spellId);
            }
        };
        SpellKnowledge.Registry.addAll(spells);
        System.out.println(SpellKnowledge.Registry.getRegisteredSpells());

    }
}
