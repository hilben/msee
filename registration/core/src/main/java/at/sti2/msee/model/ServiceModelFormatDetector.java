package at.sti2.msee.model;

import java.net.URL;

public class ServiceModelFormatDetector {

	public static ServiceModelFormat detect(URL serviceDescriptionURL) {
		//TODO better detection
		if (serviceDescriptionURL.toString().endsWith("sawsdl"))
		{
			return ServiceModelFormat.SAWSDL;
		}
		else if (serviceDescriptionURL.toString().endsWith("msm"))
		{
			return ServiceModelFormat.MSM;
		}
		else
		{
			return ServiceModelFormat.UNKNOWN;
		}
	}
}
