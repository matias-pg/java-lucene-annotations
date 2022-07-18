package dev.matiaspg.luceneannotations.utils;

import java.util.Random;

import lombok.experimental.UtilityClass;

/**
 * Utility class to generate text.
 * Maybe I should have used a library for that lol, the text makes no sense.
 */
@UtilityClass
public class TextGenerator {
    private final Random random = new Random();

    private final String[] FRAMEWORKS = new String[] {
            "Spring",
            "NestJS",
            "Express",
            "Fastify",
            "Next.js",
            "Phoenix",
            "Symfony",
            "Laravel",
            "Django",
            "FastAPI"
    };

    private final String[] ADJECTIVES = new String[] {
            "best",
            "worst",
            "fastest",
            "slowest",
            "strongest",
            "weakest"
    };

    private final String[] PLACES = new String[] {
            "the planet",
            "the galaxy",
            "the universe",
            "the best country of Chile"
    };

    /**
     * Clickbait stuff
     */
    private final String[] FANCY_ENDINGS = new String[] {
            ". Period.",
            ". Just like that.",
            ", and... That's it.",
            ", here's why.",
            ", and I like it.",
            ", and you will love it.",
            ". You will not believe why."
    };

    public String generateTitle() {
        return pickRandom(FRAMEWORKS)
                + " is the " + pickRandom(ADJECTIVES)
                + " framework in the " + pickRandom(PLACES)
                + pickRandom(FANCY_ENDINGS);
    }

    private <T> T pickRandom(T[] array) {
        int index = random.nextInt(array.length);
        return array[index];
    }
}
