/*
 * This class is implemented similar to XTraceIterator class in OpenXES
 * 
 */
package EventLog;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.deckfour.xes.model.XEvent;


public class XTraceOnDemandIterator implements ListIterator<XEvent> {

	protected XTraceOnDemandImpl trace;
	protected String[] eventIDs = null;
	protected int position = 0;

	public XTraceOnDemandIterator(XTraceOnDemandImpl trace, String[] eventIDs) {
		this(trace, eventIDs, 0);
	}

	public XTraceOnDemandIterator(XTraceOnDemandImpl atrace, String[] aeventIDs, int aPosition) {
		trace = atrace;
		eventIDs = aeventIDs;
		position = aPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		System.out.println("-----enter hasNext in trace");
		return (position < eventIDs.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	public XEvent next() {
		System.out.println("-----enter next in trace");
		
		XEvent result = null;
		try {
			result = trace.get(position);
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
	public void add(XEvent o) {
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
	public XEvent previous() {
		position--;
		return trace.get(position);
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
	public void set(XEvent o) {
	}

}
