package coms.w4156.moviewishlist.modelTests;

import coms.w4156.moviewishlist.models.watchMode.WatchModeSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Tests all the POJO stuff. ToString, Equals, Getter/Setter.
 */
class WatchModeSourceTests {

    @Test
    void testToString() {
        WatchModeSource watchModeSource = new WatchModeSource();

        Assertions.assertEquals("WatchModeSource(id=null, name=null, type=null, region=null, iosUrl=null, androidUrl=null, webUrl=null, format=null, price=null, seasons=null, episodes=null)", watchModeSource.toString());
    }

    @Test
    void testEquals() {
        WatchModeSource watchModeSource = new WatchModeSource();
        WatchModeSource watchModeSource1 = new WatchModeSource();

        Assertions.assertEquals(watchModeSource1, watchModeSource);
    }

    @Test
    void testGetSetId() {
        WatchModeSource watchModeSource = new WatchModeSource();
        long id = 42L;
        watchModeSource.setId(id);

        Assertions.assertEquals(id, watchModeSource.getId());
    }

    @Test
    void testGetSetName() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        watchModeSource.setName(name);

        Assertions.assertEquals(name, watchModeSource.getName());
    }

    @Test
    void testGetSetType() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        watchModeSource.setType(name);

        Assertions.assertEquals(name, watchModeSource.getType());
    }

    @Test
    void testGetSetRegion() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        watchModeSource.setRegion(name);

        Assertions.assertEquals(name, watchModeSource.getRegion());
    }

    @Test
    void testGetSetIosUrl() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        watchModeSource.setIosUrl(name);

        Assertions.assertEquals(name, watchModeSource.getIosUrl());
    }

    @Test
    void testGetSetAndroidUrl() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        watchModeSource.setAndroidUrl(name);

        Assertions.assertEquals(name, watchModeSource.getAndroidUrl());
    }

    @Test
    void testGetSetwebUrl() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        watchModeSource.setWebUrl(name);

        Assertions.assertEquals(name, watchModeSource.getWebUrl());
    }

    @Test
    void testGetSetFormat() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        watchModeSource.setFormat(name);

        Assertions.assertEquals(name, watchModeSource.getFormat());
    }

    @Test
    void testGetSetPrice() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        watchModeSource.setPrice(name);

        Assertions.assertEquals(name, watchModeSource.getPrice());
    }

    @Test
    void testGetSetSeasons() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        Optional<Integer> seasons = Optional.of(90);
        watchModeSource.setSeasons(seasons);

        Assertions.assertEquals(seasons, watchModeSource.getSeasons());
    }

    @Test
    void testGetSetEpisodes() {
        WatchModeSource watchModeSource = new WatchModeSource();
        String name = "Name";
        Optional<Integer> seasons = Optional.empty();
        watchModeSource.setEpisodes(seasons);

        Assertions.assertEquals(seasons, watchModeSource.getEpisodes());
    }



}
