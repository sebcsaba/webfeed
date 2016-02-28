package hu.sebcsaba.webfeed;

import hu.sebcsaba.webfeed.Config.Task;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class TestConfig {

	@Test
	public void testSimpleTask() {
		Properties p = new Properties();
		p.setProperty("task.t1.url", "http://www.example.com/to_load_this");
		p.setProperty("task.t1.selector", "#list li a");
		p.setProperty("task.t1.pager", "#pager li a");
		Config c = Config.readConfig(p);
		Assert.assertEquals(1, c.getTasks().size());
		Task t1 = c.getTasks().get("t1");
		Assert.assertEquals(1, t1.getUrls().size());
		Assert.assertTrue(t1.getPager().endsWith(" a"));
		Assert.assertTrue(t1.getSelector().endsWith(" a"));
	}

	@Test
	public void testMultipleUrlTask() {
		Properties p = new Properties();
		p.setProperty("task.t1.url.1", "http://www.example.com/to_load_this");
		p.setProperty("task.t1.url.2", "http://www.example.com/to_load_that");
		p.setProperty("task.t1.selector", "#list li a");
		p.setProperty("task.t1.pager", "#pager li a");
		Config c = Config.readConfig(p);
		Assert.assertEquals(1, c.getTasks().size());
		Task t1 = c.getTasks().get("t1");
		Assert.assertEquals(2, t1.getUrls().size());
		Assert.assertTrue(t1.getPager().endsWith(" a"));
		Assert.assertTrue(t1.getSelector().endsWith(" a"));
	}

	@Test
	public void testTemplatedUrlTask() {
		Properties p = new Properties();
		p.setProperty("task.t1.url", "http://www.example.com/key={d}/to_load_this");
		p.setProperty("task.t1.variable.d", "i;ii;iii;vi");
		p.setProperty("task.t1.selector", "#list li a");
		p.setProperty("task.t1.pager", "#pager li a");
		Config c = Config.readConfig(p);
		Assert.assertEquals(1, c.getTasks().size());
		Task t1 = c.getTasks().get("t1");
		Assert.assertEquals(4, t1.getUrls().size());
		for (String d : p.getProperty("task.t1.variable.d").split(";")) {
			String expected = p.getProperty("task.t1.url").replaceAll("\\{d\\}", d);
			Assert.assertTrue(t1.getUrls().contains(expected));
		}
		Assert.assertTrue(t1.getPager().endsWith(" a"));
		Assert.assertTrue(t1.getSelector().endsWith(" a"));
	}

}
