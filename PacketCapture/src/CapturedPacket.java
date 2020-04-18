
import java.util.Vector;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

//import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.data.general.PieDataset;
//import jpcap.packet.EthernetPacket;
//import jpcap.packet.IPPacket;
//import jpcap.packet.Packet;
//import jpcap.packet.TCPPacket;
/**
 *
 */
/**
 *
 *
 */
public class CapturedPacket {

    public static String protocoll[] = {"HOPOPT", "ICMP", "IGMP", "GGP", "IPV4", "ST", "TCP", "CBT", "EGP", "IGP", "BBN", "NV2", "PUP", "ARGUS", "EMCON", "XNET", "CHAOS", "UDP", "mux"};

    private static int HTTP = 0;
    private static int FTP = 0;
    private static int TCP = 0;
    private static int HTTPS = 0;
    private static int SMTP = 0;
    private static int POP3 = 0;
    private static int IMAP = 0;
    private static int UDP = 0;
    private static int ICMP = 0;

    public static CategoryDataset createCategoryDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(new Integer(HTTP), "HTTP", "Packet");
        dataset.setValue(new Integer(HTTPS), "HTTPS", "Packet");
        dataset.setValue(new Integer(SMTP), "SMTP", "Packet");
        dataset.setValue(new Integer(FTP), "FTP", "Packet");
        dataset.setValue(new Integer(IMAP), "IMAP", "Packet");
        dataset.setValue(new Integer(POP3), "POP3", "Packet");
        dataset.setValue(new Integer(TCP), "TCP", "Packet");
        dataset.setValue(new Integer(UDP), "UDP", "Packet");
        dataset.setValue(new Integer(ICMP), "ICMP", "Packet");


        return dataset;
    }

    private String protocol;
    private IPPacket packet;
    Vector<Object> vector = new Vector<Object>(6);

    public CapturedPacket(Packet packet, int number) {
        this.packet = (IPPacket) packet;
        if (packet != null) {

            int ppp = this.packet.protocol;
            protocol = protocoll[ppp];

            vector.add(0, number);
            if (protocol.equals("TCP")) {
                TCPPacket tcpPacket = (TCPPacket) packet;
                if (tcpPacket.src_port == 80 || tcpPacket.dst_port == 80) {
                    vector.add(1, "HTTP");
                    HTTP++;
                } else if (tcpPacket.src_port == 443 || tcpPacket.dst_port == 443) {
                    vector.add(1, "HTTPS");
                    HTTPS++;
                } else if (tcpPacket.src_port == 25 || tcpPacket.dst_port == 25) {
                    vector.add(1, "SMTP");
                    SMTP++;
                } else if (tcpPacket.src_port == 110 || tcpPacket.dst_port == 110) {
                    vector.add(1, "POP3");
                    POP3++;
                } else if (tcpPacket.src_port == 143 || tcpPacket.dst_port == 143) {
                    vector.add(1, "IMAP");
                    IMAP++;
                } else if (tcpPacket.src_port == 20 || tcpPacket.dst_port == 20 || tcpPacket.src_port == 21 || tcpPacket.dst_port == 21) {
                    vector.add(1, "FTP");
                    FTP++;
                } else {
                    TCP++;
                    vector.add(1, protocol);
                }
            } else {
                if (protocol.equals("UDP")) {
                    UDP++;
                } else {
                    ICMP++;
                }
                vector.add(1, protocol);
            }
            vector.add(2, ((EthernetPacket) packet.datalink).getSourceAddress());
            vector.add(3, ((EthernetPacket) packet.datalink).getDestinationAddress());
            vector.add(4, this.packet.src_ip.toString().substring(1));
            vector.add(5, this.packet.dst_ip.toString().substring(1));
            vector.add(6, this.packet.len);
            vector.add(7, this.packet);

        }
    }

    public static void resetDATA() {
        HTTP = 0;
        FTP = 0;
        TCP = 0;
        HTTPS = 0;
        SMTP = 0;
        POP3 = 0;
        IMAP = 0;
        UDP = 0;
        ICMP = 0;
    }

    public static PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("HTTP", new Integer(HTTP));
        dataset.setValue("HTTPS", new Integer(HTTPS));
        dataset.setValue("SMTP", new Integer(SMTP));
        dataset.setValue("FTP", new Integer(FTP));
        dataset.setValue("IMAP", new Integer(IMAP));
        dataset.setValue("POP3", new Integer(POP3));
        dataset.setValue("TCP", new Integer(TCP));
        dataset.setValue("UDP", new Integer(UDP));
        dataset.setValue("ICMP", new Integer(ICMP));
        return dataset;
    }

    public Vector<Object> getRowRecord() {
        return vector;
    }

}
