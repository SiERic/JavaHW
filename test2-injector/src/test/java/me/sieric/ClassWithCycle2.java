package me.sieric;

public class ClassWithCycle2 {
    public final ClassWithCycle1 dependency;
    public ClassWithCycle2(ClassWithCycle1 dependency) {
        this.dependency = dependency;
    }
}
