package at.sti2.msee.discovery.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import at.sti2.msee.discovery.core.common.DiscoveryConfigTest;
import at.sti2.msee.discovery.core.common.DiscoveryQueryBuilderTest;
import at.sti2.msee.discovery.core.other.RDF2GODiscoverTest;
import at.sti2.msee.discovery.core.util.ServiceTypeFinderTest;

@RunWith(Suite.class)
@SuiteClasses({ DiscoveryServiceHrestsTest.class, DiscoveryServiceTest.class,
		DiscoveryServiceTest_ForTree.class, DiscoveryServiceTestForDashboard.class,
		ServiceDiscoveryFactoryTest.class, ServiceTypeFinderTest.class, RDF2GODiscoverTest.class,
		DiscoveryQueryBuilderTest.class, DiscoveryConfigTest.class })
public class AllTests {

}
