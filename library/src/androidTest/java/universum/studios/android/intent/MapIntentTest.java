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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ResourceType")
public final class MapIntentTest extends IntentBaseTest<MapIntent> {

	@SuppressWarnings("unused")
	private static final String TAG = "MapIntentTest";

	public MapIntentTest() {
		super(MapIntent.class);
	}

	@Test
	public void testUriScheme() {
		assertThat(MapIntent.URI_SCHEME, is("geo"));
	}

	@Test
	public void testDefaultLat() {
		assertThat(mIntent.lat(), is(0d));
	}

	@Test
	public void testDefaultLng() {
		assertThat(mIntent.lng(), is(0d));
	}

	@Test
	public void testLocation() {
		mIntent.location(24.16546d, 135.154d);
		assertThat(mIntent.lat(), is(24.16546d));
		assertThat(mIntent.lng(), is(135.154d));
	}

	@Test
	public void testLocationOutOfRange() {
		mIntent.location(91.07d, 194.77d);
		assertThat(mIntent.lat(), is(90d));
		assertThat(mIntent.lng(), is(180d));
		mIntent.location(-90.07d, -194.77d);
		assertThat(mIntent.lat(), is(-90d));
		assertThat(mIntent.lng(), is(-180d));
	}

	@Test
	public void testDefaultLocationQuery() {
		assertThat(mIntent.locationQuery(), is(""));
	}

	@Test
	public void testLocationQueryText() {
		mIntent.locationQuery("Iceland");
		assertThat(mIntent.locationQuery(), is("Iceland"));
	}

	@Test
	public void testDefaultZoomLevel() {
		assertThat(mIntent.zoomLevel(), is(0));
	}

	@Test
	public void testZoomLevel() {
		mIntent.zoomLevel(12);
		assertThat(mIntent.zoomLevel(), is(12));
	}

	@Test
	public void testZoomLevelOutOfRange() {
		mIntent.zoomLevel(-3);
		assertThat(mIntent.zoomLevel(), is(1));
		mIntent.zoomLevel(50);
		assertThat(mIntent.zoomLevel(), is(23));
	}

	@Test
	public void testDefaultLabel() {
		assertThat(mIntent.label(), is(""));
	}

	@Test
	public void testLabelText() {
		mIntent.label("Venice");
		assertThat(mIntent.label(), is("Venice"));
	}

	@Test
	public void testBuildWithLatitudeLongitude() {
		Intent intent;

		// Latitude + longitude.
		mIntent.location(40.7141667, -74.0063889);
		intent = mIntent.build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889)))
		);

		// Latitude + longitude with zoom.
		mIntent = new MapIntent();
		mIntent.location(40.7141667, -74.0063889);
		mIntent.zoomLevel(10);
		intent = mIntent.build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "?z=" + Integer.toString(10)))
		);

		// Latitude + longitude with location query.
		mIntent = new MapIntent();
		mIntent.location(40.7141667, -74.0063889);
		mIntent.locationQuery("restaurants");
		intent = mIntent.build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "?q=" + Uri.encode("restaurants")))
		);

		// Latitude + longitude with zoom and location query.
		mIntent = new MapIntent();
		mIntent.location(40.7141667, -74.0063889);
		mIntent.zoomLevel(10);
		mIntent.locationQuery("restaurants");
		intent = mIntent.build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "?z=" + Integer.toString(10) + "&q=" + Uri.encode("restaurants")))
		);

		// Latitude + longitude with label.
		mIntent = new MapIntent();
		mIntent.location(40.7141667, -74.0063889);
		mIntent.label("New York City");
		intent = mIntent.build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:0,0?q=" + Double.toString(40.7141667) + "," + Double.toString(-74.0063889) + "(" + Uri.encode("New York City") + ")"))
		);
	}

	@Test
	public void testBuildWithLocationQuery() {
		Intent intent;

		// Location query.
		mIntent.locationQuery("Rome, Italy");
		intent = mIntent.build(mContext);
		assertThatIntentIsValid(intent);
		assertThat(
				intent.getData(),
				is(Uri.parse("geo:0,0?q=" + Uri.encode("Rome, Italy")))
		);

		// Location query with label.
		mIntent = new MapIntent();
		mIntent.locationQuery("Rome, Italy");
		mIntent.label("Rome");
		intent = mIntent.build(mContext);
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
		assertThatBuildThrowsExceptionWithCause(
				mIntent,
				"No latitude and longitude nor location query specified."
		);
	}
}