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

package com.ijuru.kumva;

import java.util.ArrayList;
import java.util.List;

/**
 * A dictionary entry
 */
public class Entry {

	private Integer entryId = null;
	private List<Revision> revisions = new ArrayList<Revision>();

	/**
	 * Gets the entry id
	 * @return the entryId
	 */
	public Integer getEntryId() {
		return entryId;
	}

	/**
	 * Sets the entry id
	 * @param entryId the entryId
	 */
	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	/**
	 * Gets the revisions
	 * @return the revisions
	 */
	public List<Revision> getRevisions() {
		return revisions;
	}

	/**
	 * Sets the revisions
	 * @param revisions the revisions
	 */
	public void setRevisions(List<Revision> revisions) {
		this.revisions = revisions;
	}

	/**
	 * Adds a revision
	 * @param revision the revision
	 */
	public void addRevision(Revision revision) {
		revisions.add(revision);
	}
}
