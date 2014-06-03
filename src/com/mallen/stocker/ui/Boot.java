package com.mallen.stocker.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.mallen.notify.DeskNotify;
import com.mallen.stocker.core.NotificationFactory;

public class Boot {
	public static JFrame jfSplash;
	public static void main(String[] args) throws IOException{
    	Boot b = new Boot();
    	b.launchDialog();
	}
	public void launchDialog(){
		
		NotificationFactory.notify("https://au.finance.yahoo.com/", "Visit finance.yahoo.com to look up Stock Symbols");
		
		
		JTextField stockfield = new JTextField("");
        JRadioButton fullscreen = new JRadioButton();
        JRadioButton testdata = new JRadioButton();
        JSlider slider_size = new JSlider(1000, 100000, 10000);
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        panel.add(new JLabel("STOCK SYMBOL: "));
        panel.add(stockfield);
        
        fullscreen.add(new JLabel("      FULLSCREEN"));
        panel.add(fullscreen);
        
    	fullscreen.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  if(launchfullscreen){ 
					  launchfullscreen = false;
				  } else {
					  launchfullscreen = true;  
				  }
			  }
		});
    	
    	testdata.add(new JLabel("      TEST DATA"));
        panel.add(testdata);
        
    	testdata.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  if(intest){
					  intest = false;
				  } else {
					  intest = true;
				  }
			  }
		});
    	
    	
    	panel.add(new JLabel("Memory Size"));
    	panel.add(slider_size);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Stocker",
            JOptionPane.WARNING_MESSAGE, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
        	System.out.println(stockfield.getText().trim().toUpperCase() + ", " + slider_size.getValue());
        	configSplash();
        	launchStocker(stockfield.getText().trim().toUpperCase(), slider_size.getValue());
        } else {
        	System.exit(0);
        }
	}
	boolean launchfullscreen = false, intest = false;
	public void launchStocker(String symb, int size){
		UI u =  new UI(symb, launchfullscreen, size, intest);
		u.drawUI();
	}
	public void configSplash(){
		jfSplash = new JFrame();
		jfSplash.setSize(250, 25);
		jfSplash.setLocationRelativeTo(null);
		jfSplash.add(new SplashScreen());
		jfSplash.setUndecorated(true);
		jfSplash.setAlwaysOnTop(true);
		jfSplash.setVisible(true);
	}
}
