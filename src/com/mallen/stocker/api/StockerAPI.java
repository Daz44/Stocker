package com.mallen.stocker.api;

import com.mallen.stocker.core.Stock;

public class StockerAPI {
	private Stock stock;
	
	public StockerAPI(String stock_symbol){
		stock = new Stock(stock_symbol, 10000, false);
		stock.updateShare();
	}
	public double price(){
		return stock.prices[stock.ticks-1];
	}
	public double minPrice(){
		return stock.MIN_PRICE;
	}
	public double maxPrice(){
		return stock.MAX_PRICE;
	}
	public void update(){ stock.updateShare();}
	public String getHashData(String s){
		return stock.getShare(s);
	}
}
