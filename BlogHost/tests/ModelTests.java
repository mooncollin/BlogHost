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

public class ModelTests
{
	@Test
	public void modelGetAllTest()
	{
		List<Model> posts = Model.getAll(BlogHostPosts.class);
		List<Model> ads = Model.getAll(BlogHostAds.class);
		List<Model> comments = Model.getAll(BlogHostComments.class);
		List<Model> creators = Model.getAll(BlogHostCreators.class);
		List<Model> donations = Model.getAll(BlogHostDonations.class);
		List<Model> favorites = Model.getAll(BlogHostFavorites.class);
		List<Model> likes = Model.getAll(BlogHostLikes.class);
		List<Model> mods = Model.getAll(BlogHostMods.class);
		List<Model> postStats = Model.getAll(BlogHostPostStats.class);
		List<Model> sites = Model.getAll(BlogHostSites.class);
		List<Model> stores = Model.getAll(BlogHostStores.class);
		List<Model> subscriptions = Model.getAll(BlogHostSubscriptions.class);
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
	
	public void inserter(Model model)
	{
		assertTrue(model.changed());
		assertTrue(model.commit());
		
		List<Model> models = Model.getAll(model.getClass());
		
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
	
	public void deleter(Model model)
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
			"a username", "George", "Bush", 45, "yeehaw", null, false);
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
			1, "shirt", "ew", null);
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
	
	@Test
	public void BlogHostAdsChangeTest()
	{
		BlogHostAds ad = new BlogHostAds("<b>Woah</b>");
		inserter(ad);
		assertFalse(ad.changed());
		ad.setHTML("what");
		inserter(ad);
		deleter(ad);
	}
	
	@Test
	public void BlogHostCommentsChangeTest()
	{
		BlogHostComments comment = new BlogHostComments(
				1, new BigInteger("1"), "Sup", false, 5, 10, null, null);
		inserter(comment);
		assertFalse(comment.changed());
		comment.setLikes(50);
		inserter(comment);
		deleter(comment);
	}
	
	@Test
	public void BlogHostCreatorsChangeTest()
	{
		BlogHostCreators creator = new BlogHostCreators(
				"a username", "George", "Bush", 45, "yeehaw", null, false);
		inserter(creator);
		assertFalse(creator.changed());
		creator.setFirstName("billy");
		inserter(creator);
		deleter(creator);
	}
	
	@Test
	public void BlogHostDonationsChangeTest()
	{
		BlogHostDonations donation = new BlogHostDonations(
				1, 2, new BigDecimal(50));
		inserter(donation);
		assertFalse(donation.changed());
		donation.setAmount(new BigDecimal(20.43));
		inserter(donation);
		deleter(donation);
	}
	
	@Test
	public void BlogHostFavoritesChangeTest()
	{
		BlogHostFavorites favorite = new BlogHostFavorites(
				1, new BigInteger("1"));
		inserter(favorite);
		assertFalse(favorite.changed());
		favorite.setReaderID(5);
		inserter(favorite);
		deleter(favorite);
	}
	
	@Test
	public void BlogHostLikesChangeTest()
	{
		BlogHostLikes like = new BlogHostLikes(
				1, new BigInteger("1"));
		inserter(like);
		assertFalse(like.changed());
		like.setReaderID(4);
		inserter(like);
		deleter(like);
	}
	
	@Test
	public void BlogHostModsChangeTest()
	{
		BlogHostMods mod = new BlogHostMods(
				1, 1);
		inserter(mod);
		assertFalse(mod.changed());
		mod.setCreatorID(10);
		inserter(mod);
		deleter(mod);
	}
	
	@Test
	public void BlogHostStatsChangeTest()
	{
		BlogHostPostStats stat = new BlogHostPostStats(
				new BigInteger("1"), 10, 10);
		inserter(stat);
		assertFalse(stat.changed());
		stat.setViews(4000);
		inserter(stat);
		deleter(stat);
	}
	
	@Test
	public void BlogHostSitesChangeTest()
	{
		BlogHostSites site = new BlogHostSites(
				1, "urlstuff", "a name", "htmlstuff");
		inserter(site);
		assertFalse(site.changed());
		site.setCustomHTML("woah!!!");
		inserter(site);
		deleter(site);
	}
	
	@Test
	public void BlogHostPostsChangeTest()
	{
		BlogHostPosts post = new BlogHostPosts(
				1, "Test!", "Test!", null, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
		inserter(post);
		assertFalse(post.changed());
		post.setPostTitle("Cool title bro");
		inserter(post);
		deleter(post);
	}
	
	@Test
	public void BlogHostStoresChangeTest()
	{
		BlogHostStores store = new BlogHostStores(
				1, "shirt", "ew", null);
		inserter(store);
		assertFalse(store.changed());
		store.setItemDescription("weeeee");
		inserter(store);
		deleter(store);
	}
	
	@Test
	public void BlogHostSubscriptionsChangeTest()
	{
		BlogHostSubscriptions subscription = new BlogHostSubscriptions(
				1, 1);
		inserter(subscription);
		assertFalse(subscription.changed());
		subscription.setCreatorID(30);
		inserter(subscription);
		deleter(subscription);
	}
}
