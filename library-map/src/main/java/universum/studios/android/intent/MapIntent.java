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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * A {@link BaseIntent} builder implementation providing API for building and starting of intents
 * targeting a <b>maps</b> related applications.
 *
 * <h3>Location via latitude + longitude</h3>
 * Simple intent to show map at a specific location can be created by specifying just latitude and
 * longitude via {@link #location(double, double)}. You can also specify either label for the requested
 * location via {@link #label(String)} or zoom level via {@link #zoomLevel(int)}.
 *
 * <h3>Location via query</h3>
 * In case when you want to show map at a specific location, but you do not know its exact location
 * (lat + lng), you can specify location query via {@link #locationQuery(String)}, which can contain
 * name of place (town/city) and state that you want to show on map. In this case you can also
 * specify the location label via {@link #label(String)} as for lat + lng based map intent.
 *
 * @author Martin Albedinsky
 */
public class MapIntent extends BaseIntent<MapIntent> {

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "MapIntent";

	/**
	 * Uri scheme for <b>map</b> targeting intents.
	 * <p>
	 * Constant value: <b>geo</b>
	 */
	public static final String URI_SCHEME = "geo";

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Longitude value for map location query.
	 */
	private double mLng;

	/**
	 * Latitude value for map location query.
	 */
	private double mLat;

	/**
	 * Zoom level for map camera.
	 */
	private int mZoomLevel;

	/**
	 * Label for map location query.
	 */
	private String mLabel;

	/**
	 * Location query for map data.
	 */
	private String mLocationQuery;

	/**
	 * Flag indicating whether there was latitude + longitude set or not.
	 */
	private boolean mLatLngSet;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Sets latitude and longitude of a location to show on a map.
	 *
	 * @param lat The desired location's latitude position in the range {@code [-90, 90]}.
	 * @param lng The desired location's longitude position in the range {@code [-180, 180]}.
	 * @return This intent builder to allow methods chaining.
	 * @see #lat()
	 * @see #lng()
	 */
	public MapIntent location(@FloatRange(from = -90, to = 90) double lat, @FloatRange(from = -180, to = 180) double lng) {
		this.mLat = Math.max(-90, Math.min(90, lat));
		this.mLng = Math.max(-180, Math.min(180, lng));
		this.mLatLngSet = true;
		return this;
	}

	/**
	 * Returns the latitude position of a location to show on map.
	 *
	 * @return Latitude position on a map from the range {@code [-90, 90]}, {@code 0} by default.
	 * @see #location(double, double)
	 * @see #lng()
	 */
	@FloatRange(from = -90, to = 90)
	public double lat() {
		return mLat;
	}

	/**
	 * Returns the longitude position of a location to show on map.
	 *
	 * @return Longitude position on a map from the range {@code [-180, 180]}, {@code 0} by default.
	 * @see #location(double, double)
	 * @see #lat()
	 */
	@FloatRange(from = -180, to = 180)
	public double lng() {
		return mLng;
	}

	/**
	 * Sets a location query that will be used to target a desired location on a map.
	 *
	 * @param query The desired location query. Can be for example name of a desired town with street
	 *              address and address number to show on a map.
	 * @return This intent builder to allow methods chaining.
	 * @see #locationQuery()
	 */
	public MapIntent locationQuery(@Nullable String query) {
		this.mLocationQuery = query;
		return this;
	}

	/**
	 * Returns the location query targeting a specific location on a map.
	 *
	 * @return Location query or empty string in not specified yet.
	 * @see #locationQuery(String)
	 */
	@NonNull
	public String locationQuery() {
		return mLocationQuery == null ? "" : mLocationQuery;
	}

	/**
	 * Sets a zoom level at which should be a desired location on a map displayed to a user.
	 *
	 * @param level The desired zoom level in the range {@code [1, 23]}. The highest level, the closest
	 *              to map.
	 * @return This intent builder to allow methods chaining.
	 * @see #zoomLevel()
	 */
	public MapIntent zoomLevel(@IntRange(from = 1, to = 23) int level) {
		this.mZoomLevel = Math.max(1, Math.min(23, level));
		return this;
	}

	/**
	 * Returns the zoom level at which will be map displayed.
	 *
	 * @return Zoom level from the range {@code [1, 23]} or {@code 0} by default.
	 * @see #zoomLevel(int)
	 */
	@IntRange(from = 0, to = 23)
	public int zoomLevel() {
		return mZoomLevel;
	}

	/**
	 * Sets a text by which should be labeled the requested location on map.
	 *
	 * @param label The desired label text.
	 * @return This intent builder to allow methods chaining.
	 * @see #label()
	 */
	public MapIntent label(@Nullable String label) {
		this.mLabel = label;
		return this;
	}

	/**
	 * Returns the label text for the requested map location.
	 *
	 * @return Label text.
	 * @see #label(String)
	 */
	@NonNull
	public String label() {
		return mLabel == null ? "" : mLabel;
	}

	/**
	 */
	@Override
	protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (!mLatLngSet && TextUtils.isEmpty(mLocationQuery)) {
			throw cannotBuildIntentException("No latitude and longitude nor location query specified.");
		}
	}

	/**
	 */
	@NonNull
	@Override
	protected Intent onBuild(@NonNull Context context) {
		final StringBuilder uriBuilder = new StringBuilder(64);
		if (mLatLngSet) {
			if (TextUtils.isEmpty(mLabel)) {
				uriBuilder.append(mLat);
				uriBuilder.append(",");
				uriBuilder.append(mLng);
				if (mZoomLevel != 0) {
					uriBuilder.append("?z=");
					uriBuilder.append(mZoomLevel);
				}
				if (!TextUtils.isEmpty(mLocationQuery)) {
					uriBuilder.append(mZoomLevel != 0 ? "&" : "?");
					this.appendLocationQuery(uriBuilder);
				}
			} else {
				uriBuilder.append("0,0?q=");
				uriBuilder.append(mLat);
				uriBuilder.append(",");
				uriBuilder.append(mLng);
				this.appendLabel(uriBuilder);
			}
		} else {
			uriBuilder.append("0,0?");
			this.appendLocationQuery(uriBuilder);
			if (!TextUtils.isEmpty(mLabel)) {
				this.appendLabel(uriBuilder);
			}
		}
		final Uri locationUri = Uri.parse(URI_SCHEME + ":" + uriBuilder.toString());
		if (IntentsConfig.DEBUG_LOG_ENABLED) {
			Log.v(TAG, "Building intent with uri('" + locationUri.toString() + "').");
		}
		return new Intent(Intent.ACTION_VIEW, locationUri);
	}

	/**
	 * Appends {@link #mLocationQuery} to the specified <var>uriBuilder</var>.
	 * <p>
	 * <b>Note</b>, that before adding value of the location query there will be added also
	 * {@code q=} query parameter. Also location query will be encoded via {@link Uri#encode(String)}.
	 *
	 * @param uriBuilder The builder where to append the location query value.
	 */
	private void appendLocationQuery(StringBuilder uriBuilder) {
		uriBuilder.append("q=").append(Uri.encode(mLocationQuery));
	}

	/**
	 * Appends {@link #mLabel} to the specified <var>uriBuilder</var>.
	 * <p>
	 * <b>Note</b>, that label will be enclosed in "()" bracekts.
	 *
	 * @param uriBuilder The builder where to append the label value.
	 */
	private void appendLabel(StringBuilder uriBuilder) {
		uriBuilder.append("(").append(Uri.encode(mLabel)).append(")");
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
