package me.sieric;

import org.jetbrains.annotations.NotNull;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/** Class to print structure of java classes and compare content of classes */
public class Reflector {

    /**
     * Prints structure of a given class.
     * Includes imports, class declaration, fields, constructors, methods and inner classes and interfaces.
     * Writes the result in working directory naming the file same as class.
     * @param someClass - a class to print
     * @throws FileNotFoundException if can't open file to print class
     * @throws FormatterException if can't format the code beautifully
     */
    public static void printStructure(@NotNull Class<?> someClass) throws FileNotFoundException, FormatterException {
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(someClass.getSimpleName() + ".java")));
        printWriter.print(getStructure(someClass));
        printWriter.close();
    }

    /**
     * Gets structure of a given class.
     * Includes imports, class declaration, fields, constructors, methods and inner classes and interfaces.
     * @param someClass - a class to get structure of
     * @return a source code of given class
     */
    public static String getStructure(@NotNull Class<?> someClass) throws FormatterException {
        StringBuilder classSource = new StringBuilder();

        printImports(someClass, classSource);
        printClass(someClass, classSource);

        return new Formatter().formatSourceAndFixImports(classSource.toString());
    }

    /**
     * Prints to System.out all methods and fields which exists in exactly one class of two given classes.
     * @param firstClass - a first class
     * @param secondClass - a second class
     */
    public static void diffClasses(@NotNull Class<?> firstClass, @NotNull Class<?> secondClass) {
        if (oneSideDiffClasses(firstClass, secondClass) & oneSideDiffClasses(secondClass, firstClass)) {
            System.out.println("Classes are equal!!!!");
        }
    }

    private static boolean oneSideDiffClasses(Class<?> firstClass, Class<?> secondClass) {
        String diffFields = getFieldsDiff(secondClass, firstClass).trim().replaceAll("\\s{2,}", " ");
        String diffMethods = getMethodsDiff(secondClass, firstClass).trim().replaceAll("\\s{2,}", " ");
        if (diffFields.equals("") && diffMethods.equals("")) {
            return true;
        } else {
            System.out.println("Classes are different!!!!");
        }
        if (!diffFields.equals("")) {
            System.out.println("-------------------------------------");
            System.out.println(firstClass.getSimpleName() + " / " + secondClass.getSimpleName() + " fields:");
            System.out.println(diffFields);
        }
        if (!diffMethods.equals("")) {
            System.out.println("-------------------------------------");
            System.out.println(firstClass.getSimpleName() + " / " + secondClass.getSimpleName() + " methods:");
            System.out.println(diffMethods);
        }
        return false;
    }

    private static String getFieldsDiff(Class<?> firstClass, Class<?> secondClass) {
        Set<String> fieldsFirst = Arrays.stream(firstClass.getDeclaredFields()).
                map(Reflector::fieldToString).collect(Collectors.toCollection(HashSet::new));
        return Arrays.stream(secondClass.getDeclaredFields()).map(Reflector::fieldToString).
                filter(s -> !fieldsFirst.contains(s)).collect(Collectors.joining(System.lineSeparator()));
    }

    private static String fieldToString(Field field) {
        return Modifier.toString(field.getModifiers()) + " " + field.getGenericType().getTypeName() + " " + field.getName();
    }

    private static String getMethodsDiff(Class<?> firstClass, Class<?> secondClass) {
        Set<String> methodsFirst = Arrays.stream(firstClass.getDeclaredMethods()).
                map(Reflector::methodToString).collect(Collectors.toCollection(HashSet::new));
        return Arrays.stream(secondClass.getDeclaredMethods()).map(Reflector::methodToString).
                filter(s -> !methodsFirst.contains(s)).collect(Collectors.joining(System.lineSeparator()));
    }

    private static String methodToString(Method method) {
        return Modifier.toString(method.getModifiers()) + " " + getMethodGenericType(method) + " "
                + method.getGenericReturnType().getTypeName() + " " + method.getName()
                + getParameters(method.getGenericParameterTypes(), false) + getExceptions(method.getExceptionTypes());
    }

    private static void printImports(Class<?> someClass, StringBuilder source) {
        Set<String> imports = new HashSet<>();
        addImports(someClass, imports);
        imports.remove( someClass.getPackageName() + ".*");
        for (String imported : imports) {
            source.append("import ").append(imported).append(";\n");
        }
    }

    private static void addImports(Class<?> someClass, Set<String> imports) {
        addImportsFromSuperClass(someClass, imports);
        addImportsFromFields(someClass, imports);
        addImportsFromMethods(someClass, imports);
        addImportsFromConstructors(someClass, imports);
        addImportsFromInnerClasses(someClass, imports);
    }

    private static void addImportsFromSuperClass(Class<?> someClass, Set<String> imports) {
        if (someClass.getSuperclass() != null) {
            addToImports(someClass.getSuperclass(), imports);
        }
        for (Class<?> interFace : someClass.getInterfaces()) {
            addToImports(interFace, imports);
        }
    }

    private static void addImportsFromFields(Class<?> someClass, Set<String> imports) {
        for (Field field : someClass.getDeclaredFields()) {
            addToImports(field.getType(), imports);
        }
    }

    private static void addImportsFromMethods(Class<?> someClass, Set<String> imports) {
        for (Method method : someClass.getDeclaredMethods()) {
            for (Class<?> parameter : method.getParameterTypes()) {
                addToImports(parameter, imports);
            }
            for (var exception : method.getExceptionTypes()) {
                addToImports(exception, imports);
            }
            addToImports(method.getReturnType(), imports);
        }
    }

    private static void addImportsFromConstructors(Class<?> someClass, Set<String> imports) {
        for (Constructor constructor : someClass.getDeclaredConstructors()) {
            for (Class<?> parameter : constructor.getParameterTypes()) {
                addToImports(parameter, imports);
            }
        }
    }

    private static void addImportsFromInnerClasses(Class<?> someClass, Set<String> imports) {
        for (Class<?> inner : someClass.getDeclaredClasses()) {
            addImports(inner, imports);
        }
    }

    private static void addToImports(Class<?> someImport, Set<String> imports) {
        if (!someImport.isPrimitive() && !someImport.getCanonicalName().contains("[]")) {
            imports.add(someImport.getPackage().getName() + ".*");
        }
    }

    private static void printClass(Class<?> someClass, StringBuilder source) {
        printClassDeclaration(someClass, source);
        source.append("{\n");

        printClassFields(someClass, source);
        printConstructors(someClass, source);
        printClassMethods(someClass, source);

        printInnerClasses(someClass, source);

        source.append("}\n");
    }

    private static void printClassDeclaration(Class<?> someClass, StringBuilder source) {
        source.append(Modifier.toString(someClass.getModifiers()));
        if (someClass.isInterface()) {
            source.append(" ");
        } else {
            source.append(" class ");
        }
        source.append(someClass.getSimpleName());
        source.append(getGenericType(someClass.getTypeParameters()));
        printParent(someClass, source);
        printInterfaces(someClass, source);
    }

    private static void printParent(Class<?> someClass, StringBuilder source) {
        if (someClass.getSuperclass() != null && someClass.getSuperclass() != Object.class) {
            source.append(" extends ");
            source.append(someClass.getSuperclass().getSimpleName());
            source.append(getGenericType(someClass.getSuperclass().getTypeParameters()));
        }
    }

    private static void printInterfaces(Class<?> someClass, StringBuilder source) {
        if (someClass.getInterfaces().length != 0) {
            StringJoiner interfaces = new StringJoiner(", ", " implements ", "");
            for (Type interFace : someClass.getGenericInterfaces()) {
                interfaces.add(interFace.toString().replace("interface ", ""));
            }
            source.append(interfaces.toString());
        }
    }

    private static String getGenericType(TypeVariable<?> [] parameters) {
        if (parameters.length != 0) {
            StringJoiner genericType = new StringJoiner(", ", "<", ">");
            for (var param : parameters) {
                String bounds = Arrays.stream(param.getBounds()).map(Type::getTypeName).
                        filter(type -> !type.equals("java.lang.Object")).collect(Collectors.joining(" & "));
                if (!bounds.equals("")) {
                    genericType.add(param.getName() + " extends " + bounds);
                } else {
                    genericType.add(param.getName());
                }
            }
            return genericType.toString();
        }
        return "";
    }

    private static void printClassFields(Class<?> someClass, StringBuilder source) {
        Arrays.stream(someClass.getDeclaredFields()).
                filter(x -> !x.isSynthetic()).forEach(x -> printField(x, source));
    }

    private static void printField(Field field, StringBuilder source) {
        source.append(Modifier.toString(field.getModifiers()));
        source.append(" ");
        source.append(field.getGenericType().getTypeName());
        source.append(" ");
        source.append(field.getName());
        source.append(" = ");
        source.append(getDefaultValue(field.getType()));
        source.append(";\n");
    }

    private static void printConstructors(Class<?> someClass, StringBuilder source) {
        boolean isInner = (someClass.getModifiers() & ~Modifier.PUBLIC & ~Modifier.STATIC) != 0;
        Arrays.stream(someClass.getDeclaredConstructors()).
                filter(x -> !x.isSynthetic()).forEach(x -> printConstructor(x, source, isInner));
    }

    private static void printConstructor(Constructor<?> constructor, StringBuilder source, boolean isInner) {
        source.append(Modifier.toString(constructor.getModifiers()));
        source.append(" ");
        source.append(constructor.getDeclaringClass().getSimpleName());
        source.append(getParameters(constructor.getGenericParameterTypes(), isInner));
        source.append(" ");
        source.append(getExceptions(constructor.getExceptionTypes()));
        source.append("{}\n");
    }

    private static void printClassMethods(Class<?> someClass, StringBuilder source) {
        Arrays.stream(someClass.getDeclaredMethods()).
                filter(x -> !x.isSynthetic()).forEach(x -> printMethod(x, source));
    }

    private static void printMethod(Method method, StringBuilder source) {
        source.append(Modifier.toString(method.getModifiers()));
        source.append(" ");
        source.append(getMethodGenericType(method));
        source.append(method.getGenericReturnType().getTypeName());
        source.append(" ");
        source.append(method.getName());
        source.append(getParameters(method.getGenericParameterTypes(), false));
        source.append(" ");
        source.append(getExceptions(method.getExceptionTypes()));
        if ((method.getModifiers() & Modifier.ABSTRACT) == 0) {
            source.append("{\n");
            source.append("return ");
            source.append(getDefaultValue(method.getReturnType()));
            source.append(";");
            source.append("}\n");
        } else {
            source.append(";\n");
        }
    }

    private static String getDefaultValue(Class<?> someClass) {
        if (someClass.isPrimitive()) {
            if (someClass.getSimpleName().equals("boolean")) {
                return "false";
            } else if (someClass.getSimpleName().equals("void")) {
                return "";
            } else {
                return "0";
            }
        } else {
            return "null";
        }
    }

    private static String getMethodGenericType(Method method) {
        if (method.getTypeParameters().length != 0) {
            StringJoiner genericType = new StringJoiner(", ", "<", ">");
            for (Type type : method.getTypeParameters()) {
                genericType.add(type.getTypeName());
            }
            return genericType.toString();
        }
        return "";
    }

    private static String getParameters(Type[] parameters, boolean notUseFirst) {
        int counter = 0;
        StringJoiner params = new StringJoiner(", ", "(", ")");
        for (var parameter : parameters) {
            if (notUseFirst && counter == 0) {
                counter++;
            } else {
                params.add(parameter.getTypeName() + " a" + counter++);
            }
        }
        return params.toString();
    }

    private static String getExceptions(Class<?>[] exceptions) {
        if (exceptions.length == 0) {
            return "";
        }
        StringJoiner throwsExceptions = new StringJoiner(", ", " throws ", "");
        for (var exception : exceptions) {
            throwsExceptions.add(exception.getSimpleName());
        }
        return throwsExceptions.toString();
    }

    private static void printInnerClasses(Class<?> someClass, StringBuilder source) {
        Arrays.stream(someClass.getDeclaredClasses()).forEach(x -> printClass(x, source));
    }

}
