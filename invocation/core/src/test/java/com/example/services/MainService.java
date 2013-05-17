package com.example.services;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.Hotel;
import com.google.gson.Gson;

@Path("/hotel")
@Produces(MediaType.APPLICATION_JSON)
public class MainService {
	private final Logger LOGGER = LogManager.getLogger(this.getClass().getName());
	private Gson gson = new Gson();

	private static PersistanceHotels hotels = new PersistanceHotels();

	public MainService() {

	}

	@GET
	@Path("/count")
	public String getHotelCount() {
		return gson.toJson(hotels.getHotels().size());
	}

	@GET
	@Path("{hotelid}")
	public String getHotel(@PathParam("hotelid") String hotelid) {
		LOGGER.info(this.getClass() + " got invoked");
		return gson.toJson(hotels.getHotels().get(hotelid));
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String getHotels() {
		return gson.toJson(new ArrayList<Hotel>(hotels.getHotels().values()));
	}

	@POST
	public String postHotel(@FormParam("hotel") String hotelAsJson) {
		LOGGER.info(this.getClass() + " got invoked");

		Hotel tmpHotel = gson.fromJson(hotelAsJson, Hotel.class);
		String tmpHotelID = hotels.getNewHotelID();
		tmpHotel.setId(tmpHotelID);
		hotels.getHotels().put(tmpHotelID, tmpHotel);

		return gson.toJson(hotels.getHotels().get(tmpHotelID));
	}

}
