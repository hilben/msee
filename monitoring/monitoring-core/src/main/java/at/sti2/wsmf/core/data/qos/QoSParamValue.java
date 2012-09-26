/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.wsmf.core.data.qos;

import java.io.Serializable;

import at.sti2.wsmf.api.data.qos.IQoSParamValue;
import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;

/**
 * @author Alex Oberhauser
 */
public class QoSParamValue implements IQoSParamValue, Serializable {
	private static final long serialVersionUID = -8793209957841733499L;

	private QoSParamKey type;
	private String value;
	private QoSUnit unit;

	/**
	 * Used by the Web Service Generation Code.
	 */
	public QoSParamValue() {
	}

	public QoSParamValue(QoSParamKey _type, String _value, QoSUnit _unit) {
		this.type = _type;
		this.value = _value;
		this.unit = _unit;
	}

	/**
	 * @see at.sti2.wsmf.api.data.qos.IQoSParamValue#getValue()
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * @see at.sti2.wsmf.api.data.qos.IQoSParamValue#getUnit()
	 */
	@Override
	public QoSUnit getUnit() {
		return this.unit;
	}

	/**
	 * @see at.sti2.wsmf.api.data.qos.IQoSParamValue#getType()
	 */
	@Override
	public QoSParamKey getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.type + " has value " + this.value + " " + this.unit.name();
	}
}
