package com.example.services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Path("/hotel")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
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
	@Path("{hotelid}/details")
	public String getHotelDetails(@PathParam("hotelid") String hotelid) {
		LOGGER.info(this.getClass() + " got invoked");
		return gson.toJson(hotels.getHotels().get(hotelid).getDetails());
	}

	@PUT
	@Path("{hotelid}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response putHotel(String hotelAsJson) {
		LOGGER.info(this.getClass() + " got invoked");
		Hotel updatedHotel = gson.fromJson(hotelAsJson, Hotel.class);
		String hotelID = updatedHotel.getId();
		Hotel persistantHotel = hotels.getHotels().get(hotelID);
		hotels.getHotels().put(hotelID, updatedHotel);
		if (persistantHotel == null) {
			ResponseBuilder response = Response.status(Status.CREATED);
			response.entity(gson.toJson(hotels.getHotels().get(hotelID)));
			return response.build();
		} else {
			return Response.ok(gson.toJson(hotels.getHotels().get(hotelID))).build();
		}
	}
	
	@DELETE
	@Path("{hotelid}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteHotel(@PathParam("hotelid") String hotelid) {
		LOGGER.info(this.getClass() + " got invoked");
		Hotel persistantHotel = hotels.getHotels().remove(hotelid);
		if (persistantHotel == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			return Response.ok(gson.toJson(persistantHotel)).build();
		}
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
