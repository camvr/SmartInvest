package com.finra.smartinvest;

import java.util.Random;

// Used for classifying types of investors
enum InvestorType {
	RATIONAL,
	IRRATIONAL,
	NAIVE
}

enum MarketMove {
	BUY,
	HOLD,
	SELL
}

public class StockMarket {
	private Random rand = new Random();
	
	private int initialStockPrice;
	private int prevStockPrice = 0;
	private int currStockPrice = 0;
	private int avgStockPrice = 0;
	private int prevTotalStocksOwned = 0;
	private int totalStocksOwned = 0;
	private double stockDeviationWeight = 1.0;
	private Investor[] investors;
	
	public StockMarket(int initialPrice, int numInvestors) {
		// Initialize stock market variables
		this.initialStockPrice = initialPrice;
		this.prevStockPrice = initialPrice;
		this.currStockPrice = initialPrice;
		this.avgStockPrice = initialPrice;
		investors = new Investor[numInvestors];
		
		for (int i = 0; i < numInvestors; i++) {
			InvestorType type;
			int typeDist = rand.nextInt(100);
			if (typeDist < 45)
				type = InvestorType.RATIONAL;
			else if (typeDist < 90)
				type = InvestorType.IRRATIONAL;
			else
				type = InvestorType.NAIVE;
			
			int stocksOwned = rand.nextInt(50);
			if (stocksOwned < 10)
				stocksOwned = 0;	// not invested yet
			
			this.totalStocksOwned += stocksOwned;
						
			investors[i] = new Investor(type, stocksOwned*this.initialStockPrice, stocksOwned, rand.nextDouble(), rand.nextDouble()/10);
		}
	}
	
	public MarketEvent runMarket(int phases, MarketEvent event) {
		for (int i = 0; i < phases; i++) {
			this.prevTotalStocksOwned = this.totalStocksOwned;
			
			for (Investor investor : investors) {
				switch(investor.applyStrategy(currStockPrice, prevStockPrice, avgStockPrice)) {
				case BUY:
					int amountBought = rand.nextInt(9) + 1;
					investor.buyStocks(amountBought, currStockPrice);
					this.totalStocksOwned += amountBought;
					break;
					
				case SELL:
					int amountSold = investor.sellStocks();
					this.totalStocksOwned -= amountSold;
					break;
					
				default:
					// Just continue otherwise
					break;
				}
			}
			
			// Update Random Walk stock price after this phase
			this.prevStockPrice = this.currStockPrice;
			this.avgStockPrice += this.prevStockPrice;
			this.avgStockPrice /= 2;
			this.currStockPrice = (int)((1 + ((this.totalStocksOwned - this.prevTotalStocksOwned) / this.totalStocksOwned)) * this.stockDeviationWeight * this.prevStockPrice);
			this.currStockPrice += randomWalk(event);
			
			if (this.currStockPrice < 0.2 * this.prevStockPrice)
				return MarketEvent.CRASH;
			
			// generate new stock market event
			int eventRange = rand.nextInt(300);
			if (eventRange < 200)
				event = MarketEvent.NORMAL;
			else if (eventRange < 270)
				event = MarketEvent.INSIDETRADING;
			else if (eventRange < 280) {
				event = MarketEvent.BADNEWS;
			} else {
				event = MarketEvent.FAILURE;
			}
		}
		
		return MarketEvent.NORMAL;
	}
	
	// This function adds the randomness to the stock market
	public int randomWalk(MarketEvent event) {
		switch(event) {
		case FAILURE:
			return (int) ((0.5 - rand.nextDouble() - 0.5) * this.currStockPrice);
		case BADNEWS:
			return (int) ((-rand.nextDouble() + 0.3) * this.currStockPrice);
		case INSIDETRADING:
			return rand.nextInt(8);
		default:
			return rand.nextInt(8) - 4;	// same as the normal case
		}
	}
	
	public int getInitialStockPrice() {
		return this.initialStockPrice;
	}
	
	public int getPrevStockPrice() {
		return this.prevStockPrice;
	}
	
	public int getCurrStockPrice() {
		return this.currStockPrice;
	}
	
	public int getAvgStockPrice() {
		return this.avgStockPrice;
	}
	
	class Investor {
		private InvestorType type;
		private int stocksOwned;
		
		private double greedyValue;
		private double learningRate;
		private Random rand = new Random();
		
		public Investor(InvestorType type, int amountInvested, int stocksOwned, double greedyValue, double learningRate) {
			this.type = type;
			this.stocksOwned = stocksOwned;
			this.greedyValue = greedyValue;
			this.learningRate = learningRate;
		}
		
		public MarketMove applyStrategy(int currPrice, int prevPrice, double theoreticalPrice) {
			double decision;
			int decisionWeight = 0;
			switch(this.type) {
			case RATIONAL:
				// Applies Fundamental analysis to determine move
				double r1 = 0.05 + (rand.nextDouble() * 0.05);
				
				if ((theoreticalPrice - currPrice) / currPrice < -r1) {
					// Price is over-valued -- Price will fall
					decisionWeight = -1;
				} else if ((theoreticalPrice - currPrice) / currPrice > r1) {
					// Price is under-valued -- Price will rise
					decisionWeight = 1;
				}
				
				decision = rand.nextInt(4) + (this.learningRate * decisionWeight) + this.greedyValue;
				
				// Rational investors prefer Buy-Hold
				if (decision <= 1.0)
					return MarketMove.SELL;
				else if (decision >= 2.5)
					return MarketMove.BUY;
				return MarketMove.HOLD;
				
			case IRRATIONAL:
				// Applies Chase-sell to determine move
				double r2 = 0.02 + (rand.nextDouble() * 0.03);
				
				if ((currPrice - prevPrice) / prevPrice < -r2) {
					// Price will fall
					decisionWeight = -1;
				} else if ((currPrice - prevPrice) / prevPrice > r2) {
					// Price will rise
					decisionWeight = 1;
				}
				
				decision = rand.nextInt(4) + (this.learningRate * decisionWeight) + this.greedyValue;
				
				// Irrational investors prefer Buy-Sell
				if (decision <= 1.5)
					return MarketMove.SELL;
				else if (decision >= 2.5)
					return MarketMove.BUY;
				return MarketMove.HOLD;
			case NAIVE:
				// determines move based solely on stock price
				if (prevPrice < currPrice)
					return MarketMove.BUY;
				return MarketMove.SELL;
			default:
				// should never reach this point, so holding should be safe
				return MarketMove.HOLD;
			}
		}
		
		public void buyStocks(int amount, int currPrice) {
			this.stocksOwned += amount;
			this.learningRate += rand.nextDouble() / 10;
		}
		
		public int sellStocks() {
			int s = this.stocksOwned;
			this.stocksOwned = 0;
			this.learningRate += rand.nextDouble() / 10;
			return s;
		}
	}
}
