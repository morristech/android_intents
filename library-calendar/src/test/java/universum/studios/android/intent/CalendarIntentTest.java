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
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static universum.studios.android.intent.CalendarTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("ResourceType")
public final class CalendarIntentTest extends RobolectricTestCase {

	@Test
	public void testTypeDefault() {
		assertThat(new CalendarIntent().type(), is(CalendarIntent.TYPE_VIEW));
	}

	@Test
	public void testType() {
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_EDIT_EVENT);
		assertThat(intent.type(), is(CalendarIntent.TYPE_EDIT_EVENT));
	}

	@Test
	public void testEventIdDefault() {
		assertThat(new CalendarIntent().eventId(), is(-1L));
	}

	@Test
	public void testEventId() {
		final CalendarIntent intent = new CalendarIntent();
		intent.eventId(123523);
		assertThat(intent.eventId(), is(123523L));
	}

	@Test
	public void testTimeDefault() {
		final long currentTime = System.currentTimeMillis();
		assertThatTimeIsInRange(new CalendarIntent().time(), currentTime, currentTime + 10);
	}

	@Test
	public void testTime() {
		final CalendarIntent intent = new CalendarIntent();
		intent.time(3232);
		assertThat(intent.time(), is(3232L));
	}

	@Test
	public void testBeginTimeDefault() {
		final long currentTime = System.currentTimeMillis();
		assertThatTimeIsInRange(new CalendarIntent().beginTime(), currentTime, currentTime + 10);
	}

	@Test
	public void testBeginTime() {
		final CalendarIntent intent = new CalendarIntent();
		intent.beginTime(20);
		assertThat(intent.beginTime(), is(20L));
	}

	@Test
	public void testDEndTimeDefault() {
		final long currentTime = System.currentTimeMillis();
		assertThatTimeIsInRange(new CalendarIntent().endTime(), currentTime + 1, currentTime + 11);
	}

	@Test
	public void testEndTime() {
		final CalendarIntent intent = new CalendarIntent();
		intent.endTime(13);
		assertThat(intent.endTime(), is(13L));
	}

	@Test
	public void testTitleDefault() {
		assertThat(new CalendarIntent().title().toString(), is(""));
	}

	@Test
	public void testTitle() {
		final CalendarIntent intent = new CalendarIntent();
		intent.title("New event");
		assertThat(intent.title().toString(), is("New event"));
	}

	@Test
	public void testDescriptionDefault() {
		assertThat(new CalendarIntent().description().toString(), is(""));
	}

	@Test
	public void testDescription() {
		final CalendarIntent intent = new CalendarIntent();
		intent.description("Event's description text.");
		assertThat(intent.description().toString(), is("Event's description text."));
	}

	@Test
	public void testLocationDefault() {
		assertThat(new CalendarIntent().location().toString(), is(""));
	}

	@Test
	public void testLocation() {
		final CalendarIntent intent = new CalendarIntent();
		intent.location("Rome");
		assertThat(intent.location().toString(), is("Rome"));
	}

	@Test
	public void testAvailabilityDefault() {
		assertThat(new CalendarIntent().availability(), is(CalendarIntent.AVAILABILITY_BUSY));
	}

	@Test
	public void testAvailability() {
		final CalendarIntent intent = new CalendarIntent();
		intent.availability(CalendarIntent.AVAILABILITY_FREE);
		assertThat(intent.availability(), is(CalendarIntent.AVAILABILITY_FREE));
	}

	@Test
	public void testBuildTypeOfView() {
		final long currentTime = System.currentTimeMillis();
		final CalendarIntent calendarIntent = new CalendarIntent();
		calendarIntent.type(CalendarIntent.TYPE_VIEW);
		final long time = currentTime - 1000 * 60 * 60 * 24;
		calendarIntent.time(time);
		final Intent intent = calendarIntent.build(application);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getData(), is(Uri.parse("content://" + CalendarContract.AUTHORITY + "/time/" + Long.toString(time))));
	}

	@Test
	public void testBuildTypeOfViewWithInvalidTime() {
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_VIEW);
		intent.time(-1000);
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"Specified invalid time(-1000) where to open calendar for view. Must be none-negative time value."
		);
	}

	@Test
	public void testBuildTypeOfInsertEvent() {
		final long currentTime = System.currentTimeMillis();
		final CalendarIntent calendarIntent = new CalendarIntent();
		// INSERT EVENT type is the default one.
		// intent.type(CalendarIntent.TYPE_INSERT_EVENT);
		final long beginTime = currentTime;
		final long endTime = beginTime + 1000 * 60 * 60 * 12;
		calendarIntent.type(CalendarIntent.TYPE_INSERT_EVENT)
				.title("Event title")
				.description("Event description.")
				.location("Location of the event")
				.beginTime(beginTime).endTime(endTime)
				.availability(CalendarIntent.AVAILABILITY_FREE);
		final Intent intent = calendarIntent.build(application);
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
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_INSERT_EVENT);
		intent.beginTime(-1);
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"Specified invalid begin time(-1). Must be none-negative time value."
		);
	}

	@Test
	public void testBuildTypeOfInsertEventWithInvalidEndTime() {
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_INSERT_EVENT);
		intent.endTime(-5);
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"Specified invalid end time(-5). Must be none-negative time value."
		);
	}

	@Test
	public void testBuildTypeOfInsertEventWithInvalidEndVsBeginTime() {
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_INSERT_EVENT);
		intent.endTime(0);
		try {
			intent.build(application);
		} catch (IllegalArgumentException e) {
			final String message = "Cannot build " + intent.getClass().getSimpleName() + ". Specified end time(0) is before/at begin";
			final String eMessage = e.getMessage();
			if (!eMessage.startsWith(message)) {
				throw new AssertionError(
						"Expected exception with message <" + message + "...> but message was <" + eMessage + "...>"
				);
			}
			return;
		}
		final String intentName = intent.getClass().getSimpleName();
		throw new AssertionError("No exception has been thrown while building intent(" + intentName + ").");
	}

	@Test
	public void testBuildTypeOfEditEvent() {
		final CalendarIntent calendarIntent = new CalendarIntent();
		calendarIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		calendarIntent.eventId(123);
		calendarIntent.title("New event title");
		final Intent intent = calendarIntent.build(application);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_EDIT));
		assertThat(intent.getData(), is(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, 123)));
		assertThat(intent.getCharSequenceExtra(CalendarContract.Events.TITLE).toString(), is("New event title"));
	}

	@Test
	public void testBuildTypeOfEditEventWithoutTitle() {
		final CalendarIntent calendarIntent = new CalendarIntent();
		calendarIntent.type(CalendarIntent.TYPE_EDIT_EVENT);
		calendarIntent.eventId(123);
		final Intent intent = calendarIntent.build(application);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_EDIT));
		assertThat(intent.getData(), is(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, 123)));
	}

	@Test
	public void testBuildTypeOfEditEventWithInvalidEventId() {
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_EDIT_EVENT);
		intent.eventId(-123);
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"Specified invalid event id(-123)."
		);
	}

	@Test
	public void testBuildTypeOfViewEvent() {
		final CalendarIntent calendarIntent = new CalendarIntent();
		calendarIntent.type(CalendarIntent.TYPE_VIEW_EVENT);
		calendarIntent.eventId(123);
		final Intent intent = calendarIntent.build(application);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, 123)));
	}

	@Test
	public void testBuildTypeOfViewEventWithInvalidEventId() {
		final CalendarIntent intent = new CalendarIntent();
		intent.type(CalendarIntent.TYPE_VIEW_EVENT);
		intent.eventId(-123);
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
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