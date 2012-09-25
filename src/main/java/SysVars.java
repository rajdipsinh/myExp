import java.util.Enumeration;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: rajdip
 * Date: 25/09/12
 * Time: 13:05
 * To change this template use File | Settings | File Templates.
 */
public class SysVars {
    public static void main(String[] args) {
        Properties properties = System.getProperties();
        Enumeration  enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()){
            String propName = (String)enumeration.nextElement();
            System.out.println(propName+" = "+properties.get(propName));
        }
    }
}
