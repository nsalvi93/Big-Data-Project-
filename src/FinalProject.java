import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class FinalProject {
	public static void main(String args[]) throws IOException {

		Scanner o = new Scanner(System.in);
		String yearName = "";
		int choice = 0;
		TennisAnalysis tennis;

		System.out.println("Welcome to Tennis Data Analyis!");
		while (true) {
			System.out.println("\n1. Please Enter your choice of Year 2013 2014 2015 \nEnter Exit to quit");
			yearName = o.next();
			if (yearName.toLowerCase().compareTo("exit") == 0) {
				System.out.println("Thanks!.....");
				break;
			}
			tennis = new TennisAnalysis(yearName);

			System.out.println("Please Enter your choice of Analysis \n 1. Tournament \n 2. Player Odds \n 3. Exit");
			choice = o.nextInt();
			if (choice == 1) {

				System.out.println(
						" Please Enter your choice of Analysis \n 1. Exciting Tournaments \n 2. Major Upsets Count \n 3. Exit");
				choice = o.nextInt();
				if (choice == 1)
					tennis.GetExcitement();
				if (choice == 2)
					tennis.GetUpset();
			} else if (choice == 2) {
				tennis.playerOdds();
			} else {
				System.out.println("Thanks!.....");

				break;
			}

			System.out.println("Enter Yes to continue or No to exit");
			yearName=o.next();
			if (yearName.toLowerCase().compareTo("exit") == 0) {

				System.out.println("Thanks!.....");
				break;
			}
		}

	}

}

class TennisAnalysis {

	File excel;
	FileInputStream fis;
	XSSFWorkbook wb;
	XSSFSheet ws;
	Scanner o;
	JFreeChart Chart;

	ChartPanel chartPanel;
	JFreeChart Chart1;

	ChartPanel chartPanel1;

	TennisAnalysis(String year) throws IOException {

		o = new Scanner(System.in);
		excel = new File(year + ".xlsx");
		fis = new FileInputStream(excel);
		wb = new XSSFWorkbook(fis);
		ws = wb.getSheet(year);

	}

	public void GetExcitement() throws IOException 
	{
		System.out.println("PLease enter type of analysis \n 1. Data 2. Graphical");
		int temp123 = o.nextInt();
		Map<String, Integer> tempMap = new LinkedHashMap<String, Integer>();

		for (Row row : ws) {
			// System.out.println(row.getRowNum());
			Cell noOfSets = row.getCell(8);
			Cell winSets = row.getCell(25);
			Cell lostSets = row.getCell(26);

			if (lostSets == null || lostSets.getCellType() != 0) {
				continue;
			}

			else {
				int temp = (int) (winSets.getNumericCellValue() + lostSets.getNumericCellValue());
				if ((temp == (int) noOfSets.getNumericCellValue())) {

					Cell cell = row.getCell(2);
					String temp1 = cell.toString();

					if (tempMap.containsKey(temp1)) {
						int tempInt = tempMap.get(temp1);
						tempInt++;
						tempMap.put(temp1, tempInt);

					} else {
						int tempInt = 0;
						tempMap.put(temp1, tempInt);
					}

				}
			}
		}
		if(temp123 == 1)
		{
			printMap(tempMap);
		}
		else
		{
			System.out.println("Enter key \n 1. Analysis of major exciting tournaments 2. All tournaments");
			int choice = o.nextInt();

			Chart = ChartFactory.createBarChart("Tournament analysis","Tournament", "Excitement Factor", createExcitementDataSet(tempMap, choice),  PlotOrientation.VERTICAL,           
					true, true, false);
			ChartPanel chartPanel = new ChartPanel( Chart );        
			/*chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
		      setContentPane( chartPanel ); */    
			String filename = "chartExcitement.jpg";
			CategoryPlot cp = Chart.getCategoryPlot();  
			cp.setBackgroundPaint(Color.white);       
			cp.setRangeGridlinePaint(Color.RED);
			BarRenderer br = (BarRenderer) cp.getRenderer();
			br.setItemMargin(-2);
			br.setMaximumBarWidth(.55);
			ChartUtilities.saveChartAsJPEG(new File(filename), Chart, 500, 800); 
		}

	}

	private CategoryDataset createExcitementDataSet(Map<String, Integer> tempMap, int choice) 
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

