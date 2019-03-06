package me.sieric;

public class ClassWithCycle1 {
    public final ClassWithCycle2 dependency;
    public ClassWithCycle1(ClassWithCycle2 dependency) {
        this.dependency = dependency;
    }
}
