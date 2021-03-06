package com.balinski.internship;

import com.balinski.internship.model.Geo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeoTest {

    final Geo center = new Geo() {{
        setLat("0");
        setLng("0");
    }};

    @Test
    public void settingLatitudeAndLongitudeToValidValuesPasses() {
        String lat = "12.4567";
        String lng = "123.4567";
        Geo g = new Geo() {{
            setLat(lat);
            setLng(lng);
        }};

        assertEquals(g.getLat(), lat);
        assertEquals(g.getLng(), lng);
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingLatitudeToInvalidValueTriggersException() {
        new Geo() {{
            setLat("91.0139");
        }};
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingLongitudeToInvalidValueTriggersException() {
        new Geo() {{
            setLng("191.0139");
        }};
    }

    @Test
    public void getDistanceBetweenSameGeoEqualsZero() {
        Geo g = new Geo() {{
            setLat("12.4567");
            setLng("-12.4567");
        }};

        assertEquals(0, g.getDistanceTo(g), 0.001);
    }

    @Test
    public void getDistanceBetweenEqualCoordinatesEqualZero() {
        Geo g1 = new Geo() {{
            setLat("12.4567");
            setLng("-12.4567");
        }};
        Geo g2 = new Geo() {{
            setLat("12.4567");
            setLng("-12.4567");
        }};

        assertEquals(0, g1.getDistanceTo(g2), 0.001);
    }

    @Test
    public void getDistanceBetweenCenterGeoAndTwoSymmetricalGeosIsEqual() {
        Geo g1 = new Geo() {{
            setLat("-50");
            setLng("-50");
        }};
        Geo g2 = new Geo() {{
            setLat("50");
            setLng("50");
        }};

        assertEquals(center.getDistanceTo(g1), center.getDistanceTo(g2), 0.001);
    }

    @Test
    public void getDistanceBetweenCenterAndDistantGeoIsGreaterThanDistanceToCloserOne() {
        Geo distant = new Geo() {{
            setLat("50");
            setLng("50");
        }};
        Geo closer = new Geo() {{
            setLat("-45");
            setLng("-20");
        }};

        assertTrue(center.getDistanceTo(distant) > center.getDistanceTo(closer));
    }

    @Test
    public void getDistanceTreatsDifferenceInOneUnitOfLongitudeAsInOneUnitOfLatitude() {
        Geo distant = new Geo() {{
            setLat("50");
            setLng("51");
        }};
        Geo closer = new Geo() {{
            setLat("51");
            setLng("50");
        }};

        assertEquals(center.getDistanceTo(distant), center.getDistanceTo(closer), 0.001);
    }

    @Test
    public void distanceBetweenAPointFarFarEastAndAPointFarFarWestIsShorterThanBetweenThemAndCenter() {
        //checkmate, flat-earthers!
        Geo farEast = new Geo() {{
            setLat("0");
            setLng("180");
        }};
        Geo farWest = new Geo() {{
            setLat("0");
            setLng("-180");
        }};

        assertTrue(farEast.getDistanceTo(farWest) < farEast.getDistanceTo(center));
    }

    @Test
    public void differenceBetweenEquatorialAndMeridionalCircumferencesOfEarthIsAbout67Km() {
        // The fact is caused by the Earth's flattening at the poles
        Geo northPole = new Geo() {{
            setLat("90");
            setLng("0");
        }};
        Geo southPole = new Geo() {{
            setLat("-90");
            setLng("0");
        }};
        double equatorialCircumference = 40075.017;
        double meridionalCircumference = 2 * northPole.getDistanceTo(southPole);

        assertEquals(67.154, equatorialCircumference - meridionalCircumference, 25.0f);
        // Allow for +-25km absolute error because of use of doubles and imperfect lang.Math functions
    }

}
