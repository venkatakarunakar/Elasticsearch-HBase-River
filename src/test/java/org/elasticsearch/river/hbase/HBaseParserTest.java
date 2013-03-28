package org.elasticsearch.river.hbase;

import java.util.HashMap;
import java.util.Map;

import mockit.Mock;
import mockit.MockUp;

import org.elasticsearch.client.Client;
import org.elasticsearch.river.AbstractRiverComponent;
import org.elasticsearch.river.RiverName;
import org.elasticsearch.river.RiverSettings;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HBaseParserTest {
	public String	separator;

	@BeforeClass
	public void setUp() {
		new MockUp<AbstractRiverComponent>() {
			@Mock
			void $init(final RiverName riverName, final RiverSettings settings) {}
		};
		new MockUp<HBaseRiver>() {
			@Mock
			void $init(final RiverName riverName, final RiverSettings settings, final Client esClient) {}

			@Mock
			String getColumnSeparator() {
				return HBaseParserTest.this.separator;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReadQualifierStructure() throws Exception {
		this.separator = "::";
		final Map<String, Object> result = new HashMap<String, Object>();
		final HBaseParser parser = new HBaseParser(new HBaseRiver(null, null, null));
		parser.readQualifierStructure(result, "data::set1::category1", "test1");
		parser.readQualifierStructure(result, "data::set1::category2", "test2");
		parser.readQualifierStructure(result, "data::set1::category3", "test3");
		parser.readQualifierStructure(result, "data::set2::category1", "test4");
		parser.readQualifierStructure(result, "data::set2::category2", "test5");

		Assert.assertEquals(((Map<String, Object>) ((Map<String, Object>) result.get("data")).get("set1")).get("category1"), "test1");
		Assert.assertEquals(((Map<String, Object>) ((Map<String, Object>) result.get("data")).get("set1")).get("category2"), "test2");
		Assert.assertEquals(((Map<String, Object>) ((Map<String, Object>) result.get("data")).get("set1")).get("category3"), "test3");
		Assert.assertEquals(((Map<String, Object>) ((Map<String, Object>) result.get("data")).get("set2")).get("category1"), "test4");
		Assert.assertEquals(((Map<String, Object>) ((Map<String, Object>) result.get("data")).get("set2")).get("category2"), "test5");
	}

	@Test
	public void testReadQualifierStructureNullSeperator() throws Exception {
		this.separator = null;

		final Map<String, Object> result = new HashMap<String, Object>();
		final HBaseParser parser = new HBaseParser(new HBaseRiver(null, null, null));
		parser.readQualifierStructure(result, "data::set1::category1", "test1");
		parser.readQualifierStructure(result, "data::set1::category2", "test2");
		parser.readQualifierStructure(result, "data::set1::category3", "test3");
		parser.readQualifierStructure(result, "data::set2::category1", "test4");
		parser.readQualifierStructure(result, "data::set2::category2", "test5");

		Assert.assertEquals(result.get("data::set1::category1"), "test1");
		Assert.assertEquals(result.get("data::set1::category2"), "test2");
		Assert.assertEquals(result.get("data::set1::category3"), "test3");
		Assert.assertEquals(result.get("data::set2::category1"), "test4");
		Assert.assertEquals(result.get("data::set2::category2"), "test5");
	}

	@Test
	public void testReadQualifierStructureEmptySeperator() throws Exception {
		this.separator = "";

		final Map<String, Object> result = new HashMap<String, Object>();
		final HBaseParser parser = new HBaseParser(new HBaseRiver(null, null, null));
		parser.readQualifierStructure(result, "data::set1::category1", "test1");
		parser.readQualifierStructure(result, "data::set1::category2", "test2");
		parser.readQualifierStructure(result, "data::set1::category3", "test3");
		parser.readQualifierStructure(result, "data::set2::category1", "test4");
		parser.readQualifierStructure(result, "data::set2::category2", "test5");

		Assert.assertEquals(result.get("data::set1::category1"), "test1");
		Assert.assertEquals(result.get("data::set1::category2"), "test2");
		Assert.assertEquals(result.get("data::set1::category3"), "test3");
		Assert.assertEquals(result.get("data::set2::category1"), "test4");
		Assert.assertEquals(result.get("data::set2::category2"), "test5");
	}
}