package com.mallen.stocker.core;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

public class Stock {
	
	public boolean testrun = false;
	/*
	 * USE THE PRICES ARRAY ANDA SCROLL BAR TO MANIPULATE THE TICKS VALUE
	 * THIS WILL ALLOW YOU TO REWIND THE DATA 
	 * 
	 * REFACTOR RENDERING ENGINE INTO THE STOCK FIELD, ALLOWING MULTIPLE STOCKS SIMULATANOUSLY
	 * 
	 * ALLOW DATA LOGGING
	 */
	
	public Double[] prices;
	private HashMap share = new HashMap();
	private String symb = "";
	public Double START_PRICE, MAX_PRICE = 0.0, MIN_PRICE = 1000000.0;
	public int ticks = 0;

	public Stock(String insymb, int size, boolean intest){
		prices = new Double[size];
		symb= insymb;
		testrun = intest;
	}
	
	public String getSymb(){
		return symb;
	}
	
	public void updateShare(){
		try {
			ticks++;
		
		if(ticks > prices.length) ticks = 5;	
			
		if(!testrun){
			URL y = new URL("http://finance.yahoo.com/d/quotes.csv?s="+ symb + "&f=l1vr2ejkghm3j3");		
			URLConnection url;

			
			url = y.openConnection();
			InputStreamReader isr = new InputStreamReader(url.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			
			String[] data = new String[8];		
			String line = br.readLine();
			
			data = line.split(",");
			prices[ticks] = Double.parseDouble(data[0]);
			
			System.out.println(ticks + ":" + line);
	
			share.put("price", data[0]);					//PARSING THE PRICE INTO A HASH TABLE
			share.put("volume", data[1]);					//PARSING THE VOLUME INTO A HASH TABLE
			share.put("eps", data[3]);						//PARSING THE EARNING PER SHARE INTO A HASH TABLE
			share.put("52max", data[4]);					//PARSING YEAR MAX INTO A HASH TABLE
			share.put("52min", data[5]);					//PARSING YEAR MIN INTO A HASH TABLE
			
			prices[ticks] = Double.parseDouble(share.get("price").toString());
		}	
			
			Double delta = (double) System.currentTimeMillis();
			
		if(testrun){
			if(ticks > 3){	
				Double addnum = 0d;
				Random r = new Random();
				
				addnum = r.nextInt(10)/1000d;
				if(r.nextInt(1000) > 500){
					addnum = addnum*-1;
				}
				prices[ticks] = prices[ticks-1] + addnum;
			} else {
				prices[ticks] = 6.33;
			}
		}
			
			/* VALUE 1 = TRADING VALUE
			 * VALUE 2 = VOLUME
			 * VALUE 3 = UNKNOWN
			 * VALUE 4 = EPS (Earning per share > http://www.investopedia.com/terms/e/eps.asp)
			 * VALUE 5 = 52WK MIN
			 * VALUE 6 = 52WK MAX
			 * VALUE 7 = DAY MIN
			 * VALUE 8 = DAY MAX
			 * VALUE 9 = ASK PRICE
			 */
		
			
		
		
		} catch(Exception e){
			NotificationFactory.notify("Unable to connect, check your connection and try again!", "NO URL");
		}
	
		for(int i = 0; i < prices.length; i++){
			if(prices[i] != null){
				DecimalFormat df = new DecimalFormat("#.000");
				if(MAX_PRICE < prices[i]){			
					MAX_PRICE = Double.parseDouble(df.format(prices[i]));
				}
				if(MIN_PRICE > prices[i]){			
					MIN_PRICE = Double.parseDouble(df.format(prices[i]));
				}
			if(MAX_PRICE == 0){
				MAX_PRICE = START_PRICE;
			}
			}
		}
		
		for(int i = 0; i < prices.length; i++){
			if(prices[i] != null){
				START_PRICE = prices[i];
				break;
			}
		}
		if(MAX_PRICE == 0){
			MAX_PRICE = START_PRICE;
		}
		
	}
	
	
	/**
	 * @param index price, volume, eps, 52min, 52max
	 * @return
	 */
	public String getShare(String index){
		return (String) share.get(index);
		}
}
