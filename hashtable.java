import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * the parent hashtable that is used, along with abstract classes as a framework
 * for hashtables with different collision resolution schemes
 * 
 * @author shai
 *
 */
public abstract class hashtable {
	public static int tableSize;
	public int K;
	public int loadFact;
	public int numFull;
	public int probeCountSearch = 0;
	public int probeCount = 0;
	public int[] randomSearchAllProbes;
	public String[] hashTable;

	/**
	 * instantiates the String array to be of size tableSize
	 * 
	 * @param tableSize
	 */
	public hashtable(int tableSize) {
		hashTable = new String[tableSize];
	}

	public void resetHashtable() {
		this.hashTable = new String[tableSize];
		this.probeCount = 0;
		this.numFull = 0;
	}

	/**
	 * generates a hash value based on the char values that make up a key which is
	 * of type String
	 * 
	 * @param str
	 *            the key to be hashed
	 * @return the position to attempt an insert
	 */
	public int hashVal(String str) {

		int hashVal = 0;
		for (int count = 0; count < str.length(); count++) {
			hashVal = ((31 * hashVal) + str.charAt(count));
			hashVal %= tableSize;

		}
		hashVal %= tableSize;
		if (hashVal < 0) {
			hashVal += tableSize;
		}
		return hashVal;
	}

	public int getTableSize() {
		return tableSize;
	}

	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;

	}

	/**
	 * uses String methods to format a double
	 * 
	 * @return the load factor or the proportion of the table that is full
	 */
	public double getLoadFactor() {
		double lFact = (double) numFull / tableSize;
		String lFactStr = "" + lFact;
		return Double.valueOf(lFactStr.substring(0, 5));
	}

	// public abstract void getLoadFactor();

	public String[] getHashTable() {
		return hashTable;
	}

	public void setHashTable(String[] hashTable) {
		this.hashTable = hashTable;
	}

	public int getProbeCount() {
		return probeCount;
	}

	public void setProbeCount(int probeCount) {
		this.probeCount = probeCount;
	}

	/**
	 * to be implemented in the children classes
	 * 
	 * @param relevantData
	 *            to be inserted
	 * @throws Exception
	 */
	public abstract void insert(String relevantData) throws Exception;

	/**
	 * sets the index position determined by a hash function to be equal to data
	 * 
	 * @param hashVal
	 *            the index position from hash function
	 * @param data
	 *            the data to be inserted
	 */
	public void put(int hashVal, String data) {
		hashTable[hashVal] = data;
		numFull++;

	}

	public int getProbeCountSearch() {
		return probeCountSearch;
	}

	public void resetProbeCountSearch() {
		this.probeCountSearch = 0;
	}

	/**
	 * used to iterate through and print each non-null element in the hashtables.
	 * used for testing purposes to insure everything has been inserted
	 */
	public void traverse() {
		int count = 0;
		while (count < tableSize) {

			if (hashTable[count] != null) {
				System.out.println(hashTable[count]);
				count++;
			}
			count++;
		}

	}

	public abstract String find(String query);

	public void resetRandomSearchAllProbes() {
		this.randomSearchAllProbes = new int[400];

	}

}
