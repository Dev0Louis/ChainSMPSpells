package org.d1p4k.chainsmpspells.spell;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.d1p4k.chainsmpspells.spell.spells.*;
import org.d1p4k.nebula.Nebula;
import org.d1p4k.nebula.knowledge.SpellKnowledge;
import org.d1p4k.nebula.registry.NebulaRegistries;

import java.util.ArrayList;
import java.util.Collection;

public class SpellRegisterer {
    public void registerSpells() {
        Collection<Identifier> spells = new ArrayList<>() {
            {
                add(ArrowSpell.spellId);
                add(PullSpell.spellId);
                add(PushSpell.spellId);
                add(RewindSpell.spellId);
                add(JuggernautSpell.spellId);
            }
        };

        Registry.register(NebulaRegistries.SPELLS, new Identifier("chainsmpspells", "suicide"), new SuicideSpell());
        Registry.register(NebulaRegistries.SPELLS, new Identifier("chainsmpspells", "teleport"), new TeleportSpell());
        //Registry.register(NebulaRegistries.SPELLS, new Identifier("chainsmpspells", "arrow"), new ArrowSpell());


    }
}
