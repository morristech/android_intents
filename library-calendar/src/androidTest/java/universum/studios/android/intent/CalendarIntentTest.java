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

import universum.studios.android.test.BaseInstrumentedTest;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static universum.studios.android.intent.CalendarTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ResourceType")
public final class CalendarIntentTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "CalendarIntentTest";

	private CalendarIntent mIntent;
	private long mCurrentTime;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mCurrentTime = System.currentTimeMillis();
		this.mIntent = new CalendarIntent();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mCurrentTime = 0;
		this.mIntent = null;
	}

	@Test
	public void testDefaultType() {
		assertThat(mIntent.type(), is(CalendarIntent.TYPE_VIEW));
	}

	@Test
	public void testType() {
		mIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		assertThat(mIntent.type(), is(CalendarIntent.TYPE_EDIT_EVENT));
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
		assertThatTimeIsInRange(mIntent.time(), mCurrentTime, mCurrentTime + 10);
	}

	@Test
	public void testTime() {
		mIntent.time(3232);
		assertThat(mIntent.time(), is(3232L));
	}

	@Test
	public void testDefaultBeginTime() {
		assertThatTimeIsInRange(mIntent.beginTime(), mCurrentTime, mCurrentTime + 10);
	}

	@Test
	public void testBeginTime() {
		mIntent.beginTime(20);
		assertThat(mIntent.beginTime(), is(20L));
	}

	@Test
	public void testDefaultEndTime() {
		assertThatTimeIsInRange(mIntent.endTime(), mCurrentTime + 1, mCurrentTime + 11);
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
	public void testTitle() {
		mIntent.title("New event");
		assertThat(mIntent.title().toString(), is("New event"));
	}

	@Test
	public void testDefaultDescription() {
		assertThat(mIntent.description().toString(), is(""));
	}

	@Test
	public void testDescription() {
		mIntent.description("Event's description text.");
		assertThat(mIntent.description().toString(), is("Event's description text."));
	}

	@Test
	public void testDefaultLocation() {
		assertThat(mIntent.location().toString(), is(""));
	}

	@Test
	public void testLocation() {
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
	public void testBuildTypeOfView() {
		mIntent.type(CalendarIntent.TYPE_VIEW);
		final long time = mCurrentTime - 1000 * 60 * 60 * 24;
		mIntent.time(time);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getData(), is(Uri.parse("content://" + CalendarContract.AUTHORITY + "/time/" + Long.toString(time))));
	}

	@Test
	public void testBuildTypeOfViewWithInvalidTime() {
		mIntent.type(CalendarIntent.TYPE_VIEW);
		mIntent.time(-1000);
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"Specified invalid time(-1000) where to open calendar for view. Must be none-negative time value."
		);
	}

	@Test
	public void testBuildTypeOfInsertEvent() {
		// INSERT EVENT type is the default one.
		// mIntent.type(CalendarIntent.TYPE_INSERT_EVENT);
		final long beginTime = mCurrentTime;
		final long endTime = beginTime + 1000 * 60 * 60 * 12;
		mIntent.type(CalendarIntent.TYPE_INSERT_EVENT)
				.title("Event title")
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
		mIntent.type(CalendarIntent.TYPE_INSERT_EVENT);
		mIntent.beginTime(-1);
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"Specified invalid begin time(-1). Must be none-negative time value."
		);
	}

	@Test
	public void testBuildTypeOfInsertEventWithInvalidEndTime() {
		mIntent.type(CalendarIntent.TYPE_INSERT_EVENT);
		mIntent.endTime(-5);
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"Specified invalid end time(-5). Must be none-negative time value."
		);
	}

	@Test
	public void testBuildTypeOfInsertEventWithInvalidEndVsBeginTime() {
		mIntent.type(CalendarIntent.TYPE_INSERT_EVENT);
		mIntent.endTime(0);
		try {
			mIntent.build(mContext);
		} catch (IllegalArgumentException e) {
			final String message = "Cannot build " + mIntent.getClass().getSimpleName() + ". Specified end time(0) is before/at begin";
			final String eMessage = e.getMessage();
			if (!eMessage.startsWith(message)) {
				throw new AssertionError(
						"Expected exception with message <" + message + "...> but message was <" + eMessage + "...>"
				);
			}
			return;
		}
		final String intentName = mIntent.getClass().getSimpleName();
		throw new AssertionError("No exception has been thrown while building intent(" + intentName + ").");
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
	public void testBuildTypeOfEditEventWithoutTitle() {
		mIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		mIntent.eventId(123);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_EDIT));
		assertThat(intent.getData(), is(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, 123)));
	}

	@Test
	public void testBuildTypeOfEditEventWithInvalidEventId() {
		mIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		mIntent.eventId(-123);
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
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
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"Specified invalid event id(-123)."
		);
	}

	private static void assertThatTimeIsInRange(long time, long rangeStart, long rangeEnd) {
		assertTrue(
				"Time<" + time + "> is not in the range[" + rangeStart + ", " + rangeEnd + "]!",
				time >= rangeStart && time <= rangeEnd
		);
	}
}