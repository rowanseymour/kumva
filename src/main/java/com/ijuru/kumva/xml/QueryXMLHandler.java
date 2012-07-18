/**
 * Copyright 2011 Rowan Seymour
 * 
 * This file is part of Kumva.
 *
 * Kumva is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kumva is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kumva. If not, see <http://www.gnu.org/licenses/>.
 */

package com.ijuru.kumva.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ijuru.kumva.Entry;
import com.ijuru.kumva.Example;
import com.ijuru.kumva.Meaning;
import com.ijuru.kumva.Revision;
import com.ijuru.kumva.Tag;
import com.ijuru.kumva.util.Utils;

/**
 * SAX handler for Kumva query XML
 */
public class QueryXMLHandler extends DefaultHandler {
	
	private String query;
	private String suggestion;
	
	private Entry curEntry;
	private Revision curRevision;
	private Meaning curMeaning;
	private Example curExample;
	private String curRelationship;
	private List<Tag> curTags;
	private boolean inMeanings = false;
	private StringBuilder elementText = null;
	private List<EntryListener> listeners = new ArrayList<EntryListener>();

	/**
	 * Adds the definition listener
	 * @param listener the listener
	 */
	public void addListener(EntryListener listener) {
		listeners.add(listener);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("entries")) {
			query = attributes.getValue("query");
			suggestion = attributes.getValue("suggestion");
		}
		else if (localName.equals("entry")) {
			curEntry = new Entry();
		} else if (localName.equals("revision")) {
			curRevision = new Revision();
			curRevision.setWordClass(attributes.getValue("wordclass"));
			curRevision.setNounClasses(Utils.parseCSVIntegers(attributes.getValue("nounclasses")));
		} else if (localName.equals("meanings"))
			inMeanings = true;
		else if (localName.equals("meaning")) {
			int flags = Meaning.parseFlags(attributes.getValue("flags"));
			curMeaning = new Meaning();
			curMeaning.setFlags(flags);
		} else if (localName.equals("relationship")) {
			curRelationship = attributes.getValue("name");
			curTags = new ArrayList<Tag>();
		} else if (localName.equals("tag")) {
			curTags.add(new Tag(attributes.getValue("lang"), attributes.getValue("text")));
		} else if (localName.equals("example")) {
			curExample = new Example();
		} /*else if (localName.equals("media")) {
			if ("audio".equals(attributes.getValue("type"))) {
				curDefinition.setAudioURL(attributes.getValue("url"));
			}
		}*/

		elementText = new StringBuilder();
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		elementText.append(new String(ch, start, length));
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (localName.equals("entry")) {
			entryFinished();
		} 
		else if (localName.equals("revision"))
			curEntry.addRevision(curRevision);
		else if (localName.equals("meanings"))
			inMeanings = false;
		else if (localName.equals("meaning")) {
			if (inMeanings)
				curRevision.addMeaning(curMeaning);
		} 
		else if (localName.equals("example")) {
			curRevision.addExample(curExample);
		}
		
		// Get the complete text inside this element
		String text = elementText.toString();
		if (text.length() == 0)
			text = null;

		// Read text-only elements
		if (localName.equals("prefix"))
			curRevision.setPrefix(text);
		else if (localName.equals("lemma"))
			curRevision.setLemma(text);
		else if (localName.equals("modifier"))
			curRevision.setModifier(text);
		else if (localName.equals("pronunciation"))
			curRevision.setPronunciation(text);
		else if (localName.equals("meaning")) {
			if (inMeanings)
				curMeaning.setText(text);
			else
				curExample.setMeaning(text);
		} 
		else if (localName.equals("comment"))
			curRevision.setComment(text);
		else if (localName.equals("relationship"))
			curRevision.setTags(curRelationship, curTags);
		else if (localName.equals("usage"))
			curExample.setUsage(text);
	}

	/**
	 * Notifies all listeners that the current entry is ready
	 */
	private void entryFinished() {
		// Notify listeners
		for (EntryListener listener : listeners)
			listener.found(curEntry);
	}

	/**
	 * Gets the query
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Gets the suggestion
	 * @return the suggestion
	 */
	public String getSuggestion() {
		return suggestion;
	}
}
