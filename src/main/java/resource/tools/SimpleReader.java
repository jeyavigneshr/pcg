package resource.tools;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom2.Element;

public class SimpleReader implements ObjectReader {

	HashMap map;

	public SimpleReader() {
		map = new HashMap();
	}

	public Object read(Element xob) {
		if (empty(xob)) {
			return null;
		} else if (reference(xob)) {
			return map.get(xob.getAttributeValue(IDREF));
		}
		Object ob = null;
		String id = xob.getAttributeValue(ID);
		if (primitiveArray(xob)) {
			ob = readPrimitiveArray(xob, id);
		} else if (array(xob)) {
			ob = readObjectArray(xob, id);
		} else if (Util.stringable(xob.getAttributeValue(TYPE))) {
			ob = readStringObject(xob, id);
		} else { // assume we have a normal object with some fields to set
			ob = readObject(xob, id);
		}
		return ob;
	}

	public boolean empty(Element xob) {
		return !xob.getAttributes().iterator().hasNext() && !xob.getContent().iterator().hasNext();
	}

	public boolean reference(Element xob) {
		boolean ret = xob.getAttribute(IDREF) != null;
		return ret;
	}

	public boolean primitiveArray(Element xob) {
		if (!xob.getName().equals(ARRAY)) {
			return false;
		}
		String arrayType = xob.getAttributeValue(TYPE);
		for (int i = 0; i < primitives.length; i++) {
			if (primitives[i].getName().equals(arrayType)) {
				return true;
			}
		}
		return false;
	}

	public boolean array(Element xob) {
		return xob.getName().equals(ARRAY);
	}

	public Object readPrimitiveArray(Element xob, Object id) {
		try {
			Class type = getPrimitiveType(xob.getAttributeValue(TYPE));
			Class wrapperType = getWrapperType(type);
			Constructor cons = wrapperType.getDeclaredConstructor(new Class[] { String.class });
			Object[] args = new Object[1];
			int len = Integer.parseInt(xob.getAttributeValue(LENGTH));
			Object array = Array.newInstance(type, len);
			map.put(id, array);

			if (type.equals(int.class)) {
				Object intArray = readIntArray((int[]) array, xob);
				return intArray;
			}

			if (type.equals(byte.class)) {
				Object byteArray = readByteArray((byte[]) array, xob);
				return byteArray;
			}

			StringTokenizer st = new StringTokenizer(xob.getText());
			int index = 0;
			while (st.hasMoreTokens()) {
				String s = st.nextToken();
				args[0] = s;
				Object value = cons.newInstance(args);
				Array.set(array, index++, value);
			}
			return array;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Class getPrimitiveType(String name) {
		for (int i = 0; i < primitives.length; i++) {
			if (primitives[i].getName().equals(name)) {
				return primitives[i];
			}
		}
		return null;
	}

	public Class getWrapperType(Class type) {
		for (int i = 0; i < primitives.length; i++) {
			if (primitives[i].equals(type)) {
				return primitiveWrappers[i];
			}
		}
		return null;
	}

	public Class getWrapperType(String type) {
		for (int i = 0; i < primitives.length; i++) {
			if (primitives[i].getName().equals(type)) {
				return primitiveWrappers[i];
			}
		}
		return null;
	}

	public Object readIntArray(int[] a, Element xob) {
		StringTokenizer st = new StringTokenizer(xob.getText());
		int index = 0;
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			a[index++] = Integer.parseInt(s);
		}
		return a;
	}

	public Object readByteArray(byte[] a, Element xob) {
		String strByte = xob.getText();
		a = strByte.getBytes();
		byte[] decodedArray = Base64.getDecoder().decode(a);
		return decodedArray;
	}

	public Object readObjectArray(Element xob, Object id) {
		try {
			String arrayTypeName = xob.getAttributeValue(TYPE);
			int len = Integer.parseInt(xob.getAttributeValue(LENGTH));
			Class componentType = getObjectArrayComponentType(arrayTypeName);
			Object array = Array.newInstance(componentType, len);
			map.put(id, array);
			List children = xob.getChildren();
			int index = 0;
			for (Iterator i = children.iterator(); i.hasNext();) {
				Object childArray = read((Element) i.next());
				Array.set(array, index++, childArray);
			}
			return array;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Class getObjectArrayComponentType(String arrayTypeName) throws Exception {
		return Class.forName(arrayTypeName);
	}

	public Object readStringObject(Element xob, Object id) {
		try {
			Class type = Class.forName(xob.getAttributeValue(TYPE));
			if (type.equals(Class.class)) {
				return Class.forName(xob.getText());
			} else {
				Class[] st = { String.class };
				Constructor cons = type.getDeclaredConstructor(st);
				Object ob = makeObject(cons, new String[] { xob.getText() }, id);
				return ob;
			}
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public Object readObject(Element xob, Object id) {
		try {
			Class type = Class.forName(xob.getAttributeValue(TYPE));
			Constructor cons = Util.forceDefaultConstructor(type);
			cons.setAccessible(true);
			Object ob = makeObject(cons, new Object[0], id);
			setFields(ob, xob);
			return ob;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public void setFields(Object ob, Element xob) {
		Class type = ob.getClass();
		for (Iterator i = xob.getChildren().iterator(); i.hasNext();) {
			Element fe = (Element) i.next();
			String name = fe.getAttributeValue(NAME);
			String declaredType = fe.getAttributeValue(DECLARED);
			try {
				Class declaringType;
				if (declaredType != null) {
					declaringType = Class.forName(declaredType);
				} else {
					declaringType = type;
				}
				Field field = getField(declaringType, name);
				field.setAccessible(true);
				Object value = null;
				if (Util.primitive(field.getType())) {
					value = makeWrapper(field.getType(), fe.getAttributeValue(VALUE));
				} else {
					Element child = (Element) fe.getChildren().iterator().next();
					value = read(child);
				}
				field.set(ob, value);
			} catch (Exception e) {
				System.out.println(name + " : " + e);
			}
		}

	}

	public Object makeObject(Constructor cons, Object[] args, Object key) throws Exception {
		cons.setAccessible(true);
		Object value = cons.newInstance(args);
		map.put(key, value);
		return value;
	}

	public Object makeWrapper(Class type, String value) throws Exception {
		Class wrapperType = getWrapperType(type);
		Constructor cons = wrapperType.getDeclaredConstructor(new Class[] { String.class });
		return cons.newInstance(new Object[] { value });
	}

	public Field getField(Class type, String name) throws Exception {
		if (type == null) {
			return null;
		}
		try {
			return type.getDeclaredField(name);
		} catch (Exception e) {
			return getField(type.getSuperclass(), name);
		}
	}

	public void print(Constructor[] cons) {
		for (int i = 0; i < cons.length; i++) {
			System.out.println(i + " : " + cons[i]);
		}
	}

	public Class getComponentType(String type) {
		for (int i = 0; i < primitiveArrays.length; i++) {
			if (primitiveArrays[i].getName().equals(type)) {
				return primitives[i];
			}
		}
		return null;
	}

	public Class getArrayType(String type) {
		for (int i = 0; i < primitiveArrays.length; i++) {
			if (primitiveArrays[i].getName().equals(type)) {
				return primitiveArrays[i];
			}
		}
		return null;
	}

}
