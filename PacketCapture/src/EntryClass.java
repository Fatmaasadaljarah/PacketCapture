
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

/**
 *
 */
/**
 *
 *
 */
public class EntryClass implements jpcap.PacketReceiver {

    private static JFrame frame = null;
    private static DataGridView dataGridView = null;
    private static DataGrid dataGrid = null;
    private static DefaultTreeModel defaultTreeModel = null;
    private static JTree tree = null;
    private static DefaultMutableTreeNode rootNode = null;
    private static DefaultMutableTreeNode frameInfo = null;
    private static JPanel lowerPanel = null;
    static JScrollPane lowerLeftPanel = null;
    static JPanel lowerRightPanel = null;
    static int selectedDevice = 0;
    private static ImageIcon icon = null;

    public static enum packetType {

        ALL, HTTP, HTTPS, SMTP, FTP, IMAP, POP3, TCP, UDP;
    }

    public static enum chartType {

        Pie, Bar;
    }
    public static chartType char_type;
    public static packetType pac_type = packetType.ALL;

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        frame = new JFrame("Realtime Data Traffic Analyser");
        frame.setSize(900, 600);
        frame.setLayout(new GridLayout(2, 1));

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("EXIT");
                frame.setVisible(false);
                frame.dispose();
                System.exit(0);
            }
        });
        JMenuItem save = new JMenuItem("Save to file");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                writeToFile();
            }
        });
        fileMenu.add(save);
        fileMenu.add(exit);
        JMenu captureMenu = new JMenu("Capture");
        final JMenuItem start = new JMenuItem("Start Capture");
        final JMenuItem stopPie = new JMenuItem("Stop Capture (PieChart)");
        final JMenuItem stopBar = new JMenuItem("Stop Capture (BarChart)");
        stopPie.setEnabled(false);
        stopBar.setEnabled(false);

        captureMenu.add(start);
        captureMenu.add(stopPie);
        captureMenu.add(stopBar);
        JMenu deviceList = new JMenu("Device");

        final NetworkInterface[] lists = JpcapCaptor.getDeviceList();
        final JMenuItem devices[] = new JMenuItem[lists.length];
        for (int j = 0; j < lists.length; j++) {
            final int index = j;
            devices[j] = new JMenuItem(lists[j].description);
            devices[j].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    for (int i = 0; i < lists.length; i++) {
                        devices[i].setEnabled(true);
                    }
                    ((JMenuItem) event.getSource()).setEnabled(false);
                    MyJPCaptor.restartCaptureWithNewDevice(index, stopBar.isEnabled());
                    MyJPCaptor.restartCaptureWithNewDevice(index, stopPie.isEnabled());
                }
            });
            deviceList.add(devices[j]);
        }
        if (lists.length > 0) {
            devices[0].setEnabled(false);
        }
        JMenu PacketTypeMenu = new JMenu("Packet Type");
        JMenuItem all = new JMenuItem("All");
        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pac_type = packetType.ALL;
            }
        });
        PacketTypeMenu.add(all);
        JMenuItem HTTP = new JMenuItem("HTTP");
        HTTP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pac_type = packetType.HTTP;
            }
        });
        PacketTypeMenu.add(HTTP);
        JMenuItem HTTPS = new JMenuItem("HTTPS");
        HTTPS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pac_type = packetType.HTTPS;
            }
        });
        PacketTypeMenu.add(HTTPS);
        JMenuItem SMTP = new JMenuItem("SMTP");
        SMTP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pac_type = packetType.SMTP;
            }
        });
        PacketTypeMenu.add(SMTP);
        JMenuItem FTP = new JMenuItem("FTP");
        FTP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pac_type = packetType.FTP;
            }
        });
        PacketTypeMenu.add(FTP);
        JMenuItem IMAP = new JMenuItem("IMAP");
        IMAP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pac_type = packetType.IMAP;
            }
        });
        PacketTypeMenu.add(IMAP);
        JMenuItem POP3 = new JMenuItem("POP3");
        POP3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pac_type = packetType.POP3;
            }
        });
        PacketTypeMenu.add(POP3);
        JMenuItem TCP = new JMenuItem("TCP");
        TCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pac_type = packetType.TCP;
            }
        });
        PacketTypeMenu.add(TCP);
        JMenuItem UDP = new JMenuItem("UDP");
        UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                pac_type = packetType.UDP;
            }
        });
        PacketTypeMenu.add(UDP);
        menuBar.add(fileMenu);
        menuBar.add(captureMenu);
        menuBar.add(deviceList);
        menuBar.add(PacketTypeMenu);

        frame.setJMenuBar(menuBar);

        String[] columnNames = {"Number", "Protocol",
            "Source MAC",
            "Destination MAC",
            "Source IP",
            "Destination IP", "Data Length (bytes)", "Packet"};

        dataGridView = new DataGridView(columnNames);

        dataGrid = new DataGrid(dataGridView);
        dataGrid.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        JScrollPane scrpanel = new JScrollPane(dataGrid);

        dataGrid.getColumn("Packet").setWidth(0);
        dataGrid.getColumn("Packet").setMinWidth(0);
        dataGrid.getColumn("Packet").setMaxWidth(0);

        scrpanel.setAutoscrolls(false);
        scrpanel.setWheelScrollingEnabled(true);

        JPanel panel = new JPanel(new GridLayout(1, 1));

        panel.add(scrpanel);
        frame.add(panel);
        lowerPanel = new JPanel(new GridLayout(1, 2));
        rootNode = new DefaultMutableTreeNode("Packet Information");

        tree = new JTree();
        defaultTreeModel = new DefaultTreeModel(rootNode);

        tree.setModel(defaultTreeModel);

        lowerLeftPanel = new JScrollPane(tree);

        lowerRightPanel = new JPanel(new GridLayout(1, 1));
        lowerRightPanel.add(logo);

        lowerLeftPanel.setBackground(Color.WHITE);
        lowerLeftPanel.setBorder(new LineBorder(Color.BLACK));

        dataGrid.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                onRowSelection(dataGrid.getSelectedRow());
            }
        });

        lowerPanel.add(lowerLeftPanel);
        lowerPanel.add(lowerRightPanel);

        frame.add(lowerPanel);
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyJPCaptor.startCapture();
                stopPie.setEnabled(true);
                stopBar.setEnabled(true);
                start.setEnabled(false);
            }
        });

        stopPie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                char_type = chartType.Pie;
                stopPie.setEnabled(false);
                stopBar.setEnabled(false);
                start.setEnabled(true);
                MyJPCaptor.stopCapture();
            }
        });
        stopBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                char_type = chartType.Bar;
                stopPie.setEnabled(false);
                stopBar.setEnabled(false);
                start.setEnabled(true);
                MyJPCaptor.stopCapture();
            }
        });
    }
    private static int i = 1;

    @Override
    public void receivePacket(Packet packet) {
        if (packet instanceof IPPacket) {

            int ppp = ((IPPacket) packet).protocol;
            String protocol = CapturedPacket.protocoll[ppp];
            if ((EntryClass.pac_type == packetType.ALL) && (((IPPacket) packet).protocol == 6 || ((IPPacket) packet).protocol == 17 || ((IPPacket) packet).protocol == 1)) {
                dataGridView.addPacket(new CapturedPacket(packet, i++));
            } else if (protocol.equals("TCP")) {
                TCPPacket tcpPacket = (TCPPacket) packet;
                if ((EntryClass.pac_type == packetType.HTTP) && (tcpPacket.src_port == 80 || tcpPacket.dst_port == 80)) {
                    dataGridView.addPacket(new CapturedPacket(packet, i++));
                } else if ((EntryClass.pac_type == packetType.HTTPS) && (tcpPacket.src_port == 443 || tcpPacket.dst_port == 443)) {
                    dataGridView.addPacket(new CapturedPacket(packet, i++));
                } else if ((EntryClass.pac_type == packetType.SMTP) && (tcpPacket.src_port == 25 || tcpPacket.dst_port == 25)) {
                    dataGridView.addPacket(new CapturedPacket(packet, i++));

                } else if ((EntryClass.pac_type == packetType.POP3) && (tcpPacket.src_port == 110 || tcpPacket.dst_port == 110)) {
                    dataGridView.addPacket(new CapturedPacket(packet, i++));

                } else if ((EntryClass.pac_type == packetType.IMAP) && (tcpPacket.src_port == 143 || tcpPacket.dst_port == 143)) {
                    dataGridView.addPacket(new CapturedPacket(packet, i++));

                } else if ((EntryClass.pac_type == packetType.FTP) && (tcpPacket.src_port == 20 || tcpPacket.dst_port == 20 || tcpPacket.src_port == 21 || tcpPacket.dst_port == 21)) {
                    dataGridView.addPacket(new CapturedPacket(packet, i++));

                } else if (EntryClass.pac_type == packetType.TCP) {
                    dataGridView.addPacket(new CapturedPacket(packet, i++));
                }
            } else {
                if (protocol.equals("UDP") && (EntryClass.pac_type == packetType.UDP)) {
                    dataGridView.addPacket(new CapturedPacket(packet, i++));
                } 
            }
        }
    }

    public static void onRowSelection(int selectedRow) {
        Packet packet = (Packet) ((Vector<Object>) dataGridView.getDataVector().elementAt(selectedRow)).elementAt(7);
        EthernetPacket ethernetPacket = (EthernetPacket) packet.datalink;
        if (rootNode.getChildCount() == 0) {
            frameInfo = new DefaultMutableTreeNode("Etherenet Frame");
            rootNode.add(frameInfo);
        } else {
            frameInfo.removeAllChildren();
            rootNode.remove(1);
        }
        frameInfo.add(new DefaultMutableTreeNode("Frame Type : " + ethernetPacket.frametype));
        frameInfo.add(new DefaultMutableTreeNode("Source Mac : " + ethernetPacket.getSourceAddress()));
        frameInfo.add(new DefaultMutableTreeNode("Destination MAC : " + ethernetPacket.getDestinationAddress()));

        int protocol = ((IPPacket) packet).protocol;
        if (protocol == 6) {
            TCPPacket tcpPacket = (TCPPacket) packet;
            DefaultMutableTreeNode protocolInfo = new DefaultMutableTreeNode("TCP");
            rootNode.add(protocolInfo);

            protocolInfo.add(new DefaultMutableTreeNode("Source Port : " + tcpPacket.src_port));
            protocolInfo.add(new DefaultMutableTreeNode("Destination Port : " + tcpPacket.dst_port));
            protocolInfo.add(new DefaultMutableTreeNode("Sequence Number : " + tcpPacket.sequence));
            protocolInfo.add(new DefaultMutableTreeNode("Ack Number : " + tcpPacket.ack_num));
            protocolInfo.add(new DefaultMutableTreeNode("URG Flag : " + tcpPacket.urg));
            protocolInfo.add(new DefaultMutableTreeNode("ACK Flag : " + tcpPacket.ack));
            protocolInfo.add(new DefaultMutableTreeNode("PSH Flag : " + tcpPacket.psh));
            protocolInfo.add(new DefaultMutableTreeNode("RST Flag : " + tcpPacket.rst));
            protocolInfo.add(new DefaultMutableTreeNode("SYN Flag : " + tcpPacket.syn));
            protocolInfo.add(new DefaultMutableTreeNode("FIN Flag : " + tcpPacket.fin));
            protocolInfo.add(new DefaultMutableTreeNode("Window Size : " + tcpPacket.window));

        } else if (protocol == 17) {
            UDPPacket udpPacket = (UDPPacket) packet;
            DefaultMutableTreeNode protocolInfo = new DefaultMutableTreeNode("UDP");
            rootNode.add(protocolInfo);
            protocolInfo.add(new DefaultMutableTreeNode("Source Port : " + udpPacket.src_port));
            protocolInfo.add(new DefaultMutableTreeNode("Destination Port : " + udpPacket.dst_port));

        } else if (protocol == 1) {
            ICMPPacket icmpPacket = (ICMPPacket) packet;
            DefaultMutableTreeNode protocolInfo = new DefaultMutableTreeNode("ICMP");
            rootNode.add(protocolInfo);

            protocolInfo.add(new DefaultMutableTreeNode("Alive Time : " + icmpPacket.alive_time));
            protocolInfo.add(new DefaultMutableTreeNode("Address # : " + icmpPacket.addr_num));
            protocolInfo.add(new DefaultMutableTreeNode("MTU : " + icmpPacket.mtu));
            protocolInfo.add(new DefaultMutableTreeNode("Subnet Mask : " + icmpPacket.subnetmask));
            protocolInfo.add(new DefaultMutableTreeNode("Source IP : " + icmpPacket.src_ip));
            protocolInfo.add(new DefaultMutableTreeNode("Destinatin IP : " + icmpPacket.dst_ip));
            protocolInfo.add(new DefaultMutableTreeNode("Check Sum : " + icmpPacket.checksum));
            protocolInfo.add(new DefaultMutableTreeNode("ICMP Type : " + icmpPacket.type));
        }
        ((DefaultTreeModel) tree.getModel()).reload();

        tree.expandRow(0);
        tree.expandRow(1);
    }

    public static void setChart(JPanel chart) {
        System.out.println("Chart");
        lowerRightPanel.removeAll();
        lowerRightPanel.add(chart);
        lowerRightPanel.revalidate();
        lowerRightPanel.repaint();
        frame.repaint();
    }

    public static void setLogo() {
        System.out.println("LOGO");
        lowerRightPanel.removeAll();
        lowerRightPanel.add(logo);
        lowerRightPanel.repaint();
    }

    private static JPanel logo = null;

    static {
        logo = new JPanel();
        logo.setBackground(Color.WHITE);
        String path = null;
        try {
            path = getJarFile().getParent();
            System.out.println("mitesh" + path);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(path + "\\icon.png");
        icon = new ImageIcon(path + "\\icon.png");
        JLabel label = new JLabel();
        label.setIcon(icon);
        logo.add(label);
    }

    protected static File getJarFile() throws URISyntaxException {
        return new File(EntryClass.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    public static void writeToFile() {
        try {
            FileWriter myWriter = new FileWriter("filename.csv");
            myWriter.write("Number,Protocol,Source MAC,Destination MAC,Source IP,Destination IP,Data Length (bytes)\n");
            for (int i = 0; i < dataGridView.getRowCount(); i++) {
                myWriter.write(dataGridView.getValueAt(i, 0).toString() + ",");
                myWriter.write(dataGridView.getValueAt(i, 1).toString() + ",");
                myWriter.write(dataGridView.getValueAt(i, 2).toString() + ",");
                myWriter.write(dataGridView.getValueAt(i, 3).toString() + ",");
                myWriter.write(dataGridView.getValueAt(i, 4).toString() + ",");
                myWriter.write(dataGridView.getValueAt(i, 5).toString() + ",");
                myWriter.write(dataGridView.getValueAt(i, 6).toString());
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
