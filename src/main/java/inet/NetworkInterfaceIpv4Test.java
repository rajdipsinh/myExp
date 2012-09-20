package inet;

import java.net.*;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * Date: 20/09/12
 * Time: 12:26
 *
 * @author RSolanki<rslanki@ad-holdings.co.uk>
 */
public class NetworkInterfaceIpv4Test {
    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                while (addressEnumeration.hasMoreElements()){
                    InetAddress address = addressEnumeration.nextElement();
                    if(address instanceof Inet4Address){
                        System.out.println("is IPV4");
                    }
                    if(address instanceof Inet6Address){
                        System.out.println("is IPV6");
                    }
                }

            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
