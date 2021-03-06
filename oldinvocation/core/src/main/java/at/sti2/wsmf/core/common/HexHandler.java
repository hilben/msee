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
package at.sti2.wsmf.core.common;

import java.math.BigInteger;
import java.util.Vector;

/**
 * Helper class for the handling of hex values and byte arrays.
 * 
 * @author Alex Oberhauser
 */
public abstract class HexHandler {

	/**
	 * Converts the byte in the hexadecimal representation encoded as String.
	 * 
	 * @param _b
	 *            The byte that should be converted.
	 * @return The String representation of this byte (in hexadecimal).
	 */
	public static String getByteToString(byte _b) {
		return Integer.toString((_b & 0xff) + 0x100, 16).substring(1);
	}

	/**
	 * Converts a byte array in a String.
	 * 
	 * @param _byteArray
	 *            A array of bytes.
	 * @return The String representation of the byte Array.
	 * @throws Exception
	 */
	public static String getHexString(byte[] _byteArray) throws Exception {
		String result = new String("");
		for (int i = 0; i < _byteArray.length; i++) {
			result += getByteToString(_byteArray[i]);
		}
		return result.trim();
	}

	/**
	 * @param _byteArray
	 *            A input array of bytes
	 * @return The representation of the byte array in ascii code.
	 */
	public static String getHexToAscii(byte[] _byteArray) {
		String result = new String("");
		for (int i = 0; i < _byteArray.length; i++) {
			String hexValue = getByteToString(_byteArray[i]);
			int value = Integer.parseInt(hexValue, 16);
			if (value < 7 || value > 126)
				result += " ";
			else {
				result += (char) value;
			}
		}
		return result.trim();
	}

	/**
	 * Converts a byte array (hex) to a Vector of Integer (dec).
	 * 
	 * @param _byteArray
	 *            A Array of Bytes.
	 * @return A Vector of Integer, same length as the input array.
	 */
	public static Vector<Integer> getHexToInt(byte[] _byteArray) {
		Vector<Integer> result = new Vector<Integer>(_byteArray.length);
		for (int i = 0; i < _byteArray.length; i++) {
			String hexValue = getByteToString(_byteArray[i]);
			result.add(Integer.parseInt(hexValue, 16));
		}
		return result;
	}

	public static byte getByte(int _value) throws Exception {
		if (_value > 255)
			throw new NumberFormatException("The number is to big!");
		String hexValue = Integer.toHexString(_value);
		byte[] retValues = new BigInteger(hexValue, 16).toByteArray();
		if (retValues.length == 2)
			return retValues[1];
		else
			return retValues[0];
	}

	/**
	 * @return A byte array with 16 0x00 values.
	 */
	public static byte[] initEmptyBlock() {
		return new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
	}

	/**
	 * Splits the given byte array into pieces of 16 byte long arrays. 16 bytes
	 * is the block size of the tested card.
	 * 
	 * @param _inputArray
	 *            The byte array to split
	 * @return A Vector with byte arrays of the size of 16.
	 */
	public static Vector<byte[]> splitArray(byte[] _inputArray) {
		Vector<byte[]> retVector = new Vector<byte[]>();
		byte[] tmpArray = initEmptyBlock();
		int count = 0;
		for (int i = 0; i < _inputArray.length; i++) {
			if (i % 16 == 0 && i != 0) {
				retVector.add(tmpArray);
				tmpArray = initEmptyBlock();
				count = 0;
			}
			tmpArray[count] = _inputArray[i];
			count++;
		}
		retVector.add(tmpArray);
		return retVector;
	}
}
