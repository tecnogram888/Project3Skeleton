package edu.berkeley.cs162;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ KeyServerTest.class, KVCacheTest.class, KVMessageTest.class,
		ThreadPoolTest.class })
public class AllTests {

}
