package me.sieric.md5;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MD5FileHasherTest {

    private int BIG_SIZE = 1048576;

    private String getFilePath(String name) {
        return Paths.get("src", "test", "resources", name).toString();
    }

    private byte[] hashBytes(byte[] bytes) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {}

        digest.update(bytes);
        return digest.digest();
    }

    private byte[] hashString(String string) {
        return hashBytes(string.getBytes());
    }

    private byte[] getFileHash(File file) throws IOException {
        InputStream stream = new BufferedInputStream(new FileInputStream(file));
        MessageDigest digest = MD5FileHasher.createEmptyDigest();
        byte[] buffer = new byte[BIG_SIZE];
        while (stream.available() > 0) {
            int len = stream.read(buffer);
            digest.update(buffer, 0, len);
        }
        return digest.digest();
    }

    @Test
    void hashEmptyFile() throws IOException {
        File file = new File(getFilePath("empty"));
        assertEquals(Arrays.toString(getFileHash(file)), Arrays.toString(MD5FileHasher.getHash(file)));
    }

    @Test
    void hashEmptyDirectory() throws IOException {
        File file = new File(getFilePath("emptyDirectory"));
        assertEquals(Arrays.toString(hashString("emptyDirectory")), Arrays.toString(MD5FileHasher.getHash(file)));
    }

    @Test
    void hashBigFile() throws IOException {
        File file = new File(getFilePath("big"));
        assertEquals(Arrays.toString(getFileHash(file)), Arrays.toString(MD5FileHasher.getHash(file)));
    }

    @Test
    void hashEmptyFileParallel() throws IOException {
        File file = new File(getFilePath("empty"));
        assertEquals(Arrays.toString(getFileHash(file)), Arrays.toString(MD5FileHasher.parallelGetHash(file)));
    }

    @Test
    void hashEmptyDirectoryParallel() throws IOException {
        File file = new File(getFilePath("emptyDirectory"));
        assertEquals(Arrays.toString(hashString("emptyDirectory")), Arrays.toString(MD5FileHasher.parallelGetHash(file)));
    }

    @Test
    void hashBigFileParallel() throws IOException {
        File file = new File(getFilePath("big"));
        assertEquals(Arrays.toString(getFileHash(file)), Arrays.toString(MD5FileHasher.parallelGetHash(file)));
    }

    @Test
    public void hashDirWithEmptyFile() throws IOException {
        String dirName = getFilePath("dir");
        String filename = Paths.get(dirName, "empty").toString();

        File dir = new File(dirName);

        byte[] arr1 = dir.getName().getBytes();
        byte[] arr2 = getFileHash(new File(filename));
        byte[] arr3 = new byte[arr1.length + arr2.length];

        System.arraycopy(arr1, 0, arr3, 0, arr1.length);
        System.arraycopy(arr2, 0, arr3, arr1.length, arr2.length);

        assertEquals(
                Arrays.toString(hashBytes(arr3)),
                Arrays.toString(MD5FileHasher.getHash(dir))
        );
    }

    @Test
    public void hashDirWithEmptyFileParallel() throws IOException {
        String dirName = getFilePath("dir");
        String filename = Paths.get(dirName, "empty").toString();

        File dir = new File(dirName);

        byte[] arr1 = dir.getName().getBytes();
        byte[] arr2 = getFileHash(new File(filename));
        byte[] arr3 = new byte[arr1.length + arr2.length];

        System.arraycopy(arr1, 0, arr3, 0, arr1.length);
        System.arraycopy(arr2, 0, arr3, arr1.length, arr2.length);

        assertEquals(
                Arrays.toString(hashBytes(arr3)),
                Arrays.toString(MD5FileHasher.parallelGetHash(dir))
        );
    }

}