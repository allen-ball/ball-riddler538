/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.riddler538.ant.taskdefs;

import ball.swing.table.SimpleTableModel;
import ball.util.ant.taskdefs.AntTask;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.tools.ant.BuildException;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to solve a
 * {@link.uri https://fivethirtyeight.com/features/can-you-solve-the-vexing-vexillology/ Riddler Classic}:
 *
 *The New York Times recently launched some new word puzzles, one of which is Spelling Bee. In this game, seven letters are arranged in a honeycomb lattice, with one letter in the center. Here’s the lattice from December 24, 2019:
 *
 * Spelling Bee screenshot, with the required letter G, and the additional
 * letters L, A, P, X, M and E.
 * The goal is to identify as many words that meet the following criteria:
 *
 * The word must be at least four letters long.
 * The word must include the central letter.
 * The word cannot include any letter beyond the seven given letters.
 * Note that letters can be repeated. For example, the words GAME and
 * AMALGAM are both acceptable words. Four-letter words are worth 1 point
 * each, while five-letter words are worth 5 points, six-letter words are
 * worth 6 points, seven-letter words are worth 7 points, etc. Words that
 * use all of the seven letters in the honeycomb are known as “pangrams” and
 * earn 7 bonus points (in addition to the points for the length of the
 * word). So in the above example, MEGAPLEX is worth 15 points.
 *
 * Which seven-letter honeycomb results in the highest possible game score?
 * To be a valid choice of seven letters, no letter can be repeated, it must
 * not contain the letter S (that would be too easy) and there must be at
 * least one pangram.
 *
 * For consistency, please use this word list to check your game score.
 *
 * {@ant.task}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@AntTask("solve-riddle-2020-01-03")
@NoArgsConstructor @ToString
public class SolveRiddle20200103Task extends AbstractTask {
    private static final Character S = new Character('S');

    @Override
    public void execute() throws BuildException {
        super.execute();

        try {
            URI uri = getClass().getResource("enable1.txt").toURI();
            List<String> list =
                Files.lines(Paths.get(uri))
                .filter(t -> isNotBlank(t))
                .map(t -> t.trim())
                .filter(t -> (! t.startsWith("#")))
                .map(t -> t.toUpperCase())
                .collect(toList());
            Set<String> words =
                list.stream()
                .filter(t -> t.indexOf(S) == -1)
                .filter(t -> score(t) > 0)
                .filter(t -> toCharacterSet(t).size() <= 7)
                .collect(toSet());
            Set<Set<Character>> sets =
                words.stream()
                .map(t -> toCharacterSet(t))
                .filter(t -> t.size() == 7)
                .collect(toSet());
            Map<Set<Character>,Set<String>> map =
                new TreeMap<>(Comparator.comparing(Object::toString));

            for (Set<Character> key : sets) {
                Set<String> value =
                    words.stream()
                    .filter(t -> key.containsAll(toCharacterSet(t)))
                    .collect(toSet());

                map.put(key, value);
            }

            Map<List<Character>,Set<String>> solutions =
                new TreeMap<>(Comparator.comparing(Object::toString));
            Map<List<Character>,Integer> scores =
                new TreeMap<>(Comparator.comparing(Object::toString));

            for (Map.Entry<Set<Character>,Set<String>> entry :
                     map.entrySet()) {
                for (int i = 0, n = entry.getKey().size(); i < n; i += 1) {
                    List<Character> key =
                        entry.getKey().stream().collect(toList());

                    key.add(0, key.remove(i));

                    Set<String> value =
                        entry.getValue()
                        .stream()
                        .filter(t -> t.indexOf(key.get(0)) >= 0)
                        .collect(toSet());

                    solutions.put(key, value);
                    scores.put(key, score(value));
                }
            }

            int maxScore =
                scores.values()
                .stream()
                .mapToInt(Integer::intValue)
                .max().getAsInt();

            log(new SimpleTableModel(new Object[][] { }, 2)
                .row("List:", list.size())
                .row("Words:", words.size())
                .row("Honeycomb Sets:", sets.size())
                .row("Solutions:", solutions.size())
                .row("Max Score:", maxScore));

            for (List<Character> key : scores.keySet()) {
                if (scores.get(key).intValue() == maxScore) {
                    log("");
                    log(String.valueOf(key));

                    List<String> solution =
                        solutions.get(key).stream().collect(toList());

                    solution.sort(Comparator
                                  .<String>comparingInt(t -> (- score(t)))
                                  .thenComparing(Comparator.naturalOrder()));

                    log(String.valueOf(solution.size()));

                    for (String string : solution) {
                        log(string + "\t" + score(string));
                    }
                }
            }
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }

    private int score(String string) {
        int score = 0;

        switch (string.length()) {
        case 0:
        case 1:
        case 2:
        case 3:
            score = 0;
            break;

        case 4:
            score = 1;
            break;

        default:
            score = string.length();

            if (string.length() >= 7) {
                if (toCharacterSet(string).size() == 7) {
                    score += 7;
                }
            }
            break;
        }

        return score;
    }

    private int score(Collection<String> collection) {
        return collection.stream().mapToInt(t -> score(t)).sum();
    }

    private List<Character> toCharacterList(String string) {
        return (string.chars()
                .mapToObj(c -> Character.valueOf((char) c))
                .collect(toList()));
    }

    private Set<Character> toCharacterSet(String string) {
        return toCharacterList(string).stream().collect(toSet());
    }
}
