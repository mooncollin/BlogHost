package database;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

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
import models.Model;

public class InsertModelTests
{
	@Test
	public void modelGetAllTest()
	{
		List<BlogHostPosts> posts = Model.getAll(BlogHostPosts.class);
		List<BlogHostAds> ads = Model.getAll(BlogHostAds.class);
		List<BlogHostComments> comments = Model.getAll(BlogHostComments.class);
		List<BlogHostCreators> creators = Model.getAll(BlogHostCreators.class);
		List<BlogHostDonations> donations = Model.getAll(BlogHostDonations.class);
		List<BlogHostFavorites> favorites = Model.getAll(BlogHostFavorites.class);
		List<BlogHostLikes> likes = Model.getAll(BlogHostLikes.class);
		List<BlogHostMods> mods = Model.getAll(BlogHostMods.class);
		List<BlogHostPostStats> postStats = Model.getAll(BlogHostPostStats.class);
		List<BlogHostSites> sites = Model.getAll(BlogHostSites.class);
		List<BlogHostStores> stores = Model.getAll(BlogHostStores.class);
		List<BlogHostSubscriptions> subscriptions = Model.getAll(BlogHostSubscriptions.class);
		assertNotNull(posts);
		assertNotNull(ads);
		assertNotNull(comments);
		assertNotNull(creators);
		assertNotNull(donations);
		assertNotNull(favorites);
		assertNotNull(likes);
		assertNotNull(mods);
		assertNotNull(postStats);
		assertNotNull(sites);
		assertNotNull(stores);
		assertNotNull(subscriptions);
	}
	
	@SuppressWarnings("unchecked")
	public static void inserter(Model model)
	{
		assertTrue(model.changed());
		assertTrue(model.commit());
		
		List<Model> models = (List<Model>) Model.getAll(model.getClass());
		
		boolean inserted = false;
		for(Model m : models)
		{
			if(model.equals(m))
			{
				inserted = true;
				break;
			}
		}
		
		if(!inserted)
		{
			fail(model.getClass().getSimpleName() + " was not inserted!");
		}
	}
	
	public static void deleter(Model model)
	{
		assertTrue(model.delete());
	}
	
	@Test
	public void BlogHostPostsInsertAndDeleteTest()
	{
		BlogHostPosts post = new BlogHostPosts(
			1, "Test!", "Test!", null, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
		inserter(post);
		deleter(post);
	}
	
	@Test
	public void BlogHostsAdsInsertAndDeleteTest()
	{
		BlogHostAds ad = new BlogHostAds("<b>Woah</b>");
		inserter(ad);
		deleter(ad);
	}
	
	@Test
	public void BlogHostCommentsInsertAndDeleteTest()
	{
		BlogHostComments comment = new BlogHostComments(
			1, new BigInteger("1"), "Sup", false, 5, 10, null, null);
		inserter(comment);
		deleter(comment);
	}
	
	@Test
	public void BlogHostCreatorsInsertAndDeleteTest()
	{
		BlogHostCreators creator = new BlogHostCreators(
			"a username", "George", "Bush", "GeorgeBush@gmail.com", 45, "yeehaw", null, false);
		inserter(creator);
		deleter(creator);
	}
	
	@Test
	public void BlogHostDonationsInsertAndDeleteTest()
	{
		BlogHostDonations donation = new BlogHostDonations(
			1, 2, new BigDecimal(50));
		inserter(donation);
		deleter(donation);
	}
	
	@Test
	public void BlogHostFavoritesInsertAndDeleteTest()
	{
		BlogHostFavorites favorite = new BlogHostFavorites(
			1, new BigInteger("1"));
		inserter(favorite);
		deleter(favorite);
	}
	
	@Test
	public void BlogHostLikesInsertAndDeleteTest()
	{
		BlogHostLikes like = new BlogHostLikes(
			1, new BigInteger("1"));
		inserter(like);
		deleter(like);
	}
	
	@Test
	public void BlogHostModsInsertAndDeleteTest()
	{
		BlogHostMods mod = new BlogHostMods(
			1, 1);
		inserter(mod);
		deleter(mod);
	}
	
	@Test
	public void BlogHostPostStatsInsertAndDeleteTest()
	{
		BlogHostPostStats stat = new BlogHostPostStats(
			new BigInteger("1"), 10, 10);
		inserter(stat);
		deleter(stat);
	}
	
	@Test
	public void BlogHostSitesInsertAndDeleteTest()
	{
		BlogHostSites site = new BlogHostSites(
			1, "urlstuff", "a name", "htmlstuff");
		inserter(site);
		deleter(site);
	}
	
	@Test
	public void BlogHostStoresInsertAndDeleteTest()
	{
		BlogHostStores store = new BlogHostStores(
			1, "shirt", "ew", null, new BigDecimal(45));
		inserter(store);
		deleter(store);
	}
	
	@Test
	public void BlogHostSubscriptionsInsertAndDeleteTest()
	{
		BlogHostSubscriptions subscription = new BlogHostSubscriptions(
			1, 1);
		inserter(subscription);
		deleter(subscription);
	}
}
