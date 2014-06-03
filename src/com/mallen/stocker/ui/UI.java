package com.mallen.stocker.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.mallen.stocker.core.NotificationFactory;
import com.mallen.stocker.core.Stock;

public class UI extends JPanel {
	JFrame jf = new JFrame();
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	
	//JTEXTFIELDS IN THE MENU BAR
	////////////////////////////////////////////////////////////////////////
	JMenuBar jmenu_bar = new JMenuBar();
	
	JTextField jtext_openprice = new JTextField("Open: ");
	JTextField jtext_maxprice = new JTextField("Max: ");
	JTextField jtext_minprice = new JTextField("Min: ");
	JTextField jtext_price = new JTextField("Price: ");
	JTextField jtext_change = new JTextField("Change: ");
	JTextField jtext_volume = new JTextField("Volume: ");
	JTextField jtext_tick = new JTextField("T");
	JTextField jtext_clock = new JTextField("TIME");
	
	
	JRadioButton jradio_indicators = new JRadioButton("Indicators");
	JRadioButton jradio_openprice = new JRadioButton("Open Price");
	JRadioButton jradio_price = new JRadioButton("Price");
	JRadioButton jradio_limit = new JRadioButton("High Speed Polling");
	JRadioButton jradio_replay = new JRadioButton("Replay Data");
	JRadioButton jradio_pause = new JRadioButton("Pause Data Stream");
	
	
	JSlider jslider_hor = new JSlider(200, (int) screen.getWidth()-10, (int) screen.getWidth()-10);
	JSlider jslider_ver = new JSlider(1, 500, 250);
	JSlider jslider_sensitivty = new JSlider(1, 200, 20);
	JSlider jslider_update = new JSlider(1, 100, 10);
	JSlider jslider_replay = new JSlider(1, 10000, 5000);
	
	JLabel jtext_update = new JLabel("");
	JLabel jtext_hor = new JLabel("Hor: ");
	JLabel jtext_ver = new JLabel("Ver:");
	JButton jbut_exit = new JButton("Exit");
	JButton jbut_fullscreen = new JButton("Fullscreen");
	JButton jbut_nightmode = new JButton("Night Mode");
	JLabel jtext_replay = new JLabel("Tick: ");
	////////////////////////////////////////////////////////////////////////
	
	
	//JTEXTFIELD TABLE
	////////////////////////////////////////////////////////////////////////
	JTextField table_1 = new JTextField();  JTextField table_2 = new JTextField();
	JTextField table_3 = new JTextField();  JTextField table_4 = new JTextField();
	JTextField table_5 = new JTextField();  JTextField table_6 = new JTextField();
	JTextField table_7 = new JTextField();  JTextField table_8 = new JTextField();
	JTextField table_9 = new JTextField();  JTextField table_10 = new JTextField();
		
	////////////////////////////////////////////////////////////////////////
	private boolean drawPrice = false, drawOpen = false, drawInd = false, limitdata = true;
	private int rticks = 0, rticks_offset = 0;
	private boolean rticks_override = false, fullscreen;
	private String symb;
	private Stock s;
	///////////////////////////////////////////////////////////////////////
	
	public UI(String insymb, boolean fs, int size, boolean intest){
		fullscreen = fs;
		symb = insymb;	
		s = new Stock(insymb, size, intest);	
		
		if(s.testrun){
			drawPrice = true;
			drawOpen = true;
			drawInd = true;
			limitdata = false;
		}
	}
	
	public void drawUI(){
		
		if(fullscreen){
			jf.setSize((int) screen.getWidth(), (int) screen.getHeight());
			jf.setUndecorated(true);
		} else {
			jf.setSize((int) screen.getWidth(), (int) screen.getHeight());
			jf.setExtendedState(jf.MAXIMIZED_BOTH);
			jf.setUndecorated(false);
		}
		
		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		jf.setTitle("Stocker");
		
		ImageIcon ii = new ImageIcon("res/Stocker.png");
		Image icon = ii.getImage();
		jf.setIconImage(icon);

		
		
		this.configAndBuildUI();		
		jf.setJMenuBar(jmenu_bar);
		jf.add(this);
		
		jf.setResizable(true);
		jf.setLocationRelativeTo(null);
		this.loop();
	}
	
	
	boolean Paused = false;
	String stock_change = "";
	
