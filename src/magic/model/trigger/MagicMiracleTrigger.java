package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicLocationType;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;
import magic.model.action.AIRevealAction;
import magic.model.action.CastCardAction;

public class MagicMiracleTrigger extends MagicWhenDrawnTrigger {

    private final MagicManaCost cost;

    public MagicMiracleTrigger(final MagicManaCost cost) {
        this.cost = cost;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent none,final MagicCard card) {
        return card.getOwner().getDrawnCards() == 1 ?
            new MagicEvent(
                card,
                card.getOwner(),
                new MagicMayChoice(
                    new MagicPayManaCostChoice(cost)
                ),
                this,
                "You may$ reveal this card and cast it for its miracle cost."
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        if (event.isYes() && card.isInHand()) {
            game.doAction(new AIRevealAction(card));
            game.doAction(CastCardAction.WithoutManaCost(
                event.getPlayer(),
                card,
                MagicLocationType.OwnersHand,
                MagicLocationType.Graveyard
            ));
        }
    }
}
