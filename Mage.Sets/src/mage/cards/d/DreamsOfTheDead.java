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
package mage.cards.d;

import java.util.UUID;
import mage.ObjectColor;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.ContinuousEffect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.ReplacementEffectImpl;
import mage.abilities.effects.common.continuous.GainAbilityTargetEffect;
import mage.abilities.keyword.CumulativeUpkeepAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.common.FilterCreatureCard;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.ColorPredicate;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.ZoneChangeEvent;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetCardInYourGraveyard;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author LevelX2 & L_J
 */
public final class DreamsOfTheDead extends CardImpl {

    private static final FilterCreatureCard filter = new FilterCreatureCard("white or black creature card");

    static {
        filter.add(Predicates.or(
                new ColorPredicate(ObjectColor.WHITE),
                new ColorPredicate(ObjectColor.BLACK)));
    }

    public DreamsOfTheDead(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{3}{U}");

        // {1}{U}: Return target white or black creature card from your graveyard to the battlefield. That creature gains "Cumulative upkeep {2}." If the creature would leave the battlefield, exile it instead of putting it anywhere else.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new DreamsOfTheDeadEffect(), new ManaCostsImpl("{1}{U}"));
        ability.addTarget(new TargetCardInYourGraveyard(filter));
        ability.addEffect(new DreamsOfTheDeadReplacementEffect());
        this.addAbility(ability);
    }

    public DreamsOfTheDead(final DreamsOfTheDead card) {
        super(card);
    }

    @Override
    public DreamsOfTheDead copy() {
        return new DreamsOfTheDead(this);
    }
}

class DreamsOfTheDeadEffect extends OneShotEffect {

    public DreamsOfTheDeadEffect() {
        super(Outcome.PutCreatureInPlay);
        this.staticText = "Return target white or black creature card from your graveyard to the battlefield. That creature gains \"Cumulative upkeep {2}.\"";
    }

    public DreamsOfTheDeadEffect(final DreamsOfTheDeadEffect effect) {
        super(effect);
    }

    @Override
    public DreamsOfTheDeadEffect copy() {
        return new DreamsOfTheDeadEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Card card = game.getCard(this.getTargetPointer().getFirst(game, source));
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null && card != null) {
            if (controller.moveCards(card, Zone.BATTLEFIELD, source, game)) {
                Permanent creature = game.getPermanent(card.getId());
                if (creature != null) {
                    ContinuousEffect effect = new GainAbilityTargetEffect(new CumulativeUpkeepAbility(new ManaCostsImpl("{2}")), Duration.Custom);
                    effect.setTargetPointer(new FixedTarget(creature, game));
                    game.addEffect(effect, source);
                }
            }
            return true;
        }
        return false;
    }
}

class DreamsOfTheDeadReplacementEffect extends ReplacementEffectImpl {

    DreamsOfTheDeadReplacementEffect() {
        super(Duration.OneUse, Outcome.Tap);
        staticText = "If the creature would leave the battlefield, exile it instead of putting it anywhere else";
    }

    DreamsOfTheDeadReplacementEffect(final DreamsOfTheDeadReplacementEffect effect) {
        super(effect);
    }

    @Override
    public DreamsOfTheDeadReplacementEffect copy() {
        return new DreamsOfTheDeadReplacementEffect(this);
    }

    @Override
    public boolean replaceEvent(GameEvent event, Ability source, Game game) {
        ((ZoneChangeEvent) event).setToZone(Zone.EXILED);
        return false;
    }

    @Override
    public boolean checksEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.ZONE_CHANGE;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        if (event.getTargetId().equals(source.getFirstTarget())
                && ((ZoneChangeEvent) event).getFromZone() == Zone.BATTLEFIELD
                && ((ZoneChangeEvent) event).getToZone() != Zone.EXILED) {
            return true;
        }
        return false;
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return false;
    }
}