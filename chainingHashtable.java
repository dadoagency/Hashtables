import java.util.LinkedList;
import java.util.ListIterator;

public class chainingHashtable extends hashtable {
	public LinkedList[] hashTableChain;
	public int K;
	public int numFull;

	/**
	 * Calls super constructor to se the tableSize and instantiates the hashTable
	 * which is an array of linked lists
	 * 
	 * @param tableSize
	 *            how big the hashtable is defined to be (prime number)
	 */
	public chainingHashtable(int tableSize) {
		super(tableSize);
		this.tableSize = tableSize;

		hashTableChain = new LinkedList[tableSize];

	}

	public void resetHashtable() {
		this.hashTableChain = new LinkedList[tableSize];
		this.probeCount = 0;
		this.numFull = 0;
	}

	public void setProbeCountSearch(int probeCountSearch) {
		this.probeCountSearch = probeCountSearch;
	}

	public void insert(String dataPoint) {
		String dateTime = dataPoint.substring(0, 19);
		int tryHashVal = hashVal(dateTime);
		if (hashTableChain[tryHashVal] == null) {
			put(tryHashVal, dataPoint);

		} else {
			chain(tryHashVal, dataPoint);
		}

	}

	public void put(int tryHashVal, String dataPoint) {
		hashTableChain[tryHashVal] = new LinkedList();
		hashTableChain[tryHashVal].add(dataPoint);
		numFull++;
	}

	public void chain(int tryHashVal, String dataPoint) {
		hashTableChain[tryHashVal].add(dataPoint);
		probeCount++;
		numFull++;
	}

	public double getLoadFactor() {
		double lFact = (double) numFull / tableSize;
		String lFactStr = lFact + "        ";
		return Double.valueOf(lFactStr.substring(0, 5));
	}

	public void traverse() {
		int count = 0;

		while (count < tableSize) {

			if (hashTableChain[count] != null) {
				int size = hashTableChain[count].size();
				if (size > 1) {
					int i = 0;
					while (i < size) {
						System.out.println("| " + hashTableChain[count].get(i)
								+ " | Index in linked list at cell number " + count + ": " + i);
						i++;
					}

				} else
					System.out.println("| " + hashTableChain[count].get(0) + " | Index in linked list at cell number "
							+ count + ": 0");

				count++;

			}
			count++;

		}

	}

	public String find(String query) {
		resetProbeCountSearch();
		boolean found = false;
		int hashVal = hashVal(query.toString().substring(0, 19));
		query = query.toString().substring(0, 19);
		ListIterator search = hashTableChain[hashVal].listIterator(0);
		int index = 0;
		String current;
		while (search.hasNext()) {
			current = search.next().toString();
			probeCountSearch++;
			String currentKey = current.substring(0, 19);
			if (currentKey.equals(query)) {
				found = true;
				return current;
			}
		}

		return "not found";

	}

}
