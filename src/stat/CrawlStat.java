package stat;
/**
 * @author Maolin Tu
 */
public class CrawlStat {
	private long fetchAttempted = 0;
	private long fetchSucceed = 0;
	private long fetchFailed = 0;
	private long fetchAborted = 0;
	
	/**
	 * count of fetch Attempted add 1
	 * */
	public void addFetchAttempted() {
		fetchAttempted++;
	}
	
	/**
	 * get fetchAttempted
	 * @return fetchAttempted 
	 */
	public long getFetchAttempted() {
		return fetchAttempted;
	}
	/**
	 * count of fetch Succeed add 1
	 * */
	public void addFetchSucceed() {
		fetchSucceed++;
	}
	
	/**
	 * get fetchSucceed
	 * @return fetchSucceed 
	 */
	public long getFetchSucceed() {
		return fetchSucceed;
	}
	/**
	 * count of fetch Failed add 1
	 * */
	public void addFetchFailed() {
		fetchFailed++;
	}
	
	/**
	 * get fetchFailed
	 * @return fetchFailed 
	 */
	public long getFetchFailed() {
		return fetchFailed;
	}
	/**
	 * count of fetch Aborted add 1
	 * */
	public void addFetchAborted() {
		fetchAborted++;
	}
	
	/**
	 * get fetchAborted
	 * @return fetchAborted 
	 */
	public long getFetchAborted() {
		return fetchAborted;
	}
}
