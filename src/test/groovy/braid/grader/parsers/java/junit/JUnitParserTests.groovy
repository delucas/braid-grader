package braid.grader.parsers.java.junit

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class JUnitParserTests {

	JUnitParser parser
	
	@Before void setUp() {
		parser = new JUnitParser()
	}
	
	String loadXML() {
		def myXML = this.getClass().getResource('/TEST-ar.edu.unlam.tallerweb.ecuaciones.EcuacionTest.xml')
		myXML
	}
	
	@Test void thatCanParse() {
		String xml = loadXML()
		parser.parse(xml)
	}
	
	@Test void thatCanParseFailures() {
		String xml = loadXML()
		def issues = parser.parse(xml)
		
		def issue = new JUnitIssue(
			classname: 'ar.edu.unlam.tallerweb.ecuaciones.EcuacionTest',
			test: 'test',
			type: 'failure',
			message: 'No implementado',
			details: '''java.lang.AssertionError: No implementado
	at org.junit.Assert.fail(Assert.java:93)
	at ar.edu.unlam.tallerweb.ecuaciones.EcuacionTest.test(EcuacionTest.java:16)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:597)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:45)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:15)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:42)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:20)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:263)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:68)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:47)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:231)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:60)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:229)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:50)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:222)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:300)
	at org.apache.maven.surefire.junit4.JUnit4TestSet.execute(JUnit4TestSet.java:53)
	at org.apache.maven.surefire.junit4.JUnit4Provider.executeTestSet(JUnit4Provider.java:123)
	at org.apache.maven.surefire.junit4.JUnit4Provider.invoke(JUnit4Provider.java:104)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:597)
	at org.apache.maven.surefire.util.ReflectionUtils.invokeMethodWithArray(ReflectionUtils.java:164)
	at org.apache.maven.surefire.booter.ProviderFactory$ProviderProxy.invoke(ProviderFactory.java:110)
	at org.apache.maven.surefire.booter.SurefireStarter.invokeProvider(SurefireStarter.java:175)
	at org.apache.maven.surefire.booter.SurefireStarter.runSuitesInProcessWhenForked(SurefireStarter.java:107)
	at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:68)'''
		)

		assert issue == issues['failures'][0]
		
	}
	
}
