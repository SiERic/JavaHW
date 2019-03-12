package me.sieric.TestClasses;

public class ClassWithInnerClasses {

    private class A {
        int kek = 0;
        private A() {

        }
    }

    public static class B {
        static int kek() {
            return 1;
        }


        static class BB {
            static int kek = 1234455;
        }
    }

    public interface C {
        public final int keker = 123;
        public Object kek();
    }

}
