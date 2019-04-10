package database;

import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;

import org.junit.Test;

import models.BlogHostAds;
import models.BlogHostComments;
import models.BlogHostCreators;
import models.BlogHostDonations;
import models.BlogHostFavorites;
import models.BlogHostLikes;
import models.BlogHostMods;
import models.BlogHostPostStats;
import models.BlogHostPosts;
import models.BlogHostSites;
import models.BlogHostStores;
import models.BlogHostSubscriptions;

public class ChangeModelTests
{
	@Test
	public void BlogHostAdsChangeTest()
	{
		BlogHostAds ad = new BlogHostAds("<b>Woah</b>");
		InsertModelTests.inserter(ad);
		assertFalse(ad.changed());
		ad.setHTML("what");
		InsertModelTests.inserter(ad);
		InsertModelTests.deleter(ad);
	}
	
	@Test
	public void BlogHostCommentsChangeTest()
	{
		BlogHostComments comment = new BlogHostComments(
				1, new BigInteger("1"), "Sup", false, 5, 10, null, null);
		InsertModelTests.inserter(comment);
		assertFalse(comment.changed());
		comment.setLikes(50);
		InsertModelTests.inserter(comment);
		InsertModelTests.deleter(comment);
	}
	
	@Test
	public void BlogHostCreatorsChangeTest()
	{
		BlogHostCreators creator = new BlogHostCreators(
				"a username", "George", "Bush", 45, "yeehaw", null, false);
		InsertModelTests.inserter(creator);
		assertFalse(creator.changed());
		creator.setFirstName("billy");
		InsertModelTests.inserter(creator);
		InsertModelTests.deleter(creator);
	}
	
	@Test
	public void BlogHostDonationsChangeTest()
	{
		BlogHostDonations donation = new BlogHostDonations(
				1, 2, new BigDecimal(50));
		InsertModelTests.inserter(donation);
		assertFalse(donation.changed());
		donation.setAmount(new BigDecimal(20.43));
		InsertModelTests.inserter(donation);
		InsertModelTests.deleter(donation);
	}
	
	@Test
	public void BlogHostFavoritesChangeTest()
	{
		BlogHostFavorites favorite = new BlogHostFavorites(
				1, new BigInteger("1"));
		InsertModelTests.inserter(favorite);
		assertFalse(favorite.changed());
		favorite.setReaderID(5);
		InsertModelTests.inserter(favorite);
		InsertModelTests.deleter(favorite);
	}
	
	@Test
	public void BlogHostLikesChangeTest()
	{
		BlogHostLikes like = new BlogHostLikes(
				1, new BigInteger("1"));
		InsertModelTests.inserter(like);
		assertFalse(like.changed());
		like.setReaderID(4);
		InsertModelTests.inserter(like);
		InsertModelTests.deleter(like);
	}
	
	@Test
	public void BlogHostModsChangeTest()
	{
		BlogHostMods mod = new BlogHostMods(
				1, 1);
		InsertModelTests.inserter(mod);
		assertFalse(mod.changed());
		mod.setCreatorID(10);
		InsertModelTests.inserter(mod);
		InsertModelTests.deleter(mod);
	}
	
	@Test
	public void BlogHostStatsChangeTest()
	{
		BlogHostPostStats stat = new BlogHostPostStats(
				new BigInteger("1"), 10, 10);
		InsertModelTests.inserter(stat);
		assertFalse(stat.changed());
		stat.setViews(4000);
		InsertModelTests.inserter(stat);
		InsertModelTests.deleter(stat);
	}
	
	@Test
	public void BlogHostSitesChangeTest()
	{
		BlogHostSites site = new BlogHostSites(
				1, "urlstuff", "a name", "htmlstuff");
		InsertModelTests.inserter(site);
		assertFalse(site.changed());
		site.setCustomHTML("woah!!!");
		InsertModelTests.inserter(site);
		InsertModelTests.deleter(site);
	}
	
	@Test
	public void BlogHostPostsChangeTest()
	{
		BlogHostPosts post = new BlogHostPosts(
				1, "Test!", "Test!", null, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
		InsertModelTests.inserter(post);
		assertFalse(post.changed());
		post.setPostTitle("Cool title bro");
		InsertModelTests.inserter(post);
		InsertModelTests.deleter(post);
	}
	
	@Test
	public void BlogHostStoresChangeTest()
	{
		BlogHostStores store = new BlogHostStores(
				1, "shirt", "ew", null);
		InsertModelTests.inserter(store);
		assertFalse(store.changed());
		store.setItemDescription("weeeee");
		InsertModelTests.inserter(store);
		InsertModelTests.deleter(store);
	}
	
	@Test
	public void BlogHostSubscriptionsChangeTest()
	{
		BlogHostSubscriptions subscription = new BlogHostSubscriptions(
				1, 1);
		InsertModelTests.inserter(subscription);
		assertFalse(subscription.changed());
		subscription.setCreatorID(30);
		InsertModelTests.inserter(subscription);
		InsertModelTests.deleter(subscription);
	}
}
