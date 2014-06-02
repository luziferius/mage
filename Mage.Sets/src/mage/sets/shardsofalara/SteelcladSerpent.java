/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.shardsofalara;

import java.util.UUID;

import mage.constants.CardType;
import mage.constants.Rarity;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.RestrictionEffect;
import mage.cards.CardImpl;
import mage.constants.Duration;
import mage.constants.Zone;
import mage.filter.common.FilterControlledPermanent;
import mage.filter.predicate.mageobject.CardTypePredicate;
import mage.filter.predicate.permanent.AnotherPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;

/**
 *
 * @author Plopman
 */
public class SteelcladSerpent extends CardImpl {

    public SteelcladSerpent(UUID ownerId) {
        super(ownerId, 59, "Steelclad Serpent", Rarity.COMMON, new CardType[]{CardType.ARTIFACT, CardType.CREATURE}, "{5}{U}");
        this.expansionSetCode = "ALA";
        this.subtype.add("Serpent");

        this.color.setBlue(true);
        this.power = new MageInt(4);
        this.toughness = new MageInt(5);

        // Steelclad Serpent can't attack unless you control another artifact.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new SteelcladSerpentEffect()));
    }

    public SteelcladSerpent(final SteelcladSerpent card) {
        super(card);
    }

    @Override
    public SteelcladSerpent copy() {
        return new SteelcladSerpent(this);
    }
}

class SteelcladSerpentEffect extends RestrictionEffect {

    private static final FilterControlledPermanent filter = new FilterControlledPermanent("another artifact");
    
    static {
        filter.add(new CardTypePredicate(CardType.ARTIFACT));
        filter.add(new AnotherPredicate());
    }

    public SteelcladSerpentEffect() {
        super(Duration.WhileOnBattlefield);
        staticText = "{this} can't attack unless you control another artifact";
    }

    public SteelcladSerpentEffect(final SteelcladSerpentEffect effect) {
        super(effect);
    }

    @Override
    public SteelcladSerpentEffect copy() {
        return new SteelcladSerpentEffect(this);
    }

    @Override
    public boolean canAttack(Game game) {
        return false;
    }

    @Override
    public boolean applies(Permanent permanent, Ability source, Game game) {
        if (permanent.getId().equals(source.getSourceId())) {
            if (game.getBattlefield().getActivePermanents(filter, source.getControllerId(), permanent.getId(), game).size() > 0) {
                return false;
            }
            return true;
        } 
        return false;
    }
}