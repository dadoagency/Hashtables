import java.util.LinkedList;
import java.util.ArrayList;

public class quadraticHashtable extends hashtable {

	private static final Exception probingFailureException = null;
	public int i = 0;

	public quadraticHashtable(int tableSize) {
		super(tableSize);
	}

	/**
	 * uses the put and probing methods to attempt to insert data from the array
	 * dataList into a hashTable
	 * 
	 * @throws Exception
	 */
	public void insert(String dataPoint) throws Exception {

		String dateTime = dataPoint.substring(0, 19);
		int tryHashVal = hashVal(dateTime);
		if (hashTable[tryHashVal] == null) {
			super.put(tryHashVal, dataPoint);
		} else {
			if (probeCount > tableSize) {
				throw probingFailureException;
			}
			i = 0;
			quadraticProbe(tryHashVal, dataPoint);
		}
	}

	/**
	 * find new potential hash values until one is viable (if there is space) and
	 * then inserts at said position
	 * 
	 * @param tryHashVal
	 *            potential hash value (if there is space)
	 * @param dataPoint
	 *            the data to be inserted
	 */
	private void quadraticProbe(int tryHashVal, String dataPoint) {
		boolean available = false;
		while (!available) {

			if ((hashTable[tryHashVal] != null)) {
				// tryHashVal = (tryHashVal + (tryHashVal << 1));
				tryHashVal = tryHashVal + i * i;
				tryHashVal %= tableSize;
				i++;
				probeCount++;

			} else {
				super.put(tryHashVal, dataPoint);
				available = true;
			}
		}
	}

	/**
	 * searches for a query in the hashtable, following the same probing path that
	 * would be followed if the key was used to insert data initially
	 */
	public String find(String query) {
		int j = 0;
		resetProbeCountSearch();
		int hashValue = hashVal(query.toString().substring(0, 19));

		while (hashTable[hashValue] != null) {
			probeCount++;
			probeCountSearch++;
			if (hashTable[hashValue].substring(0, 19).equals(query.trim())) {
				return hashTable[hashValue];
			}
			hashValue = hashValue + j * j;
			hashValue %= tableSize;
			j++;
		}
		return null;
	}
}
