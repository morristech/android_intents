/*
 * *************************************************************************************************
 *                                 Copyright 2016 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.studios.android.intent;

import android.content.Intent;
import android.net.Uri;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static universum.studios.android.intent.MapTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("ResourceType")
public final class MapIntentTest extends RobolectricTestCase {

	@Test public void testUriScheme() {
		// Assert:
		assertThat(MapIntent.URI_SCHEME, is("geo"));
	}

	@Test public void testInstantiation() {
		// Act:
		final MapIntent intent = new MapIntent();
		// Assert:
		assertThat(intent.lat(), is(0d));
		assertThat(intent.lng(), is(0d));
		assertThat(intent.locationQuery(), is(""));
		assertThat(intent.zoomLevel(), is(0));
		assertThat(intent.label(), is(""));
	}

	@Test public void testLocation() {
		// Arrange:
		final MapIntent intent = new MapIntent();
		// Act:
		intent.location(24.16546d, 135.154d);
		// Assert:
		assertThat(intent.lat(), is(24.16546d));
		assertThat(intent.lng(), is(135.154d));
	}

	@Test public void testLocationOutOfRange() {
		// Arrange:
		// Act + Assert:
		final MapIntent intent = new MapIntent();
		intent.location(91.07d, 194.77d);
		assertThat(intent.lat(), is(MapIntent.LAT_MAX));
		assertThat(intent.lng(), is(MapIntent.LNG_MAX));
		intent.location(-90.07d, -194.77d);
		assertThat(intent.lat(), is(MapIntent.LAT_MIN));
		assertThat(intent.lng(), is(MapIntent.LNG_MIN));
	}

	@Test public void testLocationQueryText() {
		// Arrange:
		final MapIntent intent = new MapIntent();
		// Act:
		intent.locationQuery("Iceland");
		// Assert:
		assertThat(intent.locationQuery(), is("Iceland"));
	}

	@Test public void testZoomLevel() {
		// Arrange:
		final MapIntent intent = new MapIntent();
		// Act:
		intent.zoomLevel(12);
		// Assert:
		assertThat(intent.zoomLevel(), is(12));
	}

	@Test public void testZoomLevelOutOfRange() {
		// Arrange:
		final MapIntent intent = new MapIntent();
		// Act + Assert:
		intent.zoomLevel(-3);
		assertThat(intent.zoomLevel(), is(MapIntent.ZOOM_LEVEL_MIN));
		intent.zoomLevel(50);
		assertThat(intent.zoomLevel(), is(MapIntent.ZOOM_LEVEL_MAX));
	}

	@Test public void testLabel() {
		// Arrange:
		final MapIntent intent = new MapIntent();
		// Act:
		intent.label("Venice");
		// Assert:
		assertThat(intent.label(), is("Venice"));
	}

	@Test public void testBuildWithLocation() {
		// Arrange:
		final MapIntent mapIntent = new MapIntent().location(40.7141667, -74.0063889);
		// Act:
		final Intent intent = mapIntent.build(context);
		// Assert:
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889)))
		);
	}

	@Test public void testBuildWithLocationAndZoom() {
		// Arrange:
		final MapIntent mapIntent = new MapIntent().location(40.7141667, -74.0063889).zoomLevel(10);
		// Act:
		final Intent intent = mapIntent.build(context);
		// Assert:
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "?z=" + Integer.toString(10)))
		);
	}

	@Test public void testBuildWithLocationAndLocationQuery() {
		// Arrange:
		final MapIntent mapIntent = new MapIntent().location(40.7141667, -74.0063889).locationQuery("restaurants");
		// Act:
		final Intent intent = mapIntent.build(context);
		// Assert:
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "?q=" + Uri.encode("restaurants")))
		);
	}

	@Test public void testBuildWithLocationZoomAndLocationQuery() {
		// Arrange:
		final MapIntent mapIntent = new MapIntent().location(40.7141667, -74.0063889).zoomLevel(10).locationQuery("restaurants");
		// Act:
		final Intent intent = mapIntent.build(context);
		// Assert:
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "?z=" + Integer.toString(10) + "&q=" + Uri.encode("restaurants")))
		);
	}

	@Test public void testBuildWithLocationAndLabel() {
		// Arrange:
		final MapIntent mapIntent = new MapIntent().location(40.7141667, -74.0063889).label("New York City");
		// Act:
		final Intent intent = mapIntent.build(context);
		// Assert:
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:0,0?q=" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "(" + Uri.encode("New York City") + ")"))
		);
	}

	@Test public void testBuildWithLocationQuery() {
		// Arrange:
		final MapIntent mapIntent = new MapIntent();
		mapIntent.locationQuery("Rome, Italy");
		// Act:
		final Intent intent = mapIntent.build(context);
		// Assert:
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:0,0?q=" + Uri.encode("Rome, Italy")))
		);
	}

	@Test public void testBuildWithLocationQueryAndLabel() {
		// Arrange:
		final MapIntent mapIntent = new MapIntent();
		mapIntent.locationQuery("Rome, Italy");
		mapIntent.label("Rome");
		// Act:
		final Intent intent = mapIntent.build(context);
		// Assert:
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:0,0?q=" + Uri.encode("Rome, Italy") + "(" + Uri.encode("Rome") + ")"))
		);
	}

	@SuppressWarnings("ConstantConditions")
	private static void assertThatIntentIsValid(Intent intent) {
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData().getScheme(), is(MapIntent.URI_SCHEME));
	}

	@Test public void testBuildWithoutParams() {
		assertThatBuildThrowsExceptionWithMessage(
				context,
				new MapIntent(),
				"No latitude and longitude nor location query specified."
		);
	}
}