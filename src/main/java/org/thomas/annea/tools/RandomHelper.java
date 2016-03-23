package org.thomas.annea.tools;

import java.util.Random;

public class RandomHelper {

    /**
     * Random number between two numbers
     * @param min Min number
     * @param max Max number
     * @return Random int btweetn min and max
     */

    public static int randint(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    /**
     * Calculates probability in percentage
     * @param percent Percent
     * @return If it occurs of not
     */

    public static boolean randomInPercentage(int percent) {
        if (percent == 100) {
            return true;
        }

        return RandomHelper.randint(0, 101) <= percent;
    }

    /**
     * Shortcut for Math.random(), because lazy
     * @return Random double between 0 and 1
     */

    public static double rand() {
        return Math.random();
    }
}
