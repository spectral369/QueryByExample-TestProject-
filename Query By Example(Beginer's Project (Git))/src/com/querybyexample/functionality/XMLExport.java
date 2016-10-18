package com.querybyexample.functionality;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLExport {

	protected void defaultXMLExport(QueryData queryData) throws ParserConfigurationException {
		if (queryData.data == null || queryData.QBECols == null) {
			throw new NullPointerException("Nothing to export");
		} else {

			JFileChooser fc = new JFileChooser();
			fc.setName("FileChooser");
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-DD-YYYY HH-mm-ss");
			String date = sdf.format(calendar.getTime());
			fc.setSelectedFile(new java.io.File(date + ".xml"));
			fc.getCurrentDirectory().setExecutable(true);
			fc.getCurrentDirectory().setWritable(true);

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			int op = fc.showSaveDialog(null);
			if (op == JFileChooser.APPROVE_OPTION) {
				try {

					String path = fc.getCurrentDirectory().getAbsolutePath();
					String name = fc.getSelectedFile().getName();
					Element rootElement = doc.createElement("XML_Report");
					doc.appendChild(rootElement);
					Element e, r;
					Vector<?> row = new Vector<>();
					//
					int count = queryData.length;
					int data = queryData.getData().size();

					for (int i = 0; i < data; i++) {
						Vector<?> h = (Vector<?>) queryData.getQBECols();
						e = doc.createElement("field_" + i);

						row = queryData.getData();

						for (int j = 0; j < count; j++) {
							Vector<?> v = (Vector<?>) row.get(j);

							r = doc.createElement(h.get(j).toString());
							r.setAttribute("id", h.get(j).toString());
							r.setTextContent(v.get(j).toString());
							e.appendChild(r);

						}
						rootElement.appendChild(e);
					}

					TransformerFactory tFactory = TransformerFactory.newInstance();
					Transformer transformer = tFactory.newTransformer();

					transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				//	DOMSource source = new DOMSource(doc);
				//	StreamResult result = new StreamResult(System.out);
				//	transformer.transform(source, result);

					// send DOM to file
					transformer.transform(new DOMSource(doc),
							new StreamResult(new FileOutputStream(path + "//" + name)));

				} catch (Exception ex) {
					ex.getMessage();
				}
			}

		}
	}

}
