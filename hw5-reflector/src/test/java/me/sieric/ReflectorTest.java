package me.sieric;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import static me.sieric.Reflector.printStructure;

class ReflectorTest {

    private Class<?> compile(String name) throws ClassNotFoundException, MalformedURLException {
        File root = new File("");
        File sourceFile = new File(name + ".java");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, sourceFile.getPath());

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
        return Class.forName(name, true, classLoader);
    }

    private void deleteClass(String className) {
        var file = new File(className + ".class");
        file.delete();
    }

    @Test
    void testCompileBigInterestingClass() {
         assertAll(() -> printStructure(me.sieric.TestClasses.Kek.class));
         assertAll(() -> compile("Kek"));
         deleteClass("Kek");
    }

    @Test
    void testDiffEmptyClass() throws MalformedURLException, ClassNotFoundException {
        assertAll(() -> printStructure(me.sieric.TestClasses.EmptyClass.class));
        Class<?> emptyClass = compile("EmptyClass");

        ByteArrayOutputStream baOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baOut);
        System.setOut(out);

        assertAll(() -> Reflector.diffClasses(me.sieric.TestClasses.EmptyClass.class, emptyClass));
        assertEquals("Classes are equal!!!!\n", baOut.toString());

        deleteClass("EmptyClass");
    }

    @Test
    void testDiffDifferentClasses() throws MalformedURLException, ClassNotFoundException {
        assertAll(() -> printStructure(me.sieric.TestClasses.ClassWithOneField.class));
        Class<?> classWithOneField = compile("ClassWithOneField");

        ByteArrayOutputStream baOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baOut);
        System.setOut(out);

        assertAll(() -> Reflector.diffClasses(me.sieric.TestClasses.EmptyClass.class, classWithOneField));

        assertEquals(
        "Classes are different!!!!\n"
            + "-------------------------------------\n"
            + "ClassWithOneField / EmptyClass fields:\n"
            + "private int kek\n", baOut.toString());

        deleteClass("ClassWithOneField");
    }

    @Test
    void testDiffBigInterestingClassKek() throws MalformedURLException, ClassNotFoundException {
        assertAll(() -> printStructure(me.sieric.TestClasses.Kek.class));
        Class<?> kek = compile("Kek");

        ByteArrayOutputStream baOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baOut);
        System.setOut(out);

        assertAll(() -> Reflector.diffClasses(me.sieric.TestClasses.Kek.class, kek));
        assertEquals("Classes are equal!!!!\n", baOut.toString());
        deleteClass("Kek");
    }

    @Test
    void testCompileEmptyClass() {
        assertAll(() -> printStructure(me.sieric.TestClasses.EmptyClass.class));
        assertAll(() -> compile("EmptyClass"));

        deleteClass("EmptyClass");
    }

    @Test
    void testCompileClassWithDifferentFields() {
        assertAll(() -> printStructure(me.sieric.TestClasses.ClassWithFields.class));
        assertAll(() -> compile("ClassWithFields"));

        deleteClass("ClassWithFields");
    }

    @Test
    void testCompileClassWithDifferentMethods() {
        assertAll(() -> printStructure(me.sieric.TestClasses.ClassWithMethods.class));
        assertAll(() -> compile("ClassWithMethods"));

        deleteClass("ClassWithMethods");
    }

    @Test
    void testCompileClassWithInnerClasses() {
        assertAll(() -> printStructure(me.sieric.TestClasses.ClassWithInnerClasses.class));
        assertAll(() -> compile("ClassWithInnerClasses"));

        deleteClass("ClassWithInnerClasses");
    }

    @Test
    void testConstructors() {
        assertAll(() -> printStructure(me.sieric.TestClasses.ClassWithConstructors.class));
        assertAll(() -> compile("ClassWithConstructors"));

        deleteClass("ClassWithConstructors");
    }


}
