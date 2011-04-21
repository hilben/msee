package at.sti2.ngsee.invoker_api.grounding;

import at.sti2.ngsee.invoker_api.grounding.exception.GroundingException;


/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Alex Oberhauser<br>
 * @version     $Id$<br>
 * Date of creation:  13.04.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public interface IGroundingEngine {
	public String lower(String rdfInputData) throws GroundingException;
	public String lift(String xmlInputData) throws GroundingException;
}
