package me.sieric.md5;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Class with methods for file and directory hashing.
 * Files and directories are hashed according to the following rules:
 * f(file) = MD5(*file content*)
 * f(dir) = MD5(*dir name* + f(file1) + ...)
 */
public class MD5FileHasher {

    private final static int SIZE = 1024;

    /**
     * Hashes given file in one thread
     * @param file file to hash
     * @return hash
     * @throws IOException if any IO exception will occur while reading files
     */
    public static byte[] getHash(@NotNull File file) throws IOException {
        if (file.isFile()) {
            return getFileHash(file);
        } else {
            return getDirectoryHash(file);
        }
    }

    /**
     * Hashes given file in parallel (using FJP)
     * @param file file to hash
     * @return hash
     * @throws IOException if any IO exception will occur while reading files
     */
    public static byte[] parallelGetHash(@NotNull File file) throws IOException {
        ForkJoinPool pool = new ForkJoinPool();
        ParallelHashTask task = new ParallelHashTask(file);
        pool.submit(task);
        byte[] result = task.compute();
        if (result == null) {
            throw new IOException();
        }
        return result;
    }

    private static byte[] getFileHash(@NotNull File file) throws IOException {
        InputStream stream = new BufferedInputStream(new FileInputStream(file));
        MessageDigest digest = createEmptyDigest();
        byte[] buffer = new byte[SIZE];
        while (stream.available() > 0) {
            int len = stream.read(buffer);
            digest.update(buffer, 0, len);
        }
        return digest.digest();
    }

    private static byte[] getDirectoryHash(@NotNull File dir) throws IOException {
        MessageDigest digest = createEmptyDigest();
        digest.update(dir.getName().getBytes());
        File[] listFiles = dir.listFiles();
        assert listFiles != null;

        for (File file : listFiles) {
            digest.update(getHash(file));
        }
        return digest.digest();
    }

    static MessageDigest createEmptyDigest() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) { }
        return digest;
    }

    private static class ParallelHashTask extends RecursiveTask<byte[]> {

        private final File file;

        private ParallelHashTask(@NotNull File file) {
            this.file = file;
        }

        @Override
        protected byte[] compute() {
            if (file.isFile()) {
                try {
                    return getFileHash(file);
                } catch (IOException ignored) {
                    return null;
                }
            } else {
                File[] listFiles = file.listFiles();
                List<ParallelHashTask> tasks = new ArrayList<>();

                assert listFiles != null;
                for (File file : listFiles) {
                    tasks.add(new ParallelHashTask(file));
                }
                Collection<ParallelHashTask> results = ForkJoinTask.invokeAll(tasks);

                MessageDigest digest = createEmptyDigest();
                digest.update(file.getName().getBytes());

                for (ParallelHashTask result : results) {
                    byte[] taskResult;
                    try {
                        taskResult = result.get();
                    } catch (Exception ignored) {
                        return null;
                    }
                    if (taskResult == null) {
                        return null;
                    }
                    digest.update(taskResult);
                }
                return digest.digest();
            }
        }
    }
}
