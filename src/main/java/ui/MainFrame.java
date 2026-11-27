
package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private JButton elegirMazeButton;
    private JButton salirButton;
    private JButton resolverBFSButton;
    private JButton resolverDFSButton;
    private JButton resolverDijkstraButton;
    
    private JComboBox elegirMazeComboBox;
    private JCheckBox animacionCheckBox;   
    
    private JLabel animacionLabel;
    
    private JPanel botonesPanel;
    
    public MainFrame() {
        super();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());
        initComponents();
        setLayouts();
    }
    
    private void initComponents() {
        elegirMazeButton = new JButton();
        salirButton = new JButton();
        resolverBFSButton = new JButton();
        resolverDFSButton = new JButton();
        resolverDijkstraButton = new JButton();
        
        elegirMazeComboBox = new JComboBox();
        animacionCheckBox = new JCheckBox();
        
        animacionLabel = new JLabel("¿Mostrar Animación?");
    }
    
    private void setLayouts() {
        botonesPanel = new JPanel(new FlowLayout());
        botonesPanel.add(elegirMazeButton);
        botonesPanel.add(elegirMazeComboBox);
        
        botonesPanel.add(animacionLabel);
        botonesPanel.add(animacionCheckBox);
        
        botonesPanel.add(resolverBFSButton);
        botonesPanel.add(resolverDFSButton);
        botonesPanel.add(resolverDijkstraButton);
        
        this.add(botonesPanel, BorderLayout.NORTH);
    }
    
    private void setBehavior() {
        
    }
    
}
