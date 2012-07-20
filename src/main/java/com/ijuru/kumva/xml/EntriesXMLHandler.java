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
import com.ijuru.kumva.RevisionStatus;
import com.ijuru.kumva.Tag;
import com.ijuru.kumva.util.Utils;

/**
 * SAX handler for Kumva entries XML
 */
public class EntriesXMLHandler extends DefaultHandler {
	
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
	
	private EntryParsedListener listener = null;
	
	/**
	 * Contructs a handler with a listener for parsed entries
	 * @param listener the listener
	 */
	public EntriesXMLHandler(EntryParsedListener listener) {
		this.listener = listener;
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(String, String, String, Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException, NumberFormatException {
		String element = Utils.isEmpty(localName) ? qName : localName;
		
		if (element.equals("entries")) {
			query = attributes.getValue("query");
			suggestion = attributes.getValue("suggestion");
		}
		else if (element.equals("entry")) {
			curEntry = new Entry();
			curEntry.setEntryId(Integer.parseInt(attributes.getValue("id")));
		} 
		else if (element.equals("revision")) {
			curRevision = new Revision();
			curRevision.setNumber(Integer.parseInt(attributes.getValue("number")));
			curRevision.setStatus(RevisionStatus.parse(attributes.getValue("status")));
			curRevision.setWordClass(attributes.getValue("wordclass"));
			curRevision.setNounClasses(Utils.parseCSVIntegers(attributes.getValue("nounclasses")));
			curRevision.setUnverified(Boolean.parseBoolean(attributes.getValue("unverified")));
		}
		else if (element.equals("meanings")) {
			inMeanings = true;
		}
		else if (element.equals("meaning")) {
			int flags = Meaning.parseFlags(attributes.getValue("flags"));
			curMeaning = new Meaning();
			curMeaning.setFlags(flags);
		}
		else if (element.equals("relationship")) {
			curRelationship = attributes.getValue("name");
			curTags = new ArrayList<Tag>();
		}
		else if (element.equals("tag")) {
			curTags.add(new Tag(attributes.getValue("lang"), attributes.getValue("text")));
		}
		else if (element.equals("example")) {
			curExample = new Example();
		} 
		/*else if (localName.equals("media")) {
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
	public void characters(char[] ch, int start, int length) throws SAXException {
		elementText.append(new String(ch, start, length));
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(String, String, String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		String element = Utils.isEmpty(localName) ? qName : localName;
		
		if (element.equals("entry")) {
			// Notify the listener that the current entry is ready
			if (listener != null)
				listener.entryParsed(curEntry);
		} 
		else if (element.equals("revision"))
			curEntry.addRevision(curRevision);
		else if (element.equals("meanings"))
			inMeanings = false;
		else if (element.equals("meaning")) {
			if (inMeanings)
				curRevision.addMeaning(curMeaning);
		} 
		else if (element.equals("example")) {
			curRevision.addExample(curExample);
		}
		
		// Get the complete text inside this element
		String text = elementText.toString();
		if (text.length() == 0)
			text = null;

		// Read text-only elements
		if (element.equals("prefix"))
			curRevision.setPrefix(text);
		else if (element.equals("lemma"))
			curRevision.setLemma(text);
		else if (element.equals("modifier"))
			curRevision.setModifier(text);
		else if (element.equals("pronunciation"))
			curRevision.setPronunciation(text);
		else if (element.equals("meaning")) {
			if (inMeanings)
				curMeaning.setText(text);
			else
				curExample.setMeaning(text);
		} 
		else if (element.equals("comment"))
			curRevision.setComment(text);
		else if (element.equals("relationship"))
			curRevision.setTags(curRelationship, curTags);
		else if (element.equals("usage"))
			curExample.setUsage(text);
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
