package resource.tools;

public interface Serial {

	public static final String OBJECT = "object";
	public static final String FIELD = "field";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String VALUE = "value";
	public static final String ARRAY = "array";
	public static final String LENGTH = "length";
	public static final String ID = "id";
	public static final String IDREF = "idref";
	public static final String DECLARED = "declaredClass";

	public static final Class[] primitiveArrays = new Class[] { int[].class, boolean[].class, byte[].class,
			short[].class, long[].class, char[].class, float[].class, double[].class };

	public static final Class[] primitiveWrappers = new Class[] { Integer.class, Boolean.class, Byte.class, Short.class,
			Long.class, Character.class, Float.class, Double.class };

	public static final Class[] primitives = new Class[] { int.class, boolean.class, byte.class, short.class,
			long.class, char.class, float.class, double.class };

}
