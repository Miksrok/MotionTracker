package frame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bytedeco.javacv.CanvasFrame;
import controller.EventController;
import video.paint.Painter;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private CanvasFrame main;
	private JPanel mainPanel, textPanel, servicePanel, messagePanel,
			buttonsPanel;
	private JPanel panelForText, panelForAngle;
	private JButton startButton, pauseButton, resumeButton, file;
	private JButton hip, knee, ankle, ankleFingers, ribs;
	private JLabel text, angle;
	private JLabel angleValue, angleValue_knee, angleValue_hip,
			angleValue_ankle;
	
	public MainFrame() {
	}

	public void lf() {

		init();
		createFrame();
		addListeners();

	}

	private void init() {
		main = new CanvasFrame("Main Frame");
		main.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		textPanel = new JPanel();
		panelForText = new JPanel();
		panelForAngle = new JPanel();
		servicePanel = new JPanel();
		messagePanel = new JPanel();
		buttonsPanel = new JPanel();
		// =====
		startButton = new JButton("Start");
		pauseButton = new JButton("Pause");
		resumeButton = new JButton("Resume");
		file = new JButton("File");
		// =====
		hip = new JButton("hip");
		knee = new JButton("knee");
		ankle = new JButton("ankle");
		ankleFingers = new JButton("fingers");
		ribs = new JButton("ribs");
		// =====
		text = new JLabel("select file");
		angle = new JLabel("angle:");
		angleValue = new JLabel("0");
		angleValue_knee = new JLabel("knee joint angle: 0");
		angleValue_hip = new JLabel("hip joint angle: 0");
		angleValue_ankle = new JLabel("ankle joint angle: 0");
	}

	private void createFrame() {

		main.add(mainPanel, BorderLayout.SOUTH);
		main.add(textPanel, BorderLayout.NORTH);
		main.add(servicePanel, BorderLayout.EAST);
		servicePanel.setLayout(new GridLayout(2, 0));
		textPanel.setLayout(new GridLayout(0, 2));

		main.setSize(800, 500);

		mainPanel.add(startButton);
		mainPanel.add(pauseButton);
		mainPanel.add(resumeButton);
		mainPanel.add(file);

		textPanel.add(panelForText);
		textPanel.add(panelForAngle);
		panelForText.add(text);
		panelForAngle.add(angle);
		panelForAngle.add(angleValue);
		servicePanel.add(messagePanel);
		servicePanel.add(buttonsPanel);
		buttonsPanel.setLayout(new GridLayout(3, 2));
		buttonsPanel.add(hip);
		hip.setEnabled(false);
		buttonsPanel.add(knee);
		knee.setEnabled(false);
		buttonsPanel.add(ankle);
		ankle.setEnabled(false);
		buttonsPanel.add(ankleFingers);
		ankleFingers.setEnabled(false);
		buttonsPanel.add(ribs);
		ribs.setEnabled(false);

		messagePanel.setLayout(new GridLayout(3, 0));
		messagePanel.add(angleValue_hip);
		messagePanel.add(angleValue_knee);
		messagePanel.add(angleValue_ankle);

	}

	private void addListeners() {
		
		EventController controller = new EventController();
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				controller.startPlay();				
			}
		});
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		resumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileopen = new JFileChooser();
				int ret = fileopen.showDialog(null, "Open file");
				if (ret == JFileChooser.APPROVE_OPTION) {
					if(fileopen.getSelectedFile().canExecute()){		
						controller.addVideo(fileopen.getSelectedFile(), main);				
						text.setText(fileopen.getSelectedFile().getName() + " is open");
						hip.setEnabled(true);
						knee.setEnabled(true);
						ankle.setEnabled(true);
						ankleFingers.setEnabled(true);
						ribs.setEnabled(true);
					}else{
						text.setText("fail");
					}
				}
				Painter.getPainter().clearMaps();
			}
		});
		hip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.changeSelector("hip");	
				text.setText("select hip joint"); 
				hip.setEnabled(false);
			}
		});
		knee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				controller.changeSelector("knee");
				text.setText("select knee joint"); 
				knee.setEnabled(false);			 
			}
		});
		ankle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.changeSelector("ankle");
				text.setText("select ankle joint"); 
				ankle.setEnabled(false);
			}
		});
		ankleFingers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * PrevFrame.selector=PrevFrame.ANKLE_FINGERS;
				 * text.setText("select fingers joint");
				 * ankleFingers.setEnabled(false);
				 */
			}
		});
		ribs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * PrevFrame.selector=PrevFrame.RIBS;
				 * text.setText("select ribs joint"); ribs.setEnabled(false);
				 */
			}
		});
		
		main.getCanvas().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent click) {
				controller.addMouseAction(click);		
			}
		});

	}

}
