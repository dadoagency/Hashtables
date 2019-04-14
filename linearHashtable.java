import java.util.ArrayList;
import java.util.NoSuchElementException;

public class linearHashtable extends hashtable {
	public int numFull;

	public linearHashtable(int tableSize) {
		super(tableSize);

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
	public int linearProbe(int tryHashVal, String dataPoint) {

		boolean available = false;

		while (!available) {
			probeCount++;
			if (hashTable[tryHashVal] != null) {
				tryHashVal = (tryHashVal + 1);
				tryHashVal %= tableSize;
			} else {
				super.put(tryHashVal, dataPoint);
				this.numFull++;
				available = true;
			}

		}

		return tryHashVal;
	}

	/**
	 * searches for a query in the hashtable, following the same probing path that
	 * would be followed if the key was used to insert data initially
	 */

	public String find(String query) {
		resetProbeCountSearch();
		query = query.toString().substring(0, 19);
		int hashValue = hashVal(query);

		while (hashTable[hashValue] != null) {
			probeCount++;
			probeCountSearch++;
			if (hashTable[hashValue].substring(0, 19).equals(query)) {

				return hashTable[hashValue];
			}

			hashValue = (hashValue + 1);
			hashValue %= tableSize;
		}
		return null;
	}

	public void insert(String dataPoint) {
		String dateTime = dataPoint.substring(0, 19);
		int tryHashVal = hashVal(dateTime);

		if (hashTable[tryHashVal] == null) {

			super.put(tryHashVal, dataPoint);
			this.numFull++;

		} else {
			linearProbe(tryHashVal, dataPoint);
		}
	}

	// public static void main(String[] args) throws Exception {
	// String[] a = new String[3];
	// a[0] = "h";
	// a[1] = "b";
	// a[2] = "f";
	//
	// for (int i = 0; i < a.length; i++) {
	// System.out.println("| " + a[i] + " | " + i);
	// }
	// }

}
