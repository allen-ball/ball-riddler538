/*
 * $Id$
 *
 * Copyright 2016 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.riddler538.ant.taskdefs;

import ball.game.card.Card;
import ball.game.card.poker.Deck;
import ball.game.card.poker.Evaluator;
import ball.game.card.poker.Ranking;
import ball.swing.table.SimpleTableModel;
import ball.util.ant.taskdefs.AntTask;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.TreeMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.tools.ant.BuildException;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to solve
 * {@link.uri https://fivethirtyeight.com/features/hark-two-holiday-puzzles/ Riddler Express}
 * <p>
 * From Jeffrey Hope, open up your junk drawer and pull out those decks of
 * cards:
 * </p>
 * <p>
 * Your challenge is to take any 50 cards from a standard 52-card deck and
 * arrange them into 10 poker hands, one of each type from a royal flush
 * down to a lowly high card.2 The hands you build always rank as the
 * highest type of hand possible,3 and a card may not be reused across the
 * hands. (This means, for example, that a four-of-a-kind better than four
 * nines is impossible because it’d use a card you’d need to make a royal
 * flush.) As in actual poker, it doesn’t matter what order the cards are
 * arranged within a hand.
 * </p>
 * <p>
 * Sounds easy enough, and indeed there is more than one solution. But:
 * Exactly how many solutions are there?
 * </p>
 *
 * {@ant.task}
 *
<pre>
Ranking       Hand
----------------------------------------
RoyalFlush    [A-♤, K-♤, Q-♤, J-♤, 10-♤]
StraightFlush [K-♡, Q-♡, J-♡, 10-♡, 9-♡]
FourOfAKind   [8-♤, 8-♡, 8-♢, 8-♧, J-♧]
FullHouse     [A-♡, A-♢, A-♧, K-♢, K-♧]
Flush         [Q-♢, J-♢, 10-♢, 9-♢, 7-♢]
Straight      [7-♤, 6-♤, 5-♤, 4-♤, 3-♤]
ThreeOfAKind  [6-♡, 6-♢, 6-♧, 10-♧, 5-♧]
TwoPair       [9-♤, 9-♧, 7-♡, 7-♧, 4-♡]
Pair          [5-♡, 5-♢, 4-♢, 4-♧, 3-♡]
HighCard      [Q-♧, 3-♢, 3-♧, 2-♤, 2-♡]
Remaining     [2-♢, 2-♧]
</pre>
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@AntTask("solve-express-2017-12-22")
@NoArgsConstructor @ToString
public class SolveExpress20171222Task extends AbstractTask {
    @Override
    public void execute() throws BuildException {
        super.execute();

        try {
            SimpleTableModel model =
                new SimpleTableModel(new Object[][] { },
                                     "Ranking", "Hand");
            TreeMap<Ranking,List<Card>> map = new TreeMap<>();
            List<Card> deck = new Deck();

            deck.sort(Evaluator.CARD);
            Collections.reverse(deck);

            List<Ranking> rankings = Arrays.asList(Ranking.values());

            Collections.reverse(rankings);

            for (Ranking ranking : rankings) {
                List<Card> hand = ranking.find(deck);

                if (! hand.isEmpty()) {
                    hand = hand.subList(0, ranking.required());

                    map.put(ranking, hand);

                    deck.removeAll(hand);
                }
            }

            for (Ranking ranking : rankings) {
                List<Card> hand = map.get(ranking);

                if (hand != null) {
                    if (hand.size() < 5) {
                        hand = new LinkedList<>(hand);

                        List<Card> draw = deck.subList(0, 5 - hand.size());

                        hand.addAll(draw);
                        draw.clear();
                    }

                    model.row(ranking, hand);
                }
            }

            model.row("Remaining", deck);

            log(model);
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }
}
