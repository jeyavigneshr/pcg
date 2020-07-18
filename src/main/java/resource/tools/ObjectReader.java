package resource.tools;

import org.jdom2.Element;

public interface ObjectReader extends Serial{
	public Object read(Element xob);
}
