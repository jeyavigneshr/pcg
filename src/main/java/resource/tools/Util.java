package resource.tools;

import java.lang.reflect.Constructor;
import java.security.AccessController;

import sun.reflect.ReflectionFactory;

public class Util implements Serial {

	/** reflection factory for forcing default constructors */
	private static final ReflectionFactory reflFactory = (ReflectionFactory) AccessController
			.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());

	public static void main(String[] args) {
		String test = "Hello";
		System.out.println(stringable(test));
	}

	/**
	 * Returns a no-arg constructor that despite appearences can be used to
	 * construct objects of the specified type!!!of first non-serializable
	 */
	public static Constructor forceDefaultConstructor(Class cl) throws Exception {
		Constructor cons = Object.class.getDeclaredConstructor(new Class[0]);
		cons = reflFactory.newConstructorForSerialization(cl, cons);
		cons.setAccessible(true);
		// System.out.println("Cons: " + cons);
		return cons;
	}

	public static boolean stringable(Object o) {
		// assume the following types go easily to strings...
		boolean val = (o instanceof Number) || (o instanceof Boolean) || (o instanceof Class) || (o instanceof String);
		// System.out.println("Stringable: " + o + " : " + val + " : " + o.getClass());
		return val;
	}

	public static boolean stringable(Class type) {
		// assume the following types go easily to strings...
		boolean val = (Number.class.isAssignableFrom(type)) || (Boolean.class.isAssignableFrom(type))
				|| (String.class.equals(type)) || (Class.class.equals(type));
		// System.out.println("Stringable: " + type + " : " + val);
		return val;
	}

	public static boolean stringable(String name) {
		// assume the following types go easily to strings...
		// System.out.println("Called (String) version");
		try {
			Class type = Class.forName(name);
			return stringable(type);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean primitive(Class type) {
		for (int i = 0; i < primitives.length; i++) {
			if (primitives[i].equals(type)) {
				return true;
			}
		}
		return false;
	}

}
