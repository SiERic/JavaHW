package me.sieric;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Create and initialize object of `rootClassName` class using classes from
 * `implementationClassNames` for concrete dependencies.
 */
public class Injector {

    private static Map<Class<?>, Optional<Object>> builtObjects = new HashMap<>();

    /**
     * Creates instance of the class with specified name.
     * Uses implementations of classes from the specified list to resolve all dependencies.
     * @param rootClassName - a name of class to create instance of
     * @param implementationClasses - a list of available implementations
     * @return an instance of required class
     * @throws ImplementationNotFoundException if implementation is not found
     * @throws InjectionCycleException if found dependency cycle
     * @throws AmbiguousImplementationException if found multiple implementations of one class
     * @throws InvocationTargetException if something got wrong while construct an instance
     * @throws InstantiationException if could not create an instance of some implementation
     * @throws IllegalAccessException if there is no access to some method
     */
    public static Object initialize(String rootClassName, List<Class<?> > implementationClasses) throws ImplementationNotFoundException, InjectionCycleException, AmbiguousImplementationException, InvocationTargetException, InstantiationException, IllegalAccessException {

        builtObjects.clear();

        Class<?> rootClass;
        try {
            rootClass = Class.forName(rootClassName);
        } catch (ClassNotFoundException e) {
            throw new ImplementationNotFoundException("");
        }

        return go(rootClass, implementationClasses);
    }

    /**
     * A dfs-like method to get around the dependency tree
     * @param clazz - a class to create instance of
     * @param implementationClasses - a list of available implementations
     * @return an instance of required class
     * @throws InjectionCycleException if found dependency cycle
     * @throws AmbiguousImplementationException if found multiple implementations of one class
     * @throws ImplementationNotFoundException if implementation is not found
     * @throws IllegalAccessException if there is no access to some method
     * @throws InvocationTargetException if something got wrong while construct an instance
     * @throws InstantiationException if could not create an instance of some implementation
     */
    private static Object go(Class<?> clazz, List<Class<?> > implementationClasses) throws InjectionCycleException, AmbiguousImplementationException, ImplementationNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (builtObjects.containsKey(clazz)) {
            Optional<Object> tmp = builtObjects.get(clazz);
            if (tmp.isEmpty()) {
                throw new InjectionCycleException("Cycle found while constructing class " + clazz.getName());
            }
        }

        builtObjects.put(clazz, Optional.empty());

        Constructor<?> constructor = clazz.getConstructors()[0];
        List<Object> params = new ArrayList<>();

        for (Class<?> dependency : constructor.getParameterTypes()) {

            Object param = null;
            for (Class<?> classs : implementationClasses) {
                if (dependency.isAssignableFrom(classs)) {
                    if (param != null) {
                        throw new AmbiguousImplementationException("Several realization for " + dependency.getName());
                    } else {
                        param = go(classs, implementationClasses);
                    }
                }
            }

            if (param == null) {
                throw new ImplementationNotFoundException("Found no implementation for " + dependency.getName());
            }
            params.add(param);
        }

        Object object = constructor.newInstance(params.toArray());

        builtObjects.put(clazz, Optional.of(object));
        return object;
    }
}
