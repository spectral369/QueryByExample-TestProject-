/**
 * 
 */
package com.querybyexample.functionality;

import java.util.Vector;

/**
 * @author spectral369
 *
 */
public class QueryData {
	Vector<?> QBECols = null;
	Vector<?> data = null;
	int length = 0;

	public void setLength(int length) {
		this.length = length;
	}

	public void setQBECols(Vector<Object> cols) {
		this.QBECols = cols;
	}

	public Vector<?> getQBECols() {
		if (length == 0)
			return null;
		else
			return QBECols;
	}

	public void setdata(Vector<?> data) {
		this.data = data;
	}

	public Vector<?> getData() {
		if (length == 0)
			return null;
		else
			return data;
	}

}
