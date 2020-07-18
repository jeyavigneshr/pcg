package resource.tools;

import org.jdom2.Element;

public interface ObjectWriter extends Serial{
	public Element write(Object o);
}