	public void loop(){
		
		double delta = System.currentTimeMillis();
		DecimalFormat df = new DecimalFormat("#.000");
		
		while(true){
				delta = System.currentTimeMillis();
				try {
					
					if(!jf.isVisible() && s.ticks > 10 && s.START_PRICE != 0 && s.MAX_PRICE != 0){
							jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
							jf.setVisible(true);
							Boot.jfSplash.setVisible(false);
					}
					
					if(!Paused){
						s.updateShare();
						jf.setTitle("Stocker - " + s.getSymb() + " - " + df.format(s.prices[rticks-1]) + "/" + s.MAX_PRICE);
						jtext_price.setText("Price:" + df.format(s.prices[rticks-1]));
						jtext_tick.setText("T" + rticks + "/" + s.prices.length);
						jtext_replay.setText("Tick: " + jslider_replay.getValue());
						
						COL_WIDTH = jslider_hor.getValue();
						COL_HEIGHT = jslider_ver.getValue();
						sensitivity = jslider_sensitivty.getValue();
						
						jtext_hor.setText("Hor: " + COL_WIDTH);
						jtext_ver.setText("Ver: " + COL_HEIGHT);
						
						//SETTING CLOCK
						Date date = new Date();
						SimpleDateFormat clock = new SimpleDateFormat("HH:mm:ss");	
						jtext_clock.setText(clock.format(date));
						//////////////////////////////////////////////////////////////
						if(s.prices[rticks-1] != s.prices[rticks-2]){
							jtext_change.setText(df.format(s.prices[rticks-1]-s.prices[rticks-2]));
						}
						
					} else {
						jf.setTitle("Stocker - PAUSED");
						jtext_tick.setText("PAUSED");
					}
					
					
					if(limitdata){
						System.out.println("TICK - " + (long) ((long)(jslider_update.getValue() * 100.0)-(System.currentTimeMillis()-delta)));
						Thread.sleep((long) ((long)(jslider_update.getValue() * 100.0)-(System.currentTimeMillis()-delta)));		
					}
			} catch(Exception e){}
		}
	}

	//COL WIDTH AND HEIGHT
	int COL_WIDTH = jslider_hor.getValue();
	int COL_HEIGHT = jslider_ver.getValue();
	int sensitivity = jslider_sensitivty.getValue();
	
	//FONT DEFINITIONS
	Font stock_font = new Font("arial", Font.PLAIN, 42);
	Font inf_font = new Font("arial", Font.PLAIN, 24);
	Font sml_font = new Font("arial", Font.PLAIN, 12);
	int upval = 0;
	
	//COLOUR PALLETE	
	Color col_secondary;
	Color col_primary;

	Color col_down;
	Color col_up;
	Color col_alt;
	Color prevcol;                                      
	ThemeManager thememanager = new ThemeManager();
	////////////////
	
	public void paint(Graphics g){
	super.paint(g);
	
	
	Theme currtheme = thememanager.loadTheme("");
	
	col_primary = currtheme.theme_array[0];
	col_secondary = currtheme.theme_array[1];
	col_alt = currtheme.theme_array[2];
	col_up = currtheme.theme_array[3];
	col_down = currtheme.theme_array[4];
	
		if(!rticks_override){
			rticks = s.ticks;
		} else {
			rticks = s.ticks-rticks_offset;
		}
	
	
		
		DecimalFormat updateformat = new DecimalFormat("#0.0");
		DecimalFormat df = new DecimalFormat("#.000");
		FontMetrics tm = g.getFontMetrics(inf_font);
		
		//SETTING COLOUR OF TEXT, 
		//IF Previous Price > Current Price,Color = Red, else Color = Green
		//////////////////////////////////////////////////////////////////////
		
		if(s.prices[rticks-2] != null && s.prices[rticks-1] != null && s.prices[rticks]!= null){
				if(Double.valueOf(s.prices[rticks-1]) > Double.valueOf(s.prices[rticks-2])){
					prevcol = col_up;		
					g.setColor(prevcol);
				} else if(Double.valueOf(s.prices[rticks-1]) < Double.valueOf(s.prices[rticks-2])){
					prevcol = col_down;
					g.setColor(prevcol);
				}
		
		//UPDATING UI ELEMENTS
		//////////////////////////////////////////////////////////////////		
			
				FontMetrics tms = g.getFontMetrics(sml_font);
			
				jtext_openprice.setText("Open: " + s.START_PRICE);
				jtext_maxprice.setText("Max: " + s.MAX_PRICE);
				jtext_minprice.setText("Min: " + s.MIN_PRICE);
				jtext_volume.setText("Volume: " + s.getShare("volume"));
			}
	
				
		///// DRAWING BACKGROUND ///////////////////////////
		g.setColor(col_primary);
		g.fillRect(0, 0, jf.getBounds().width, jf.getBounds().height);
		/////////////////////////////////////////////////////////////
		
		
		if(rticks > 1){
			int firsttick = rticks;
			for(int LOOP = 1; LOOP < COL_WIDTH+10; LOOP++){ 
				
				boolean drawRight = false; //DRAW THE DATA FLOWING TO THE RIGHT, IF FALSE FLOWS TO LEFT
				
				int DRAW_VALUE = (COL_WIDTH+10)-LOOP;		//VALUE THAT IS USED TO DRAW ONTO THE STAGE
				
				if(drawRight) DRAW_VALUE = LOOP;
				
				int DRAW_DATA = rticks-LOOP;	//VALUE THAT IS USED TO GET DATA

				if(DRAW_DATA < 0){
					DRAW_DATA = LOOP;
				}
				
				try {
					if(s.prices[DRAW_DATA] != null){
						int stage_height = jf.getBounds().height;
						int stage_width = (jf.getBounds().width);
						int stage_mid = (stage_height/2);
						int offset = 1;
						double DIV_PRICE = s.START_PRICE;
	
						g.setColor(this.getIndCol(DRAW_DATA));
						this.updateInterval();
						this.drawInd(g, DRAW_DATA, DRAW_VALUE, stage_height, stage_mid, stage_width, tm);
						this.drawOpen(g, stage_mid, stage_width);
						this.drawPrice(g, DRAW_VALUE, DRAW_DATA, DIV_PRICE, stage_mid, stage_height);
					}
				} catch(Exception e){}
			}
		}
		
				try {
					Thread.sleep(25);
				} catch(Exception e){}
		repaint();
	}
	
