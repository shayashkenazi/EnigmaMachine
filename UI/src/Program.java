import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
public class Program {

    public static void main(String[] args) {

        UIprogram ui = new ConsoleUI();
        ui.run();
    }
}