		int compare =0;
		if(choice == 1)
		{
			for(String temp : tempMap.keySet())
			{
				compare = tempMap.get(temp);
				if(compare > 20)
				{
					dataset.addValue(tempMap.get(temp), temp, temp);
				}
			}
		}
		else
		{

			for(String temp : tempMap.keySet())
			{
				for(String temp1 : tempMap.keySet())
				{

					dataset.addValue(tempMap.get(temp), temp, temp);

				}
			}
		}
		return dataset;
	}

	public void printMap(Map<String, Integer> graphMap12) 
	{
		System.out.println("Entered");
		Set setOfKeys = graphMap12.keySet();
		Iterator iterator = setOfKeys.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			int list = graphMap12.get(key);
			System.out.println(  key   +" "+ list);
		}

	}

	public void GetUpset() throws IOException {
		System.out.println("PLease enter type of analysis \n 1. Data 2. Graphical");
		int temp123 = o.nextInt();

		Map<String, Integer> tempMap = new LinkedHashMap<String, Integer>();
		for (Row row : ws) {
			// System.out.println(row.getRowNum());
			Cell winnerRank = row.getCell(11);
			Cell loserRank = row.getCell(12);
			String status = row.getCell(27).toString();
			if (loserRank.getCellType() != 0 || winnerRank.getCellType() != 0 || status == "Retired") {
				continue;
			} else {
				int difference = (int) (winnerRank.getNumericCellValue() - loserRank.getNumericCellValue());
				if (difference > 30) {
					Cell cell = row.getCell(2);
					String temp1 = cell.toString();

					if (tempMap.containsKey(temp1)) {
						int tempInt = tempMap.get(temp1);
						tempInt++;
						tempMap.put(temp1, tempInt);

					} else {
						int tempInt = 0;
						tempMap.put(temp1, tempInt);
					}
				}
			}
		}

		if(temp123 == 1)
		{
		printMap(tempMap);
		}
		
		else
		{
			System.out.println("Enter key \n 1. Analysis of upsets in major tournaments 2. All tournaments");
			int choice = o.nextInt();

			Chart1 = ChartFactory.createBarChart("Tournament analysis","Tournament", "Upsets", createUpsetDataSet(tempMap, choice),  PlotOrientation.VERTICAL,           
					true, true, false);
			//ChartPanel chartPanel1 = new ChartPanel( Chart );        
			/*chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
		      setContentPane( chartPanel ); */    
			String filename = "chartupset.jpg";
			CategoryPlot cp = Chart1.getCategoryPlot();  
			cp.setBackgroundPaint(Color.white);       
			cp.setRangeGridlinePaint(Color.RED);
			BarRenderer br = (BarRenderer) cp.getRenderer();
			br.setItemMargin(-2);
			br.setMaximumBarWidth(.55);
			ChartUtilities.saveChartAsJPEG(new File(filename), Chart1, 500, 800); 
		}


	}

	private CategoryDataset createUpsetDataSet(Map<String, Integer> tempMap, int choice) 
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

		int compare =0;
		if(choice == 1)
		{
			for(String temp : tempMap.keySet())
			{
				compare = tempMap.get(temp);
				if(compare > 10)
				{
					dataset.addValue(tempMap.get(temp), temp, temp);
				}
			}
		}
		else
		{

			for(String temp : tempMap.keySet())
			{
				for(String temp1 : tempMap.keySet())
				{

					dataset.addValue(tempMap.get(temp), temp, temp);

				}
			}
		}
		return dataset;
		
		
	}

	public Map<String, Double> playerMap() {

		Map<String, Double> PlayerIndex = new LinkedHashMap<String, Double>();

		double temp = 0;

		for (Row row : ws) {
			Cell cell = row.getCell(9);
			if (PlayerIndex.get(cell.toString()) == null)
				PlayerIndex.put(cell.toString(), temp);

			cell = row.getCell(10);
			if (PlayerIndex.get(cell.toString()) == null)
				PlayerIndex.put(cell.toString(), temp);

		}

		PlayerIndex.remove("Winner");
		PlayerIndex.remove("Loser");

		return PlayerIndex;
	}

	public double getProfitLoss(String name, String siteName) {

		Cell cell;

		double value = 0;

		int site = 37;
		Row row1 = ws.getRow(0);

		if (row1.getLastCellNum() > 40)
			site = 39; // By default Max value

		if (siteName.compareTo("B365") == 0)
			site = 29;

		else if (siteName.compareTo("EX") == 0)
			site = 31;

		else if (siteName.compareTo("LB") == 0)
			site = 33;

		else if (siteName.compareTo("PS") == 0)
			site = 35;

		else if (siteName.compareTo("SJ") == 0)
			site = 37;

		for (Row row : ws) {

			if (row.getRowNum() != 0) {

				cell = row.getCell(9);

				Cell winner = row.getCell(site);
				if (cell.toString().compareTo(name) == 0) {
					if (winner != null && winner.getCellType() == (winner.CELL_TYPE_NUMERIC))
						value += winner.getNumericCellValue();
				}

				cell = row.getCell(10);
				if (cell.toString().compareTo(name) == 0)
					value -= 1;
			}
		}
		return value;
	}

	public void playerOdds() {

		Map<String, Double> PlayerIndex = playerMap();

		while (true) {
			System.out.println("\n\nOptions \n 1. All Players \n 2. Particular Player \n 3. Top 10 \n 4. Main Menu");
			int choice = o.nextInt();

			if (choice == 4)
				return;

			System.out.println("Please enter betting website name \n 1. B365 2. EX 3. LB 4. PS 5. SJ 6. Max Value ");
			String siteName = o.next();

			// For All Players

			if (choice == 1) {

				/*System.out.println("Player Name \t\t Profit Factor");
				for (Map.Entry<String, Double> entry : PlayerIndex.entrySet()) {
					String key = entry.getKey();
					PlayerIndex.replace(key, getProfitLoss(key, siteName));

					System.out.println(key + "\t\t" + PlayerIndex.get(key));*/
				int temp=1;
				System.out.println("No. Player Name \t\t Profit Factor");
				for (Map.Entry<String, Double> entry : PlayerIndex.entrySet()) {
					String key = entry.getKey();
					PlayerIndex.replace(key, getProfitLoss(key, siteName));

					System.out.println((temp++)+" "+key + "\t\t" + PlayerIndex.get(key));
				}

			}

			// For Individual Player

			if (choice == 2) {

				System.out.println("Enter Player Name: ");
				String key = o.next();

				// Finding exact key from user given player name
				String nameSuggestions[] = new String[100];
				int index = 0;
				for (Map.Entry<String, Double> entry : PlayerIndex.entrySet()) {
					String actualKey = entry.getKey();
					if (actualKey.toLowerCase().contains((CharSequence) key.toLowerCase())) {

						nameSuggestions[index++] = actualKey;

					}
				}

				// Giving suggestions to user if more than one name found
				boolean flag = false;
				if (index == 1)
					key = nameSuggestions[0];
				else {

					System.out.println("Player name suggestions: Enter 1 if yes or 0 for next name");

					for (int i = 0; i < index; i++) {
						System.out.println("Is it " + nameSuggestions[i] + " ?");
						if (o.nextInt() == 1) {
							key = nameSuggestions[i];
							flag = true;
							break;
						}

					}

					// Player not found
					if (!flag) {
						System.out.println("Sorry, we cannot find the data for player with name : " + key);
						return;
					}
				}

				PlayerIndex.replace(key, getProfitLoss(key, siteName));

				System.out.println("Profit factor for " + key + ":  " + PlayerIndex.get(key));

			}

			if (choice == 3) {

				int index = 0;
				String topTen[] = new String[10];

				for (Map.Entry<String, Double> entry : PlayerIndex.entrySet()) {
					String key = entry.getKey();
					PlayerIndex.replace(key, getProfitLoss(key, siteName));

					if (index < 10)
						topTen[index++] = key;
					else {

						sort(PlayerIndex, topTen);
						if (PlayerIndex.get(topTen[9]) < PlayerIndex.get(key))
							topTen[9] = key;

					}

				}

				sort(PlayerIndex, topTen);

				System.out.println("Top Ten Players");
				System.out.println("Player Name \t\t Profit Factor");
				for (int i = 0; i < topTen.length; i++)
					System.out.println(topTen[i] + "\t\t" + PlayerIndex.get(topTen[i]));

			}
		}
	}

	void sort(Map<String, Double> m, String a[]) {

		String tempName;

		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a.length; j++) {
				if (m.get(a[i]) > m.get(a[j])) {
					tempName = a[i];
					a[i] = a[j];
					a[j] = tempName;
				}
			}
	}
}