package com.hvn.sensex.model;

import java.util.ArrayList;
import java.util.List;

public class ScenarioBuilder {
	static List<PortfolioConfig> configs = new ArrayList<PortfolioConfig>();
	
	static {
		configs.add(PortfolioConfig.create("2014-2018 - 30 Day MinMax")
				.setStartDay("2014-04-01")
				.setEndDay("2018-04-01")
				.setHighLowDaysRange(30));
		
		configs.add(PortfolioConfig.create("2014-2018 - 15 Day MinMax")
				.setStartDay("2014-04-01")
				.setEndDay("2018-04-01")
				.setHighLowDaysRange(15));
		
		configs.add(PortfolioConfig.create("2014-2018 - 15 Day MinMax - low value Stock")
				.setStartDay("2014-04-01")
				.setEndDay("2018-04-01")
				.setHighLowDaysRange(15)
				.setMinimumStockPriceToBuy(0)
				.setMaximumStockPriceToBuy(1000));

		configs.add(PortfolioConfig.create("2014-2018 - 15 Day MinMax - High value Stock")
				.setStartDay("2014-04-01")
				.setEndDay("2018-04-01")
				.setHighLowDaysRange(15)
				.setMinimumStockPriceToBuy(1000)
				.setMaximumStockPriceToBuy(10000));

		configs.add(PortfolioConfig.create("2014-2022 - 15 Day MinMax")
				.setStartDay("2014-04-01")
				.setEndDay("2022-04-01")
				.setHighLowDaysRange(15));

		configs.add(PortfolioConfig.create("2016-2021 - 15 Day MinMax")
				.setStartDay("2016-04-01")
				.setEndDay("2021-04-01")
				.setHighLowDaysRange(15));
		
		configs.add(PortfolioConfig.create("2014-2017 - 15 Day MinMax")
				.setStartDay("2014-04-01")
				.setEndDay("2017-04-01")
				.setHighLowDaysRange(15));
		
		configs.add(PortfolioConfig.create("2014-2017 - 15 Day MinMax - 1 lac max inv")
				.setStartDay("2014-04-01")
				.setEndDay("2017-04-01")
				.setHighLowDaysRange(15)
				.setMaxAmountToInvest(100000));

		
		configs.add(PortfolioConfig.create("2016-2020 - 15 Day MinMax")
				.setStartDay("2016-04-01")
				.setEndDay("2020-04-01")
				.setHighLowDaysRange(15));
		
		configs.add(PortfolioConfig.create("2016-2020 - 60 Day MinMax")
				.setStartDay("2016-04-01")
				.setEndDay("2020-04-01")
				.setHighLowDaysRange(60));
		
		
		configs.add(PortfolioConfig.create("2016-2020 - 30 Day MinMax - 1 percent fluctuation")
				.setStartDay("2016-04-01")
				.setEndDay("2020-04-01")
				.setHighLowDaysRange(30)
				.setBuyStoplossPercent(1.0)
				.setSaleStoplossPercent(1.0));


	}
	
	public static List<PortfolioConfig> getConfigs() {
		return configs;
	}
}
