package com.finra.smartinvest;

import java.util.Random;


public class QuestionGenerator {
	private static Random rand = new Random();
	
	private static StockMarket stockMarket = new StockMarket(rand.nextInt(80) + 20, rand.nextInt(100) + 50);
	
	public static int answerDeviation = 0;	// Start off with no bias
	/*
	 * The more difficult questions will have higher indices in the selection
	 * lists, so a higher answerDeviation will make getting those more likely.
	 */
	
	public static String[] generateQuestionAnswer() {
		String question = "", answer = "", correctAns = "";
		
		// Question variables
		MarketEvent currEvent = MarketEvent.NORMAL;
		String marketDir;
		int stockPriceBefore = stockMarket.getCurrStockPrice();
		int stockPriceAfter;
		MarketEvent otherEvent = MarketEvent.NORMAL;
		
		if (currEvent == MarketEvent.CRASH || otherEvent == MarketEvent.CRASH) {
			// If current simulation crashes, then restart as crashes will give low values too often.
			stockMarket = new StockMarket(rand.nextInt(80) + 20, rand.nextInt(100) + 50);
		}
		
		switch(rand.nextInt(2)) {
		case 0:
			// Market planning
			currEvent = stockMarket.runMarket(rand.nextInt(15) + 1, MarketEvent.NORMAL);
			
			if (rand.nextBoolean())
				marketDir = "rise";
			else
				marketDir = "fall";
			
			question = "After observing the market for a brief period since investing, you find that the\naverage stock price was " 
							+ stockMarket.getAvgStockPrice() + ", and the last price the stock was\nrecorded at was " + stockMarket.getCurrStockPrice() 
							+ ". An investor tells you that he estimates\nthe stock price will " + marketDir + ".\n\nDo you agree with what he says?";
			
			if ((stockMarket.getAvgStockPrice() - stockMarket.getCurrStockPrice()) / stockMarket.getCurrStockPrice() > 0.05 + (rand.nextDouble() * 0.05)) {
				if (marketDir.equals("rise")) {
					correctAns = "yes";
					answer = "The market shows evidence of rising since it is statistically under-valued.\n";
				} else {
					correctAns = "no";
					answer = "The market shows evidence of falling since it is statistically over-valued.\n";
				}
			} else if (marketDir.equals("fall")) {
				correctAns = "yes";
				answer = "The market shows evidence of falling since it is statistically over-valued.\n";
			} else {
				correctAns = "no";
				answer = "The market shows evidence of rising since it is statistically under-valued.\n";
			}
				
			answer += "It is very important to have a good idea for yourself about where the value of an\ninvestment is going. There are many methods "
					+ "we can use to help model, \none of which is where we compare:\n"
					+ "(average Price - current Price) / current Price > A value between 5% and 10%, which we say shows good\nevidence that a stock"
					+ "is under-valued and will increase in value, as well as:\n(average Price - current Price) / current Price < a value between -10% "
					+ "and -5%,\nwhich we says shows good evidence a stock is over-valued and will fall in price. \nUsing these models over large spans "
					+ "of data and using reasoning with our\n\"Risk to Reward\" ratios we can make smart investments.";
			
			break;
			
			
		// Market catastrophe
		case 1:
			otherEvent = MarketEvent.BADNEWS;
			currEvent = stockMarket.runMarket(rand.nextInt(1) + 1, otherEvent);
			stockPriceAfter = stockMarket.getCurrStockPrice();
			if (rand.nextBoolean())
				marketDir = "won't fall";
			else
				marketDir = "fall";
			question = "Last night, the director of an Areo-space technology firm that held the interest of many\nmajor stock investors "
					 + "had annouced that some recent studies of their new technologies\n had been flawed and that the development had "
					 + "gone backwards to prototyping.\nHe said that it may be a few years before they can get the technology back to where "
					 + "were\nlast. A financial expert later said that the market will most likely " + marketDir + ".\nDo you agree with his "
					 + "prediction?";
			
			if (stockPriceBefore - 10 > stockPriceAfter) {
				if (marketDir.equals("fall"))
					correctAns = "yes";
				else
					correctAns = "no";
			} else if (marketDir.equals("won't fall")) {
				correctAns = "yes";
			} else {
				correctAns = "no";
			}
			
			answer = "Although it may be surprsing, even a small piece of bad news can have a very large\nimpact on the market. This is "
					+ "mainly due to how investors react to this news,\noften times selling their stocks frantically, which cause a massive "
					+ "dip in the stock value.\nThis may not always be the case, but in this case it " + (stockPriceBefore - 10 > stockPriceAfter ? "was." : "wasn't.");
			
			break;
		case 2:
			if (rand.nextBoolean())
				marketDir = "fall, but then jump back up again";
			else
				marketDir = "be lost money";
			otherEvent = MarketEvent.FAILURE;
			currEvent = stockMarket.runMarket(rand.nextInt(1) + 1, otherEvent);
			stockPriceAfter = stockMarket.getCurrStockPrice();
			question = "A Bio-technology research firm's recent studies had shown to not be the same\nresults as they once had shown to "
					 + "stockholders before, which caused a huge\nmarket rise for their company. Your friend who had also bought a stock "
					 + "in this technology had said\nthat he thinks their stocks will " + marketDir + ". Do you agree with his prediction?";
			
			if (marketDir.equals("be lost money")) {
				correctAns = "yes";
			} else {
				correctAns = "no";
			}
			
			answer = "Biotech companies are a very hih risk imvestment, that 85% to 90% of the time fail,\nwith often times 95% losses "
					+ "to stock owners. When a failure hits a market, that market\nhas very little chance of building itself back up "
					+ "again, especially overnight,\nwhich is the time frame of this problem.";
			
			break;
		case 3:
			otherEvent = MarketEvent.CRASH;
			currEvent = stockMarket.runMarket(rand.nextInt(1) + 1, otherEvent);
			stockPriceAfter = stockMarket.getCurrStockPrice();
			question = "A company that keeps its financial records under the table had recently begun\nto have a very large influx of "
					 + "inexperienced stock traders buying into it.\nDo you think that this will have a major impact on the market?";
			
			correctAns = "yes";
			
			answer = "Companies that keeps their financial records hiden are often dodgey to invest in,\nand if many inexperienced stock "
					+ "traders begin investing, this will cause\na market to crash as inexperienced traders will often buy and sell rapidly.";
			
			break;
		default:
			break;
		}
		
		String[] qaStrings = {question, answer, correctAns};
		return qaStrings;
	}
}
