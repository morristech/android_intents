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

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A {@link BaseIntent} builder implementation providing API for building and starting of intents
 * targeting a <b>calendar</b> related applications.
 * <p>
 * Whether a started calendar intent should insert a new event or edit the existing one may be
 * specified via {@link #type(int)} and supplying one of {@link #TYPE_VIEW}, {@link #TYPE_INSERT_EVENT},
 * {@link #TYPE_EDIT_EVENT}, {@link #TYPE_VIEW_EVENT}.
 *
 * <h3>Intent type</h3>
 * <ul>
 * <li>
 * {@link #TYPE_VIEW}
 * <p>
 * Use this type when you want to open only calendar at the current date for preview. If you want to
 * open calendar at different date, you can specify your desired date via {@link #beginTime(long)}.
 * </li>
 * <li>
 * {@link #TYPE_VIEW_EVENT}
 * <p>
 * Use this type when you want to open an existing calendar event for preview. This type requires an
 * id of the desired event to be specified via {@link #eventId(long)} otherwise calendar intent cannot
 * be build properly and {@link #build(Context)} will return {@code null}.
 * </li>
 * <li>
 * {@link #TYPE_EDIT_EVENT}
 * <p>
 * Use this type when you want to open an existing calendar event for editing. This type requires an
 * id of the desired event like described for {@link #TYPE_VIEW_EVENT} above.
 * </li>
 * <li>
 * {@link #TYPE_INSERT_EVENT}
 * <p>
 * Use this type when you want to create a new calendar event. This type requires a begin time to be
 * specified via {@link #beginTime(long)} and an end time via {@link #endTime(long)}. Following data
 * are relatively optional but for 'good' calendar event should be always set: {@link #title(CharSequence)},
 * {@link #description(CharSequence)}, {@link #location(CharSequence)}, {@link #availability(int)}.
 * </li>
 * </ul>
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public class CalendarIntent extends BaseIntent<CalendarIntent> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "CalendarIntent";

	/**
	 * Flag to identify CalendarIntent as intent to open calendar. Can be passed only to {@link #type(int)}.
	 */
	public static final int TYPE_VIEW = 0x01;

	/**
	 * Flag to identify CalendarIntent as intent to create new calendar event. Can be passed only to
	 * {@link #type(int)}.
	 */
	public static final int TYPE_INSERT_EVENT = 0x02;

	/**
	 * Flag to identify CalendarIntent as intent to edit existing calendar event. Can be passed only
	 * to {@link #type(int)}.
	 */
	public static final int TYPE_EDIT_EVENT = 0x03;

	/**
	 * Flag to identify CalendarIntent as intent to view existing calendar event. Can be passed only
	 * to {@link #type(int)}.
	 */
	public static final int TYPE_VIEW_EVENT = 0x04;

	/**
	 * Defines an annotation for determining set of allowed flags for {@link #type(int)} method.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef({TYPE_VIEW, TYPE_INSERT_EVENT, TYPE_EDIT_EVENT, TYPE_VIEW_EVENT})
	public @interface Type {}

	/**
	 * Flag to identify BUSY availability for the newly creating calendar event.
	 * See {@link CalendarContract.Events#AVAILABILITY_BUSY} for additional info.
	 */
	public static final int AVAILABILITY_BUSY = CalendarContract.Events.AVAILABILITY_BUSY;

	/**
	 * Flag to identify FREE availability for the newly creating calendar event.
	 * See {@link CalendarContract.Events#AVAILABILITY_FREE} for additional info.
	 */
	public static final int AVAILABILITY_FREE = CalendarContract.Events.AVAILABILITY_FREE;

	/**
	 * Flag to identify TENTATIVE availability for the newly creating calendar event.
	 * See {@link CalendarContract.Events#AVAILABILITY_TENTATIVE} for additional info.
	 */
	public static final int AVAILABILITY_TENTATIVE = 0x02;

	/**
	 * Defines an annotation for determining set of allowed flags for {@link #availability(int)} method.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef({AVAILABILITY_BUSY, AVAILABILITY_FREE, AVAILABILITY_TENTATIVE})
	public @interface Availability {}

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Type of intent that to be created by this builder.
	 */
	private int type = TYPE_VIEW;

	/**
	 * Id of newly creating or already existing calendar event.
	 */
	private long eventId = -1;

	/**
	 * Begin time for newly creating calendar event.
	 */
	private long beginTime;

	/**
	 * End time for newly creating calendar event.
	 */
	private long endTime;

	/**
	 * Title for newly creating calendar event.
	 */
	private CharSequence title;

	/**
	 * Description for newly creating calendar event.
	 */
	private CharSequence description;

	/**
	 * Location for newly creating calendar event.
	 */
	private CharSequence location;

	/**
	 * Availability for newly creating calendar event.
	 */
	private int availability = AVAILABILITY_BUSY;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of CalendarIntent.
	 * <p>
	 * The begin time will be initially set to the current time and the end time to begin time + 1.
	 *
	 * @see #beginTime(long)
	 * @see #endTime(long)
	 */
	public CalendarIntent() {
		super();
		this.beginTime = System.currentTimeMillis();
		this.endTime = beginTime + 1;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Sets a type determining whether to create, view or edit an event in the calendar.
	 * <p>
	 * Default value: <b>{@link #TYPE_VIEW}</b>
	 *
	 * @param type One of {@link #TYPE_VIEW}, {@link #TYPE_INSERT_EVENT}, {@link #TYPE_EDIT_EVENT},
	 *             {@link #TYPE_VIEW_EVENT}.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #type()
	 */
	public CalendarIntent type(@Type final int type) {
		this.type = type;
		return this;
	}

	/**
	 * Returns the type of the intent that will be created by this intent builder.
	 *
	 * @return One of {@link #TYPE_VIEW}, {@link #TYPE_INSERT_EVENT}, {@link #TYPE_EDIT_EVENT},
	 * {@link #TYPE_VIEW_EVENT}.
	 *
	 * @see #type(int)
	 */
	@Type public int type() {
		return type;
	}

	/**
	 * Sets an id of the calendar event to be edited or viewed. This id is only used in case of
	 * intent type {@link #TYPE_EDIT_EVENT} or {@link #TYPE_VIEW_EVENT}.
	 * <p>
	 * Default value: <b>{@code -1}</b>
	 *
	 * @param eventId Id of the desired calendar event (to edit or view).
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #eventId()
	 * @see #type(int)
	 */
	public CalendarIntent eventId(@IntRange(from = 1) final long eventId) {
		this.eventId = eventId;
		return this;
	}

	/**
	 * Returns the id of the event to edit or view within calendar.
	 *
	 * @return Event id.
	 *
	 * @see #eventId(long)
	 */
	public long eventId() {
		return eventId;
	}

	/**
	 * Sets a time determining where to open the calendar for view. This time is only used in case
	 * of intent type {@link #TYPE_VIEW}.
	 * <p>
	 * Default value: <b>{@link System#currentTimeMillis()}</b>
	 *
	 * @param time The desired time where to open the calendar.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #time()
	 */
	public CalendarIntent time(@IntRange(from = 0) final long time) {
		this.beginTime = time;
		return this;
	}

	/**
	 * Returns the time determining where to open the calendar for view.
	 *
	 * @return Time for calendar in milliseconds.
	 *
	 * @see #time(long)
	 */
	public long time() {
		return beginTime;
	}

	/**
	 * Sets a begin time for the calendar event to be newly created. This time is only used in case
	 * of intent type {@link #TYPE_INSERT_EVENT} or {@link #TYPE_VIEW}.
	 * <p>
	 * Default value: <b>{@link System#currentTimeMillis()}</b>
	 *
	 * @param beginTime The desired begin time for the new event in milliseconds.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #endTime(long)
	 * @see #type(int)
	 * @see #beginTime()
	 */
	public CalendarIntent beginTime(@IntRange(from = 0) final long beginTime) {
		this.beginTime = beginTime;
		return this;
	}

	/**
	 * Returns the begin time for the new calendar event.
	 *
	 * @return Begin time for calendar event in milliseconds.
	 *
	 * @see #beginTime(long)
	 * @see #endTime(long)
	 */
	public long beginTime() {
		return beginTime;
	}

	/**
	 * Sets an end time for the calendar event to be newly created. This time is only used in case
	 * of intent type {@link #TYPE_INSERT_EVENT}.
	 * <p>
	 * <b>Note</b>, that the default end time is set to {@link #beginTime()} {@code +1}.
	 *
	 * @param endTime The desired end time for the new event in milliseconds.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #beginTime(long)
	 * @see #type(int)
	 * @see #endTime()
	 */
	public CalendarIntent endTime(@IntRange(from = 0) final long endTime) {
		this.endTime = endTime;
		return this;
	}

	/**
	 * Returns the end time for the new calendar event.
	 *
	 * @return End time for calendar event in milliseconds.
	 *
	 * @see #endTime(long)
	 * @see #beginTime(long)
	 */
	public long endTime() {
		return endTime;
	}

	/**
	 * Sets a title for the calendar event to be newly created. This text is used only in case of
	 * intent type {@link #TYPE_INSERT_EVENT} or {@link #TYPE_EDIT_EVENT}.
	 *
	 * @param title The desired title text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #title()
	 */
	public CalendarIntent title(@Nullable final CharSequence title) {
		this.title = title;
		return this;
	}

	/**
	 * Returns the title for the new calendar event.
	 *
	 * @return Title text for calendar event.
	 *
	 * @see #title(CharSequence)
	 */
	@NonNull public CharSequence title() {
		return title == null ? "" : title;
	}

	/**
	 * Sets a description for the calendar event to be newly created. This text is only used in case
	 * of intent type {@link #TYPE_INSERT_EVENT}.
	 *
	 * @param description The desired description text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #description()
	 */
	public CalendarIntent description(@Nullable final CharSequence description) {
		this.description = description;
		return this;
	}

	/**
	 * Returns the description for the new calendar event.
	 *
	 * @return Description text for calendar event.
	 *
	 * @see #description(CharSequence)
	 */
	@NonNull public CharSequence description() {
		return description == null ? "" : description;
	}

	/**
	 * Sets a location for the calendar event to be newly created. This text is only used in case of
	 * intent type {@link #TYPE_INSERT_EVENT}.
	 *
	 * @param location The desired location text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #location()
	 */
	public CalendarIntent location(@Nullable final CharSequence location) {
		this.location = location;
		return this;
	}

	/**
	 * Returns the location for the new calendar event.
	 *
	 * @return Location text for calendar event.
	 *
	 * @see #location(CharSequence)
	 */
	@NonNull public CharSequence location() {
		return location == null ? "" : location;
	}

	/**
	 * Sets an availability flag for the calendar event to be newly created. This flag is used only
	 * in case of intent type {@link #TYPE_INSERT_EVENT}.
	 * <p>
	 * Default value: <b>{@link #AVAILABILITY_BUSY}</b>
	 *
	 * @param availability One of {@link #AVAILABILITY_BUSY}, {@link #AVAILABILITY_FREE} or {@link #AVAILABILITY_TENTATIVE}.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #availability()
	 */
	public CalendarIntent availability(@Availability final int availability) {
		this.availability = availability;
		return this;
	}

	/**
	 * Returns the availability flag for the new calendar event.
	 * <p>
	 * Default value: <b>{@link #AVAILABILITY_BUSY}</b>
	 *
	 * @return Availability flag. One of {@link #AVAILABILITY_BUSY}, {@link #AVAILABILITY_FREE} or
	 * {@link #AVAILABILITY_TENTATIVE}.
	 *
	 * @see #availability(int)
	 */
	@Availability public int availability() {
		return availability;
	}

	/**
	 */
	@Override protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		switch (type) {
			case TYPE_VIEW:
				if (beginTime < 0) {
					throw cannotBuildIntentException(
							"Specified invalid time(" + beginTime + ") where to open calendar for view. " +
									"Must be none-negative time value."
					);
				}
				break;
			case TYPE_INSERT_EVENT:
				if (beginTime < 0) {
					throw cannotBuildIntentException(
							"Specified invalid begin time(" + beginTime + "). " +
									"Must be none-negative time value."
					);
				}
				if (endTime < 0) {
					throw cannotBuildIntentException(
							"Specified invalid end time(" + endTime + "). " +
									"Must be none-negative time value."
					);
				}
				if (endTime <= beginTime) {
					throw cannotBuildIntentException(
							"Specified end time(" + endTime + ") is before/at begin time(" + beginTime + "). " +
									"Must be greater than the begin time."
					);
				}
				break;
			case TYPE_EDIT_EVENT:
			case TYPE_VIEW_EVENT:
			default:
				if (eventId <= 0) {
					throw cannotBuildIntentException("Specified invalid event id(" + eventId + ").");
				}
				break;
		}
	}

	/**
	 */
	@SuppressWarnings("ConstantConditions")
	@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
		switch (type) {
			case TYPE_VIEW:
				final Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
				builder.appendPath("time");
				ContentUris.appendId(builder, beginTime);
				return new Intent(Intent.ACTION_VIEW).setData(builder.build());
			case TYPE_INSERT_EVENT:
				return new Intent(Intent.ACTION_INSERT)
						.setData(CalendarContract.Events.CONTENT_URI)
						.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime)
						.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
						.putExtra(CalendarContract.Events.TITLE, title)
						.putExtra(CalendarContract.Events.DESCRIPTION, description)
						.putExtra(CalendarContract.Events.EVENT_LOCATION, location)
						.putExtra(CalendarContract.Events.AVAILABILITY, availability);
			case TYPE_EDIT_EVENT:
				final Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setData(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId));
				if (!TextUtils.isEmpty(title)) {
					intent.putExtra(CalendarContract.Events.TITLE, title);
				}
				return intent;
			default:
				return new Intent(Intent.ACTION_VIEW).setData(
						ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
				);
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */
}