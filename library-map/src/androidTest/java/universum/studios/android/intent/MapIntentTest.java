/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License
 * you may obtain at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * You can redistribute, modify or publish any part of the code written within this file but as it
 * is described in the License, the software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.intent;

import android.content.Intent;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static universum.studios.android.intent.MapTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ResourceType")
public final class MapIntentTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "MapIntentTest";

	@Test
	public void testUriScheme() {
		assertThat(MapIntent.URI_SCHEME, is("geo"));
	}

	@Test
	public void testLatDefault() {
		assertThat(new MapIntent().lat(), is(0d));
	}

	@Test
	public void testLngDefault() {
		assertThat(new MapIntent().lng(), is(0d));
	}

	@Test
	public void testLocation() {
		final MapIntent intent = new MapIntent();
		intent.location(24.16546d, 135.154d);
		assertThat(intent.lat(), is(24.16546d));
		assertThat(intent.lng(), is(135.154d));
	}

	@Test
	public void testLocationOutOfRange() {
		final MapIntent intent = new MapIntent();
		intent.location(91.07d, 194.77d);
		assertThat(intent.lat(), is(MapIntent.LAT_MAX));
		assertThat(intent.lng(), is(MapIntent.LNG_MAX));
		intent.location(-90.07d, -194.77d);
		assertThat(intent.lat(), is(MapIntent.LAT_MIN));
		assertThat(intent.lng(), is(MapIntent.LNG_MIN));
	}

	@Test
	public void testLocationQueryDefault() {
		assertThat(new MapIntent().locationQuery(), is(""));
	}

	@Test
	public void testLocationQueryText() {
		final MapIntent intent = new MapIntent();
		intent.locationQuery("Iceland");
		assertThat(intent.locationQuery(), is("Iceland"));
	}

	@Test
	public void testZoomLevelDefault() {
		assertThat(new MapIntent().zoomLevel(), is(0));
	}

	@Test
	public void testZoomLevel() {
		final MapIntent intent = new MapIntent();
		intent.zoomLevel(12);
		assertThat(intent.zoomLevel(), is(12));
	}

	@Test
	public void testZoomLevelOutOfRange() {
		final MapIntent intent = new MapIntent();
		intent.zoomLevel(-3);
		assertThat(intent.zoomLevel(), is(MapIntent.ZOOM_LEVEL_MIN));
		intent.zoomLevel(50);
		assertThat(intent.zoomLevel(), is(MapIntent.ZOOM_LEVEL_MAX));
	}

	@Test
	public void testLabelDefault() {
		assertThat(new MapIntent().label(), is(""));
	}

	@Test
	public void testLabelText() {
		final MapIntent intent = new MapIntent();
		intent.label("Venice");
		assertThat(intent.label(), is("Venice"));
	}

	@Test
	public void testBuildWithLocation() {
		final Intent intent = new MapIntent().location(40.7141667, -74.0063889).build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889)))
		);
	}

	@Test
	public void testBuildWithLocationAndZoom() {
		final Intent intent = new MapIntent().location(40.7141667, -74.0063889).zoomLevel(10).build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "?z=" + Integer.toString(10)))
		);
	}

	@Test
	public void testBuildWithLocationAndLocationQuery() {
		final Intent intent = new MapIntent().location(40.7141667, -74.0063889).locationQuery("restaurants").build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "?q=" + Uri.encode("restaurants")))
		);
	}

	@Test
	public void testBuildWithLocationZoomAndLocationQuery() {
		final Intent intent = new MapIntent().location(40.7141667, -74.0063889).zoomLevel(10).locationQuery("restaurants").build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "?z=" + Integer.toString(10) + "&q=" + Uri.encode("restaurants")))
		);
	}

	@Test
	public void testBuildWithLocationAndLabel() {
		final Intent intent = new MapIntent().location(40.7141667, -74.0063889).label("New York City").build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:0,0?q=" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "(" + Uri.encode("New York City") + ")"))
		);
	}

	@Test
	public void testBuildWithLocationQuery() {
		final MapIntent mapIntent = new MapIntent();
		mapIntent.locationQuery("Rome, Italy");
		final Intent intent = mapIntent.build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:0,0?q=" + Uri.encode("Rome, Italy")))
		);
	}

	@Test
	public void testBuildWithLocationQueryAndLabel() {
		final MapIntent mapIntent = new MapIntent();
		mapIntent.locationQuery("Rome, Italy");
		mapIntent.label("Rome");
		final Intent intent = mapIntent.build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:0,0?q=" + Uri.encode("Rome, Italy") + "(" + Uri.encode("Rome") + ")"))
		);
	}

	private static void assertThatIntentIsValid(Intent intent) {
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData().getScheme(), is(MapIntent.URI_SCHEME));
	}

	@Test
	public void testBuildWithoutParams() {
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				new MapIntent(),
				"No latitude and longitude nor location query specified."
		);
	}
}