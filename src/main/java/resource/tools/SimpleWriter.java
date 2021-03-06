package resource.tools;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Base64;
import java.util.HashMap;
import java.util.Vector;

import org.jdom2.Comment;
import org.jdom2.Element;

public class SimpleWriter implements ObjectWriter {

	HashMap map;
	int count;
	boolean writePrimitiveTypes = true;
	boolean doStatic = true;
	boolean doFinal = false;

	public SimpleWriter() {
		map = new HashMap();
		count = 0;
	}

	public Element write(Object ob) {
		Element el;
		if (ob == null) {
			return new Element(OBJECT);
		}
		if (map.get(ob) != null) {
			el = new Element(OBJECT);
			el.setAttribute(IDREF, map.get(ob).toString());
			return el;
		}
		map.put(ob, new Integer(count++));
		if (Util.stringable(ob)) {
			el = new Element(OBJECT);
			el.setAttribute(TYPE, ob.getClass().getName());
			el.setText(stringify(ob));
		} else if (ob.getClass().isArray()) {
			el = writeArray(ob);
		} else {
			el = new Element(OBJECT);
			el.setAttribute(TYPE, ob.getClass().getName());
			writeFields(ob, el);
		}
		el.setAttribute(ID, map.get(ob).toString());
		return el;
	}

	public Element writeArray(Object ob) {
		if (isPrimitiveArray(ob.getClass())) {
			return writePrimitiveArray(ob);
		} else {
			return writeObjectArray(ob);
		}
	}

	public Element writeObjectArray(Object ob) {
		Element el = new Element(ARRAY);
		el.setAttribute(TYPE, ob.getClass().getComponentType().getName());
		int len = Array.getLength(ob);
		el.setAttribute(LENGTH, "" + len);
		for (int i = 0; i < len; i++) {
			el.addContent(write(Array.get(ob, i)));
		}
		return el;
	}

	public Element writePrimitiveArray(Object ob) {
		Element el = new Element(ARRAY);
		el.setAttribute(TYPE, ob.getClass().getComponentType().getName());
		int len = Array.getLength(ob);
		if (ob instanceof byte[]) {
			el.setText(byteArrayString((byte[]) ob, el));
		} else {
			el.setAttribute(LENGTH, "" + len);
			el.setText(arrayString(ob, len));
		}
		return el;
	}

	public String byteArrayString(byte[] a, Element e) {
		byte[] target = Base64.getEncoder().encode(a);
		e.setAttribute(LENGTH, "" + target.length);
		String strTarget = new String(target);
		return strTarget;
	}

	public String arrayString(Object ob, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			if (i > 0) {
				sb.append(" ");
			}
			sb.append(Array.get(ob, i).toString());
		}
		return sb.toString();
	}

	public void writeFields(Object o, Element parent) {
		Class cl = o.getClass();
		Field[] fields = getFields(cl);
		String name = null;
		for (int i = 0; i < fields.length; i++) {
			if ((doStatic || !Modifier.isStatic(fields[i].getModifiers()))
					&& (doFinal || !Modifier.isFinal(fields[i].getModifiers())))
				try {
					fields[i].setAccessible(true);
					name = fields[i].getName();
					Object value = fields[i].get(o);
					Element field = new Element(FIELD);
					field.setAttribute(NAME, name);
					if (shadowed(fields, name)) {
						field.setAttribute(DECLARED, fields[i].getDeclaringClass().getName());
					}
					if (fields[i].getType().isPrimitive()) {
						if (writePrimitiveTypes) {
							field.setAttribute(TYPE, fields[i].getType().getName());
						}
						field.setAttribute(VALUE, value.toString());

					} else {
						field.addContent(write(value));
					}
					parent.addContent(field);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
					parent.addContent(new Comment(e.toString()));
				}
		}
	}

	private boolean shadowed(Field[] fields, String fieldName) {
		int count = 0;
		for (int i = 0; i < fields.length; i++) {
			if (fieldName.equals(fields[i].getName())) {
				count++;
			}
		}
		return count > 1;
	}

	public static String stringify(Object ob) {
		if (ob instanceof Class) {
			return ((Class) ob).getName();
		} else {
			return ob.toString();
		}
	}

	public static Field[] getFields(Class c) {
		Vector v = new Vector();
		while (!(c == null)) { // c.equals( Object.class ) ) {
			Field[] fields = c.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				// System.out.println(fields[i]);
				v.addElement(fields[i]);
			}
			c = c.getSuperclass();
		}
		Field[] f = new Field[v.size()];
		for (int i = 0; i < f.length; i++) {
			f[i] = (Field) v.get(i);
		}
		return f;
	}

	public static Object[] getValues(Object o, Field[] fields) {
		Object[] values = new Object[fields.length];
		for (int i = 0; i < fields.length; i++) {
			try {
				fields[i].setAccessible(true);
				values[i] = fields[i].get(o);
				System.out.println(fields[i].getName() + "\t " + values[i]);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return values;
	}

	public boolean isPrimitiveArray(Class c) {
		for (int i = 0; i < primitiveArrays.length; i++) {
			if (c.equals(primitiveArrays[i])) {
				return true;
			}
		}
		return false;
	}

}
