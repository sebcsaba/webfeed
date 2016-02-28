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

}
