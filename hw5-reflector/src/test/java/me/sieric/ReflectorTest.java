package me.sieric;

import org.junit.jupiter.api.AfterEach;
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

    @Test
    void testCompileBigInterestingClass() {
         assertAll(() -> printStructure(me.sieric.TestClasses.Kek.class));
         assertAll(() -> compile("Kek"));
    }

    @Test
    void testDiffEmptyClass() throws MalformedURLException, ClassNotFoundException {
        assertAll(() -> printStructure(me.sieric.TestClasses.EmptyClass.class));
        Class<?> emptyClass = compile("EmptyClass");

        ByteArrayOutputStream baOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baOut);
        System.setOut(out);

        assertAll(() -> Reflector.diffClasses(me.sieric.TestClasses.EmptyClass.class, emptyClass));
        assertEquals("Classes are equal!!!!" + System.lineSeparator(), baOut.toString());
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
        "Classes are different!!!!" + System.lineSeparator()
            + "-------------------------------------" + System.lineSeparator()
            + "ClassWithOneField / EmptyClass fields:" + System.lineSeparator()
            + "private int kek" + System.lineSeparator(), baOut.toString());
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
    }

    @Test
    void testCompileEmptyClass() {
        assertAll(() -> printStructure(me.sieric.TestClasses.EmptyClass.class));
        assertAll(() -> compile("EmptyClass"));
    }

    @Test
    void testCompileClassWithDifferentFields() {
        assertAll(() -> printStructure(me.sieric.TestClasses.ClassWithFields.class));
        assertAll(() -> compile("ClassWithFields"));
    }

    @Test
    void testCompileClassWithDifferentMethods() {
        assertAll(() -> printStructure(me.sieric.TestClasses.ClassWithMethods.class));
        assertAll(() -> compile("ClassWithMethods"));
    }

    @Test
    void testCompileClassWithInnerClasses() {
        assertAll(() -> printStructure(me.sieric.TestClasses.ClassWithInnerClasses.class));
        assertAll(() -> compile("ClassWithInnerClasses"));
    }

    @Test
    void testConstructors() {
        assertAll(() -> printStructure(me.sieric.TestClasses.ClassWithConstructors.class));
        assertAll(() -> compile("ClassWithConstructors"));
    }

    @AfterEach
    void deleteClasses() {
        var folder = new File(".");
        for (var file : folder.listFiles()) {
            if (file.toString().endsWith(".class") || file.toString().endsWith(".java")) {
                file.delete();
            }
        }
    }
}
