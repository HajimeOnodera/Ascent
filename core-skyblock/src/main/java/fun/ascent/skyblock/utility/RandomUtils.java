package fun.ascent.skyblock.utility;

import java.util.Random;

public class RandomUtils {

    public static Random random = new Random();

    public static int getRandomInt(int bound){
        return random.nextInt(bound);
    }

    public static  int getRandomInt(int origin,int bound){
        return random.nextInt(origin,bound);
    }

}
