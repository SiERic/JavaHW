package me.sieric.myjunit.testClasses;

import me.sieric.myjunit.annotations.Test;

public class FailClass {
    @Test
    void test() throws Exception {
        throw new Exception("this class is loser");
    }
}
