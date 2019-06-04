package me.sieric.md5;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Console application to hash files/directories using MD5
 * Calculates hash and time of single/many thread execution
 */
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Should be a simle path in arguments");
            return;
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("File/directory does not exist");
            return;
        }
        byte[] simleResult;
        byte[] parallelResult;

        long simleStartTime = System.currentTimeMillis();
        try {
            simleResult = MD5FileHasher.getHash(file);
        } catch (IOException e) {
            System.out.println("IO error occurred during execution");
            return;
        }
        long simleFinishTime = System.currentTimeMillis();


        long parallelStartTime = System.currentTimeMillis();
        try {
            parallelResult = MD5FileHasher.parallelGetHash(file);
        } catch (IOException e) {
            System.out.println("IO error occurred during execution");
            return;
        }
        if (parallelResult == null) {
            System.out.println("IO error occurred during execution");
            return;
        }
        long parallelFinishTime = System.currentTimeMillis();

        System.out.println("Simple algorithm result:" + Arrays.toString(simleResult));
        System.out.println("Simple algorithm time execution:" + (int)(simleFinishTime - simleStartTime) + " milliseconds");

        System.out.println("Parallel algorithm time execution:" + (int)(parallelFinishTime - parallelStartTime) + " milliseconds");
        System.out.println("Parallel algorithm result:" + Arrays.toString(simleResult));
    }
}
