package me.sieric.TestClasses;

import java.util.ArrayList;

public class ClassWithConstructors {
    int kek = 0;

    ClassWithConstructors(int kek, String heh, ArrayList<? extends Object> ooo) {}

    ClassWithConstructors() {}

    ClassWithConstructors(ClassWithConstructors lol) {}

    private class InnerClass {
        int kek = 0;
        InnerClass(int lol) {

        }
        private class InnerInnerClass {
            int kek;
            InnerInnerClass(int lol) {

            }
        }
    }

    private static class NestedClass {
        int kek;
        NestedClass(int lol) {

        }

        private class InnerNestedClass {
            int kek = 0;
            InnerNestedClass(int lol) {

            }
        }
    }

}
