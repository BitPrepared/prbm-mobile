/*   This file is part of PrbmMobile
 *
 *   PrbmMobile is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   PrbmMobile is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with PrbmMobile.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.bitprepared.prbm.mobile.activity;

import it.bitprepared.prbm.mobile.model.Prbm;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

/**
 * Class used to collect global data related to user/application
 * Realized with singleton pattern
 *
 * @author Nicola Corti
 */
public class UserData {

	/** Singleton instance */
	private static UserData instance = null;

	/** Reference to actual prbm */
	private Prbm prbm = null;
	/** Reference to actual unit */
	private PrbmUnit unit;
	/** Reference to actual entity */
	private PrbmEntity entity;
	/** Reference to actual entity column*/
	private int entityColumn;

	/**
	 * Empty constructor
	 */
	private UserData() {
	}

	/**
	 * Method used to return UserData instance
	 *
	 * @return Reference to singleton instance
	 */
	public static synchronized UserData getInstance() {
		if (null == instance) {
			instance = new UserData();
		}
		return instance;
	}

	/**
	 * Returns reference to actual prbm
	 *
	 * @return Global reference to prbm
	 */
	public Prbm getPrbm() {
		return this.prbm;
	}

	/**
	 * Store actual prbm
	 *
	 * @param p Global reference to prbm
	 */
	public void setPrbm(Prbm p) {
		this.prbm = p;
	}

	/**
	 * Returns reference to actual entity
	 *
	 * @return Global reference to entity
	 */
	public PrbmEntity getEntity() {
		return this.entity;
	}

	public void setEntity(PrbmEntity entity) {
		this.entity = entity;
	}

	public PrbmUnit getUnit() {
		return unit;
	}

	public void setUnit(PrbmUnit unit) {
		this.unit = unit;
	}

	public int getColumn() {
		return entityColumn;
	}

	public void setColumn(int entityColumn) {
		this.entityColumn = entityColumn;
	}
}
