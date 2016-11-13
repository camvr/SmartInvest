package com.finra.smartinvest;

//Used for classifying various events that can randomly disrupt the market
public enum MarketEvent {
	NORMAL,
	CRASH,
	FAILURE,
	BADNEWS,
	INSIDETRADING
}
