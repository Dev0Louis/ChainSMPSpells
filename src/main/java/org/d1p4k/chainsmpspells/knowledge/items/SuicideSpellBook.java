package org.d1p4k.chainsmpspells.knowledge.items;

import net.minecraft.util.Identifier;
import org.d1p4k.chainsmpspells.spell.spells.SuicideSpell;
import org.d1p4k.nebula.knowledge.items.AbstractSpellBook;

public class SuicideSpellBook extends AbstractSpellBook {
    public SuicideSpellBook(Settings settings) {
        super(settings);
    }

    @Override
    public Identifier getType() {
        return SuicideSpell.spellId;
    }
}
