package org.creativeteen.tools;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.creativeteen.tools.model.Configuration;
import org.creativeteen.tools.utils.GsonUtil;

public class CreativeteenMediaProcessor {

    private static Configuration config;
    private static JTextArea output;
    private static JScrollPane scrollPane;

    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Media Processing Actions");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("Video to Audio", KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Convert Video to Audio");
        menu.add(menuItem);
        menuItem.addActionListener( new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											String src = config.video;
											String tgt = config.folder+"\\"+config.video+".mp3";
											runCommand(".\\ffmpeg\\bin\\ffmpeg.exe   -i " + src + " " + tgt);
										}
									} );
        menu.addSeparator();
        menuItem = new JMenuItem("Replace Audio in Video", KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Convert Video to Audio");
        menu.add(menuItem);


        //Build second menu in the menu bar.
        menu = new JMenu("About..");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "CreativeTeen ~ leading our kids to be creative");
        menuBar.add(menu);

        return menuBar;
    }

    
    private static void runCommand(String command) {
    	System.out.print(command);
    	Process process;
	    try {
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    private FileChoosingButton videoChooser;
    private FileChoosingButton audioChooser;
    private FileChoosingButton pictureChooser;
    private FileChoosingButton folderChooser;
    
    public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        //contentPane.setOpaque(true);

        {
	        //Create a scrolled text area.
	        output = new JTextArea(1, 30);
	        output.setEditable(false);
	        scrollPane = new JScrollPane(output);
	
	        //Add the text area to the content pane.
	        contentPane.add(scrollPane, BorderLayout.SOUTH);
        }

        {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            
            videoChooser = new FileChoosingButton("VIDEO........", JFileChooser.FILES_ONLY, new File(config.video));
        	panel.add( videoChooser );

        	audioChooser = new FileChoosingButton("AUDIO........", JFileChooser.FILES_ONLY, new File(config.audio));
        	panel.add( audioChooser );

        	pictureChooser = new FileChoosingButton("PICTURE..", JFileChooser.FILES_ONLY, new File(config.picture));
        	panel.add( pictureChooser );

        	folderChooser = new FileChoosingButton("FOLDER......", JFileChooser.DIRECTORIES_ONLY, new File(config.folder));
        	panel.add( folderChooser );
        	
	        contentPane.add(panel, BorderLayout.CENTER);
        }
        // 
        return contentPane;
    }
 
 
    class FileChoosingButton extends JButton {
    	private File fileChoosed = null;
    	private ActionListener actionListener;
    	private JFileChooser fc;
    	
    	FileChoosingButton(String label, int choosemode, File file){
    		fc = new JFileChooser();
    		fc.setFileSelectionMode(choosemode);
    		
    		fileChoosed = file;
    		this.setText(label +"        "+fileChoosed.getName());
    		
    		actionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
		            int returnVal = fc.showOpenDialog(null);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		            	fileChoosed = fc.getSelectedFile();
		            }
				}
    		};
    		
    		this.addActionListener(actionListener);
    	}
    }
    
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CreativeteenMediaProcessor.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("CreativeTeen Media Processor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        CreativeteenMediaProcessor demo = new CreativeteenMediaProcessor();
        frame.setJMenuBar(demo.createMenuBar());
        frame.setContentPane(demo.createContentPane());

        //Display the window.
        frame.setSize(450, 260);
        frame.setVisible(true);
    }

    
    public static void main(String[] args) {
    	String json = null;
		try {
			json = Files.readString(Paths.get("config.json"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
    	config = GsonUtil.parse(json, Configuration.class);
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
	
}
