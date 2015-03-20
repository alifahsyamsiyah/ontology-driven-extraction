/*
 * This class is implemented similar to XTraceIterator class in OpenXES
 * 
 */
package EventLog;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.deckfour.xes.model.XTrace;


public class XLogOnDemandIterator implements ListIterator<XTrace> {

	protected XLogOnDemandImpl log;
	protected String[] traceIDs = null;
	protected int position = 0;

	public XLogOnDemandIterator(XLogOnDemandImpl log, String[] traceIDs) {
		this(log, traceIDs, 0);
	}

	public XLogOnDemandIterator(XLogOnDemandImpl alog, String[] atraceIDs, int aPosition) {
		log = alog;
		traceIDs = atraceIDs;
		position = aPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		System.out.println("enter hasnext in XLog");
		return (position < traceIDs.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	public XTrace next() {
		System.out.println("enter next in XLog");
		XTrace result = null;
		try {
			result = log.get(position);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException("There is no next event in this trace");
		} finally {
			position++;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#add(java.lang.Object)
	 */
	public void add(XTrace o) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#hasPrevious()
	 */
	public boolean hasPrevious() {
		return (position > 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#nextIndex()
	 */
	public int nextIndex() {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#previous()
	 */
	public XTrace previous() {
		position--;
		return log.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#previousIndex()
	 */
	public int previousIndex() {
		return (position - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.eventIDsIterator#set(java.lang.Object)
	 */
	public void set(XTrace o) {
	}

}
