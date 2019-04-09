package database;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import models.BlogHostAds;
import models.Model;

public class FilterModelTests
{
	@Test
	public void BlogHostAdsFilterTest()
	{
		BlogHostAds ad = new BlogHostAds("<b>Woah</b>");
		InsertModelTests.inserter(ad);
		List<BlogHostAds> justOne = Model.getAll(BlogHostAds.class, "ad_id=?", ad.getAdID());
		assertEquals(1, justOne.size());
		assertEquals(ad.getAdID(), justOne.get(0).getAdID());
		assertEquals("<b>Woah</b>", justOne.get(0).getHTML());
		ad.delete();
	}
	
	@Test
	public void BlogHostAdsFilterMultipleTest()
	{
		final int AMOUNT = 5;
		final String testString = "testing";
		
		List<BlogHostAds> adding = new LinkedList<BlogHostAds>();
		for(int i = 0; i < AMOUNT; i++)
		{
			adding.add(new BlogHostAds(testString));
		}
		for(BlogHostAds ad : adding)
		{
			InsertModelTests.inserter(ad);
		}
		
		List<BlogHostAds> query = Model.getAll(BlogHostAds.class, "html=?", testString);
		assertEquals(AMOUNT, query.size());
		
		for(BlogHostAds ad : adding)
		{
			InsertModelTests.deleter(ad);
		}
	}
}
