/*
 * Copyright (c) 2009, University of Innsbruck, Austria.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package eu.soa4all.ranking.wsmolite;

import junit.framework.TestCase;
import eu.soa4all.ranking.ws.Ranking;

public class RankWSTestURI extends TestCase {

	private Ranking r;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		r = new Ranking();
	}

	public void testRanking() throws Exception {
		String[] servicesURI = new String[5];

		servicesURI[0] = "http://members.sti2.at/~ioant/soa4all-service-ranking-integration/WSMullerFixed_new.rdf.n3";
		servicesURI[1] = "http://members.sti2.at/~ioant/soa4all-service-ranking-integration/WSRacerFixed_new.rdf.n3";
		servicesURI[2] = "http://members.sti2.at/~ioant/soa4all-service-ranking-integration/WSRunnerFixed_new.rdf.n3";
		servicesURI[3] = "http://members.sti2.at/~ioant/soa4all-service-ranking-integration/WSWalkerFixed_new.rdf.n3";
		servicesURI[4] = "http://members.sti2.at/~ioant/soa4all-service-ranking-integration/WSWeaselFixed_new.rdf.n3";

		String templateURI = "http://members.sti2.at/~ioant/soa4all-service-ranking-integration/stShipping.rdf.n3";

		String instances = "<http://www.example.com/Gumble#GubleOrder> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://sws-ranking/Shipment.wsml#ShipmentOrderRequest> . "
				+ "<http://www.example.com/Gumble#GubleOrder> <http://sws-ranking/Shipment.wsml#paymentMethod> <http://www.wsmo.org/ontologies/nfp/paymentNFPOntology#CashInstrument> . "
				+ "<http://www.example.com/Gumble#GubleOrder> <http://sws-ranking/Shipment.wsml#packageToSend> <http://www.example.com/Gumble#GumblePackage> . "
				+ "<http://www.example.com/Gumble#GumblePackage> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://sws-ranking/Shipment.wsml#Package> . "
				+ "<http://www.example.com/Gumble#GumblePackage> <http://sws-ranking/Shipment.wsml#length> \"10\"^^<http://www.w3.org/2001/XMLSchema#float> . "
				+ "<http://www.example.com/Gumble#GumblePackage> <http://sws-ranking/Shipment.wsml#width> \"2\"^^<http://www.w3.org/2001/XMLSchema#float> . "
				+ "<http://www.example.com/Gumble#GumblePackage> <http://sws-ranking/Shipment.wsml#height> \"3\"^^<http://www.w3.org/2001/XMLSchema#float> . "
				+ "<http://www.example.com/Gumble#GumblePackage> <http://sws-ranking/Shipment.wsml#weight> \"10\"^^<http://www.w3.org/2001/XMLSchema#float> . "
				+ "<http://www.example.com/Gumble#GumblePackage> <http://sws-ranking/Shipment.wsml#declaredValue> \"150\"^^<http://www.w3.org/2001/XMLSchema#float> . "
				+ "<http://www.example.com/Gumble#GumblePackage> <http://sws-ranking/Shipment.wsml#containesItemsOfType> <http://sws-ranking/Shipment.wsml#Glassware> . "
				+ "<http://www.example.com/Gumble#GumblePackage> <http://sws-ranking/Shipment.wsml#packageStatus> <http://sws-ranking/Shipment.wsml#packageLost> .";

		r.rank(servicesURI, templateURI, instances);
	}

}