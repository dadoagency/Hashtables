import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PowerHashApp {
	private static final Exception NoSpaceException = null;
	String[] dataList = new String[4000];
	public hashtable table;
	public String[] keysToTest = new String[400];

	public int[] searchSizes = { 653, 769, 883, 971, 1009 };

	/**
	 * Determines which child class of parent hashtable to instantiate depending on
	 * the parameter passed in, then goes on to either simply populate the table, or
	 * perform a series of K searches.
	 * 
	 * @param tableSize
	 *            the size of the hashtable in which the data will be inserted
	 * @param tableType
	 *            which scheme / child (linear, quadratic or chaining)
	 * @param dataFile
	 *            indicates the source of the data (of type csv)
	 * @param K
	 *            the number of random searches to perform
	 * @throws Exception
	 */
	public PowerHashApp(int tableSize, String tableType, String dataFile, int K) throws Exception {

		if (tableType.equals("l")) {
			if (tableSize < 500) {
				System.out.println("No space to insert data. Please run again with a table size greater than 500. ");
				throw NoSpaceException;
			}
			this.table = new linearHashtable(tableSize);
		}

		else if (tableType.equals("q")) {
			if (tableSize < 500) {
				System.out.println("No space to insert data. Please run again with a table size greater than 500. ");
				throw NoSpaceException;
			}
			this.table = new quadraticHashtable(tableSize);
		}

		else if (tableType.equals("c")) {
			this.table = new chainingHashtable(tableSize);
		}

		else if (tableType.equals("cT")) {
			this.table = new chainingHashtable(tableSize);
			readCSV(dataFile);
			this.table.traverse();
		}

		this.table.setTableSize(tableSize);
		if (K == 0) {
			readCSV(dataFile);
			System.out.println("Number of probes: " + table.probeCount + "\nLoad Factor:" + table.getLoadFactor());
		}

		else if (K == -5) {
			readCSV(dataFile);
			System.out.println("Random keys test: (Table sizes = { 653, 769, 883, 971, 1009 })");
			testKeys();
			System.out.println("Insertions test: ");
			testInsertions();

		}

		else {
			readCSV(dataFile);
			table.randomSearchAllProbes = new int[K];
			randomSearch(K);
		}

		//

	}

	/**
	 * reads in the data from a csv file and inserts it into an Array
	 * 
	 * @throws Exception
	 */
	public void readCSV(String dataFile) throws Exception {
		int count = 0;
		Scanner key = new Scanner(new File(dataFile));
		key.nextLine();
		key.useDelimiter(",|\n");

		try {
			while (key.hasNext() & count < (4000)) {
				String nextDataPoint = key.next();
				dataList[count] = (nextDataPoint);
				count++;

			}
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		populateTable();
	}

	/**
	 * takes the data from the array and uses it to populate a hashTable
	 * 
	 * @throws Exception
	 */
	public void populateTable() throws Exception {
		int indexPos = 0;

		while (indexPos < 4000) {
			if (dataList[indexPos].length() == 19) {
				table.insert(getRelevantData(indexPos));
				indexPos++;
			}

			indexPos++;
		}

	}

	/**
	 * gets the relevant 3 data points associated with a time and date at the given
	 * index position
	 * 
	 * @param indexPos
	 *            indicates which date and time data needs to be returned
	 */
	public String getRelevantData(int indexPos) {

		return dataList[indexPos] + " " + dataList[indexPos + 1] + " " + dataList[indexPos + 3];

	}

	/**
	 * finds the closest prime number to the one passed in as a parameter to ensure
	 * table size is a prime number
	 * 
	 * @param number
	 *            the one to be checked if prime and if it isn't the one to be used
	 *            to find a bigger prime number
	 * @return either initial number if prime or closest one that is bigger
	 */
	public static int closestPrime(int number) {
		int sqrt = (int) Math.sqrt(number) + 1;
		for (int i = 2; i < sqrt; i++) {
			if (number % i == 0) {
				System.out.println("The number you entered wasn't a prime number! \n"
						+ "We found the closest prime number though so the table size will be set that.\n");
				return closestPrime(number + 1);
			}
		}
		return number;

	}

	/**
	 * searches the hashtable from a random shuffled list of K search keys
	 * 
	 * @param K
	 *            the number of search keys
	 * @throws FileNotFoundException
	 *             in case the file with the search keys is not there
	 */

	public void randomSearch(int K) throws FileNotFoundException {

		int count = 0;
		String[] datesList = new String[500];
		Scanner dates = new Scanner(new File("onlyDates.txt"));
		dates.nextLine();
		dates.useDelimiter(",|\n");

		while (dates.hasNext() & count < (500)) {
			String nextDataPoint = dates.next();
			datesList[count] = (nextDataPoint);
			count++;

		}

		List<String> datesListt = Arrays.asList(datesList);
		Collections.shuffle(datesListt);
		datesListt.toArray();
		int i = 0;
		for (String el : datesListt) {
			if (i < K) {
				String query = "" + el;
				System.out.println("Find: " + table.find(query) + "| Probes to find: " + table.getProbeCountSearch());
				table.randomSearchAllProbes[i] = table.getProbeCountSearch();
				i++;
			}
		}
		Arrays.sort((table.randomSearchAllProbes));

		System.out.println("Longest probe: " + table.randomSearchAllProbes[K - 1]);
		System.out.println("Total number of probes: " + (sum(table.randomSearchAllProbes)));
		System.out.println("Average number of probes: " + (double) sum(table.randomSearchAllProbes) / K);

	}

	public void testKeys() throws Exception {
		int K = 400;
		int count = 0;
		Scanner dates = new Scanner(new File("testKeys.txt"));
		dates.useDelimiter("\n");

		while (dates.hasNext() & count < (400)) {
			String nextDataPoint = dates.next();
			keysToTest[count] = (nextDataPoint);
			count++;

		}

		List<String> datesListt = Arrays.asList(keysToTest);
		datesListt.toArray();
		System.out.println("Longest probe,TotalProbes,AverageProbe,LoadFactor");
		for (int j = 0; j < searchSizes.length; j++) {
			int i = 0;
			table.randomSearchAllProbes = new int[400];
			table.setTableSize(searchSizes[j]);
			table.resetHashtable();
			// table.resetRandomSearchAllProbes();
			table.resetProbeCountSearch();
			populateTable();

			for (String el : datesListt) {

				if (i < 400) {

					String query = "" + el;
					table.find(query);
					// System.out.println("find: " + table.find(query) + " Probes to find: " +
					// table.getProbeCountSearch());
					table.randomSearchAllProbes[i] = table.getProbeCountSearch();
					i++;
				}
			}
			Arrays.sort((table.randomSearchAllProbes));

			System.out.println(table.randomSearchAllProbes[K - 1] + "," + (sum(table.randomSearchAllProbes)) + ","
					+ (double) sum(table.randomSearchAllProbes) / K + "," + table.getLoadFactor());

		}
	}

	public void testInsertions() throws Exception {
		for (int i = 0; i < searchSizes.length; i++) {
			table.setTableSize(searchSizes[i]);
			table.resetHashtable();
			populateTable();
			System.out.println(table.probeCount + "," + table.getLoadFactor());

		}

	}

	/**
	 * returns the sum of all elements in an array
	 * 
	 * @param randomSearchAllProbes
	 *            a list of all the probe counts from a random search of K keys
	 * @return the result of adding all counts together
	 */
	private int sum(int[] randomSearchAllProbes) {
		int i = 0;
		int sum = 0;
		while (i < randomSearchAllProbes.length) {
			sum = sum + randomSearchAllProbes[i];
			i++;
		}
		return sum;
	}

	/**
	 * The user / marker inputs the necessary information to build and test a
	 * hashtable through random searches.
	 * 
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		Scanner key = new Scanner(System.in);
		if (args.length > 0) {
			new PowerHashApp(653, "l", "cleaned_data.csv", -5);
			System.out.println("Quadratic: ");
			new PowerHashApp(653, "q", "cleaned_data.csv", -5);
			System.out.println("Chaining: ");
			new PowerHashApp(653, "c", "cleaned_data.csv", -5);
		}

		else {
			System.out.println("Welcome \nTo build and test a hashtable we are going to need some information from you."
					+ "\nFirstly, enter the table size: ");
			int tableSize = closestPrime(Integer.parseInt(key.nextLine()));
			System.out.println("Which method do you want us to use to probe if there is a collision? "
					+ "\n- Linear Probing (l) \n- Quadratic Probing (q)\n- Chaining (c)");
			String method = key.nextLine();
			System.out.println("\nNow enter the name of the file that contains the data to be stored in the hashstable "
					+ "(eg. cleaned_data.csv): ");
			String dataFile = key.nextLine();
			System.out.println("Lastly, enter how many keys you want to search for: ");
			int K = key.nextInt();
			new PowerHashApp(tableSize, method, dataFile, K);

		}
	}

}
