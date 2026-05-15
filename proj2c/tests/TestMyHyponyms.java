import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import main.AutograderBuddy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class TestMyHyponyms {
    public static final String WORDS_FILE = "data/ngrams/very_short.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SYN16 = "data/wordnet/synsets16.txt";
    public static final String HYP16 = "data/wordnet/hyponyms16.txt";
    public static final String SYN11 = "data/wordnet/synsets11.txt";
    public static final String HYP11 = "data/wordnet/hyponyms11.txt";

    @Test
    public void testChangeOn16() {
        NgordnetQueryHandler h = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SYN16, HYP16);

        String actual = h.handle(new NgordnetQuery(
                List.of("change"), 0, 0, 0, NgordnetQueryType.HYPONYMS));

        String expected = "[alteration, change, demotion, increase, jump, leap, modification, saltation, transition, variation]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testTransitionOn16() {
        NgordnetQueryHandler h = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SYN16, HYP16);

        String actual = h.handle(new NgordnetQuery(
                List.of("transition"), 0, 0, 0, NgordnetQueryType.HYPONYMS));

        String expected = "[flashback, jump, leap, saltation, transition]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testChangeAndAdjustmentOn16() {
        NgordnetQueryHandler h = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SYN16, HYP16);

        String actual = h.handle(new NgordnetQuery(
                List.of("change", "adjustment"), 0, 0, 0, NgordnetQueryType.HYPONYMS));

        String expected = "[alteration, modification]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testActionAndFlashbackOn16() {
        NgordnetQueryHandler h = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SYN16, HYP16);

        String actual = h.handle(new NgordnetQuery(
                List.of("action", "flashback"), 0, 0, 0, NgordnetQueryType.HYPONYMS));

        String expected = "[]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testAntihistamineAndNasalDecongestantOn11() {
        NgordnetQueryHandler h = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SYN11, HYP11);

        String actual = h.handle(new NgordnetQuery(
                List.of("antihistamine", "nasal_decongestant"), 0, 0, 0, NgordnetQueryType.HYPONYMS));

        String expected = "[actifed]";
        assertThat(actual).isEqualTo(expected);
    }
}
