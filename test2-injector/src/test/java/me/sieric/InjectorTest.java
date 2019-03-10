package me.sieric;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {

    @Test
    void testInjectorShouldInitializeClassWithoutDependencies() throws Exception {
        Object object = Injector.initialize("me.sieric.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    void testInjectorShouldInitializeClassWithOneClassDependency() throws Exception {
        Object object = Injector.initialize(
                "me.sieric.ClassWithOneClassDependency",
                singletonList(me.sieric.ClassWithoutDependencies.class)
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertNotNull(instance.dependency);
    }

    @Test
    void testInjectorThrowsExceptions() {
        List<Class<?>> list = new ArrayList<>();
        list.add(me.sieric.ClassWithCycle1.class);
        list.add(me.sieric.ClassWithCycle2.class);
        assertThrows(InjectionCycleException.class, () -> Injector.initialize("me.sieric.ClassWithCycle1", list));

        assertThrows(ImplementationNotFoundException.class, () -> Injector.initialize("me.sieric.ClassWithOneClassDependency", Collections.emptyList()));
    }

}