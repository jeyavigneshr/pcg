package resource.tools;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.input.SAXBuilder;
//import org.jdom.output.XMLOutputter;
//
//import wox.serial.ObjectReader;
//import wox.serial.ObjectWriter;
//import wox.serial.SimpleReader;
//import wox.serial.SimpleWriter;

public class Easy {

    public static void save(Object ob, String filename) {
        try {
            ObjectWriter writer = new SimpleWriter();
            Element el = writer.write(ob);
            XMLOutputter out = new XMLOutputter(); // ("  ", true);
            FileWriter file = new FileWriter(filename);
            out.output(el, file);
            file.close();
            System.out.println("Saved object to " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object load(String filename) {
        try {
            SAXBuilder builder = new SAXBuilder();
            InputStream is = new FileInputStream(filename);
            Document doc = builder.build(is);
            Element el = doc.getRootElement();
            ObjectReader reader = new SimpleReader();
            return reader.read(el);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