	public void drawPrice(Graphics g, int DRAW_VALUE, int DRAW_DATA, double DIV_PRICE, int stage_mid, int stage_height){
		if(drawPrice){
			int offset = -1;
			g.setColor(col_secondary);
			g.drawLine(((DRAW_VALUE)*(jf.getBounds().width-30)/COL_WIDTH), 
					(int) (1+stage_mid-((stage_height*sensitivity)*(s.prices[DRAW_DATA-2])/(DIV_PRICE)))
					+(stage_height*sensitivity),
					((DRAW_VALUE-offset)*(jf.getBounds().width-30)/COL_WIDTH), 	
					(int) (1+stage_mid-((stage_height*sensitivity)*(s.prices[DRAW_DATA-1])/(DIV_PRICE)))
					+(stage_height*sensitivity)
			);
		}
	}
	public Color getIndCol(int DRAW_DATA){
		Color returnCol;
		if(s.prices[DRAW_DATA-1] >= s.prices[DRAW_DATA-2]){
			returnCol = (col_up);
		} else {
			returnCol = (col_down);
		}
			return returnCol;
	}
	public void drawInd(Graphics g, int DRAW_DATA, double DRAW_VALUE, int stage_height, int stage_mid, int stage_width, FontMetrics tm){
		if(drawInd){
			double val_change = s.prices[DRAW_DATA-1]-s.prices[DRAW_DATA-2];
			int drawHeight = (int) ((stage_height/2)*(val_change*10));  // Sets canvas value to $.20 scale -- Scale is 1/i -- eg. 5 = .2, 10 = .1, 100 = .001
			
			g.fillRect((int) ((DRAW_VALUE)*(jf.getBounds().width-30)/COL_WIDTH), 
						stage_mid-drawHeight-(((jf.getBounds().height-(tm.getHeight()+20))/COL_HEIGHT)/2), 
						stage_width/COL_WIDTH, 
						stage_height/COL_HEIGHT);
		}
	}
	public void drawOpen(Graphics g, int stage_mid, int stage_width){
		if(drawOpen){
			// OPENING PRICE LINE					
			g.setColor(col_alt);
			g.fillRect(0, stage_mid-1, stage_width, 2);
			///////////////////////////////////////////////////////
	}
	}
	public void updateInterval(){
		DecimalFormat updateformat = new DecimalFormat("#0.0");
		jtext_update.setText("Interval: " + updateformat.format(jslider_update.getValue()*0.1) + "s");
	}
	public void configAndBuildUI(){
		jtext_price.setBackground(col_primary);
		jtext_openprice.setBackground(col_primary);
		jtext_minprice.setBackground(col_primary);
		jtext_maxprice.setBackground(col_primary);
		jtext_change.setBackground(col_primary);
		jtext_volume.setBackground(col_primary);
		
		jtext_price.setForeground(col_secondary);
		jtext_openprice.setForeground(col_secondary);
		jtext_minprice.setForeground(col_secondary);
		jtext_maxprice.setForeground(col_secondary);
		jtext_change.setForeground(col_secondary);
		jtext_volume.setForeground(col_secondary);
		jtext_clock.setForeground(col_secondary);
		
		jtext_tick.setEditable(false);
		jtext_price.setEditable(false);
		jtext_openprice.setEditable(false);
		jtext_minprice.setEditable(false);
		jtext_maxprice.setEditable(false);
		jtext_change.setEditable(false);
		jtext_volume.setEditable(false);
		jtext_clock.setEditable(false);
		
		//MENU CONTAINING DATA CONTROL SETTINGS
		JMenu menu = new JMenu("Stocker");
		JMenu menu_data = new JMenu("Data Settings");
			menu_data.add(jradio_pause);
			menu_data.addSeparator();
			menu_data.add(jtext_update);
			menu_data.add(jslider_update);
			menu_data.add(jradio_limit);
		menu.add(menu_data);
		//////////////////////////////////////////////////////
		
		//MENU CONTAINING RADIO BUTTONS TO TOGGLE UI ELEMENTS
		JMenu menu_elements = new JMenu("UI Elements");
			menu_elements.add(jradio_indicators);	
			menu_elements.add(jradio_openprice);
			menu_elements.add(jradio_price);
		menu.add(menu_elements);
		//////////////////////////////////////////////////////
		
			
		//MENU CONTAING JSLIDERS FOR CHANGING SCALE OF UI
		JMenu menu_scaling = new JMenu("Scaling");
			menu_scaling.add(jtext_ver);
			menu_scaling.add(jslider_ver);
			
			menu_scaling.add(jtext_hor);
			menu_scaling.add(jslider_hor);
			
			menu_scaling.add(new JLabel("Sensitivity"));
			menu_scaling.add(jslider_sensitivty);
	
		menu.add(menu_scaling);
		/////////////////////////////////////////////////////
			
		//BUTTON CALLED TO ENABLE TEST DATA
				jradio_pause.addActionListener(new ActionListener() { 
					  public void actionPerformed(ActionEvent e) { 
						 if(Paused){Paused = false;} else {Paused = true;}
					  }
				});
		////////////////////////////////////////////
			
		//BUTTON CALLED TO ENABLE TEST DATA
				jradio_limit.addActionListener(new ActionListener() { 
					  public void actionPerformed(ActionEvent e) { 
						 if(limitdata){limitdata = false;} else {limitdata = true;}
					  }
				});
		////////////////////////////////////////////f
				
		//BUTTON CALLED TO EXIT
			jbut_nightmode.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
					ThemeManager.toggleNight();
				}
			});
		////////////////////////////////////////////
				
		//BUTTON CALLED TO EXIT
			jbut_exit.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) { 
					  System.exit(0);
				  }
			});
		////////////////////////////////////////////
		
		//RADIO BUTTON CALLING IF YOU DRAW THE PRICE LINE
			jradio_price.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) { 
					  if(drawPrice){
						  drawPrice = false;
					  } else {
						  drawPrice = true;
					}
				} 
			});
		/////////////////////////////////////////////
		
		//RADIO BUTTON FOR CALLING HISTORY PLAYER
			jradio_replay.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) { 
						 if(!rticks_override){
						  	  rticks_override = true;
							  rticks_offset = rticks;
							  rticks = jslider_replay.getValue();
						 } else {
							 rticks_override = false;
						 }
				} 
			});
		/////////////////////////////////////////	
		
		//RADIO BUTTON CALLING IF YOU DRAW THE OPEN PRICE
			jradio_openprice.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) { 
					  if(drawOpen){
						  drawOpen = false;
					  } else {
						  drawOpen = true;
					}
				} 
			});
		/////////////////////////////////////////////
		
		//RADIO BUTTON CALLING IF YOU DRAW INDICATORS
			jradio_indicators.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) { 
					  if(drawInd){
						  drawInd = false;
					  } else {
						  drawInd = true;
					}
				} 
			});
		/////////////////////////////////////////////
			jmenu_bar.add(menu);
		
				jmenu_bar.add(jtext_openprice);
				jmenu_bar.add(jtext_price);
				jmenu_bar.add(jtext_minprice);
				jmenu_bar.add(jtext_maxprice);
				jmenu_bar.add(jtext_volume);
				
				jmenu_bar.add(jtext_change);
				jmenu_bar.add(jtext_clock);
				
			jbut_exit.setBackground(col_secondary);
			jbut_nightmode.setBackground(col_secondary);
			jmenu_bar.add(jbut_nightmode);
			
			menu.setBackground(col_secondary);
			
				if(fullscreen){
					menu.addSeparator();
					jmenu_bar.add(jbut_exit);
				}
	}
	
}

