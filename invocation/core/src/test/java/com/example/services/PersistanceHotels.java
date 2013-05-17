package com.example.services;

import java.util.HashMap;
import java.util.Map;


public class PersistanceHotels {
	private static int currentID = 1;

	private Map<String, Hotel> hotels = new HashMap<String, Hotel>();

	public PersistanceHotels() {
		for (int i = 0; i < 10; i++) {
			String hotelID = getNewHotelID();
			Hotel h = new Hotel();
			h.setId(hotelID);
			h.setName("The standard name of Hotel" + hotelID);
			hotels.put(hotelID, h);
		}
	}

	String getNewHotelID() {
		return Integer.toString(currentID++);
	}

	public Map<String, Hotel> getHotels() {
		return hotels;
	}

	@Override
	public String toString() {
		return hotels.toString();
	}

}
