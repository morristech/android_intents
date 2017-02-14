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

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.intent.test.R;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ResourceType")
public final class CalendarIntentTest extends IntentBaseTest<CalendarIntent> {

	@SuppressWarnings("unused")
	private static final String TAG = "CalendarIntentTest";

	private long curentTime;

	public CalendarIntentTest() {
		super(CalendarIntent.class);
	}

	@Override
	public void beforeTest() throws Exception {
		this.curentTime = System.currentTimeMillis();
		super.beforeTest();
	}

	@Test
	public void testDefaultType() {
		assertThat(mIntent.type(), is(CalendarIntent.TYPE_INSERT_EVENT));
	}

	@Test
	public void testType() {
		mIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		assertThat(mIntent.type(), is(CalendarIntent.TYPE_EDIT_EVENT));
	}

	@Test
	public void testInvalidType() {
		mIntent.type(12);
		assertThat(mIntent.type(), is(CalendarIntent.TYPE_INSERT_EVENT));
	}

	@Test
	public void testDefaultEventId() {
		assertThat(mIntent.eventId(), is(-1L));
	}

	@Test
	public void testEventId() {
		mIntent.eventId(123523);
		assertThat(mIntent.eventId(), is(123523L));
	}

	@Test
	public void testDefaultTime() {
		assertThat(mIntent.time(), is(curentTime));
	}

	@Test
	public void testTime() {
		mIntent.time(3232);
		assertThat(mIntent.time(), is(3232L));
	}

	@Test
	public void testDefaultBeginTime() {
		assertThat(mIntent.beginTime(), is(curentTime));
	}

	@Test
	public void testBeginTime() {
		mIntent.beginTime(20);
		assertThat(mIntent.beginTime(), is(20L));
	}

	@Test
	public void testDefaultEndTime() {
		assertThat(mIntent.endTime(), is(curentTime + 1));
	}

	@Test
	public void testEndTime() {
		mIntent.endTime(13);
		assertThat(mIntent.endTime(), is(13L));
	}

	@Test
	public void testDefaultTitle() {
		assertThat(mIntent.title().toString(), is(""));
	}

	@Test
	public void testTitleResId() {
		mIntent.title(getText(R.string.test_intent_calendar_event_title));
		assertThat(mIntent.title(), is(getText(R.string.test_intent_calendar_event_title)));
	}

	@Test
	public void testTitleText() {
		mIntent.title("New event");
		assertThat(mIntent.title().toString(), is("New event"));
	}

	@Test
	public void testDefaultDescription() {
		assertThat(mIntent.description().toString(), is(""));
	}

	@Test
	public void testDescriptionText() {
		mIntent.description("Event's description text.");
		assertThat(mIntent.description().toString(), is("Event's description text."));
	}

	@Test
	public void testDefaultLocation() {
		assertThat(mIntent.location().toString(), is(""));
	}

	@Test
	public void testLocationText() {
		mIntent.location("Rome");
		assertThat(mIntent.location().toString(), is("Rome"));
	}

	@Test
	public void testDefaultAvailability() {
		assertThat(mIntent.availability(), is(CalendarIntent.AVAILABILITY_BUSY));
	}

	@Test
	public void testAvailability() {
		mIntent.availability(CalendarIntent.AVAILABILITY_FREE);
		assertThat(mIntent.availability(), is(CalendarIntent.AVAILABILITY_FREE));
	}

	@Test
	public void testUnsupportedAvailability() {
		mIntent.availability(15);
		assertThat(mIntent.availability(), is(CalendarIntent.AVAILABILITY_BUSY));
	}

	@Test
	public void testBuildTypeOfView() {
		mIntent.type(CalendarIntent.TYPE_VIEW);
		final long time = curentTime - 1000 * 60 * 60 * 24;
		mIntent.time(time);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getData(), is(Uri.parse("content://" + CalendarContract.AUTHORITY + "/time/" + Long.toString(time))));
	}

	@Test
	public void testBuildTypeOfViewWithInvalidTime() {
		mIntent.type(CalendarIntent.TYPE_VIEW);
		mIntent.time(-1000);
		assertThatBuildThrowsExceptionWithCause(
				mIntent,
				"Specified invalid time(-1000) where to open calendar for view. Must be none-negative time value."
		);
	}

	@Test
	public void testBuildTypeOfInsertEvent() {
		// INSERT EVENT type is the default one.
		// mIntent.type(CalendarIntent.TYPE_INSERT_EVENT);
		final long beginTime = curentTime;
		final long endTime = beginTime + 1000 * 60 * 60 * 12;
		mIntent.title("Event title")
				.description("Event description.")
				.location("Location of the event")
				.beginTime(beginTime).endTime(endTime)
				.availability(CalendarIntent.AVAILABILITY_FREE);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getLongExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, -1), is(beginTime));
		assertThat(intent.getLongExtra(CalendarContract.EXTRA_EVENT_END_TIME, -1), is(endTime));
		assertThat(intent.getCharSequenceExtra(CalendarContract.Events.TITLE).toString(), is("Event title"));
		assertThat(intent.getCharSequenceExtra(CalendarContract.Events.DESCRIPTION).toString(), is("Event description."));
		assertThat(intent.getCharSequenceExtra(CalendarContract.Events.EVENT_LOCATION).toString(), is("Location of the event"));
		assertThat(intent.getIntExtra(CalendarContract.Events.AVAILABILITY, -1), is(CalendarIntent.AVAILABILITY_FREE));
	}

	@Test
	public void testBuildTypeOfInsertEventWithInvalidBeginTime() {
		mIntent.beginTime(-1);
		assertThatBuildThrowsExceptionWithCause(
				mIntent,
				"Specified invalid begin time(-1). Must be none-negative time value."
		);
	}

	@Test
	public void testBuildTypeOfInsertEventWithInvalidEndTime() {
		mIntent.endTime(-5);
		assertThatBuildThrowsExceptionWithCause(
				mIntent,
				"Specified invalid end time(-5). Must be none-negative time value."
		);
	}

	@Test
	public void testBuildTypeOfInsertEventWithInvalidEndVsBeginTime() {
		final long beginTime = curentTime;
		mIntent.endTime(0);
		assertThatBuildThrowsExceptionWithCause(
				mIntent,
				"Specified end time(0) is before/at begin time(" + beginTime + "). Must be greater than the begin time."
		);
	}

	@Test
	public void testBuildTypeOfEditEvent() {
		mIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		mIntent.eventId(123);
		mIntent.title("New event title");
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_EDIT));
		assertThat(intent.getData(), is(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, 123)));
		assertThat(intent.getCharSequenceExtra(CalendarContract.Events.TITLE).toString(), is("New event title"));
	}

	@Test
	public void testBuildTypeOfEditEventWithInvalidEventId() {
		mIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		mIntent.eventId(-123);
		assertThatBuildThrowsExceptionWithCause(
				mIntent,
				"Specified invalid event id(-123)."
		);
	}

	@Test
	public void testBuildTypeOfViewEvent() {
		mIntent.type(CalendarIntent.TYPE_VIEW_EVENT);
		mIntent.eventId(123);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, 123)));
	}

	@Test
	public void testBuildTypeOfViewEventWithInvalidEventId() {
		mIntent.type(CalendarIntent.TYPE_VIEW_EVENT);
		mIntent.eventId(-123);
		assertThatBuildThrowsExceptionWithCause(
				mIntent,
				"Specified invalid event id(-123)."
		);
	}
}