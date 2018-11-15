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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A {@link BaseIntent} builder implementation providing base API for building and starting of intents
 * targeting a <b>content previewing/editing/obtaining</b> related applications.
 * <p>
 * The content intent may be intent to obtain or preview specific type of content, like <b>image,
 * audio, video, ...</b>. To decide which type of intent (obtain/preview) should be started, the
 * current set of {@link ContentHandler} is checked, if it isn't empty an <b>OBTAIN</b> intent will
 * be started, so <b>chooser</b> dialog will be showed with a list items specific for each of current
 * providers. If there are no providers assigned to this intent builder and there was specified valid
 * {@link Uri} to {@link #input(Uri)}, a <b>PREVIEW</b> intent will be started.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public abstract class ContentIntent<I extends ContentIntent<I>> extends BaseIntent<I> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ContentIntent";

	/**
	 * Name format for files created by this type of intent.
	 * <p>
	 * Constant Value: <b>yyyyMMdd_HHmmss</b>
	 */
	public static final String CONTENT_FILE_TIME_STAMP_FORMAT = "yyyyMMdd_HHmmss";

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
	 * Uri to content.
	 */
	Uri uri;

	/**
	 * Data type of content pointed by {@link #uri}.
	 */
	String dataType;

	/**
	 * Flag indicating whether {@link #uri} has been specified as input uri via {@link #input(Uri)}
	 * or not.
	 */
	private boolean hasInputUri;

	/**
	 * Set of content handlers that will be used to create a chooser dialog (if there are any).
	 */
	private List<ContentHandler> handlers;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a time stamp in the {@link #CONTENT_FILE_TIME_STAMP_FORMAT} format for the current
	 * {@link Date} that may be used as name for a content file.
	 *
	 * @return String representation of the current time stamp.
	 */
	@NonNull public static String createContentFileTimeStamp() {
		return new SimpleDateFormat(CONTENT_FILE_TIME_STAMP_FORMAT, Locale.getDefault()).format(new Date());
	}

	/**
	 * Same as {@link #createContentFile(String, File)} with <b>directory</b> obtained via
	 * {@link Environment#getExternalStoragePublicDirectory(String)} with the specified
	 * <var>externalDirectoryType</var> as <var>type</var>.
	 *
	 * @param fileName              The desired name for the requested file.
	 * @param externalDirectoryType One of {@link Environment#DIRECTORY_PICTURES}, {@link Environment#DIRECTORY_MOVIES},
	 *                              ..., external directory types.
	 */
	@Nullable public static File createContentFile(@NonNull final String fileName, @NonNull final String externalDirectoryType) {
		return createContentFile(fileName, Environment.getExternalStoragePublicDirectory(externalDirectoryType));
	}

	/**
	 * Creates a new file with the given parameters within the specified <var>directory</var>.
	 *
	 * @param fileName  The desired name for the requested file. Must also contain a suffix for the
	 *                  file.
	 * @param directory The directory within which should be the requested file created.
	 * @return New instance of the desired file or {@code null} if some IO error occurs during its
	 * creation process.
	 *
	 * @see #createContentFile(String, String)
	 */
	@Nullable public static File createContentFile(@NonNull final String fileName, @NonNull final File directory) {
		try {
			final File file = new File(directory.getPath() + File.separator + fileName);
			return file.createNewFile() ? file : null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Appends the specified <var>suffix</var> to the specified <var>fileName</var> if there is not
	 * presented any yet.
	 * <p>
	 * <b>Note</b>, that this will append the given suffix only in case that the specified file name
	 * does not contain any "." char.
	 *
	 * @param fileName      The file name where to append the suffix.
	 * @param defaultSuffix The suffix to append if necessary.
	 * @return The given file name with the appended suffix if necessary.
	 */
	static String appendDefaultFileSuffixIfNotPresented(final String fileName, final String defaultSuffix) {
		return fileName.contains(".") ? fileName : fileName + defaultSuffix;
	}

	/**
	 * Attaches default content handlers to this intent.
	 * <p>
	 * Type and count of default handlers may differ depending on a specific ContentIntent implementation.
	 *
	 * @return This intent builder to allow methods chaining.
	 */
	public abstract ContentIntent withDefaultHandlers(@NonNull Context context);

	/**
	 * Same as {@link #withHandlers(List)} for variable array of ContentHandlers.
	 *
	 * @param handlers The desired array of handlers to add.
	 */
	public I withHandlers(@NonNull final ContentHandler... handlers) {
		return withHandlers(Arrays.asList(handlers));
	}

	/**
	 * Same as {@link #withHandler(ContentHandler)} for list of handler items.
	 *
	 * @param handlers The desired list of handlers items to add. May be {@code null} to clear the
	 *                 current one.
	 * @return This intent builder to allow methods chaining.
	 */
	@SuppressWarnings("unchecked")
	public I withHandlers(@Nullable final List<ContentHandler> handlers) {
		if (handlers == null) {
			this.handlers = null;
		} else if (!handlers.isEmpty()) {
			if (this.handlers == null) {
				this.handlers = new ArrayList<>(handlers.size());
			}
			this.handlers.addAll(handlers);
		}
		return (I) this;
	}

	/**
	 * Adds the specified content <var>handler</var> item into the list of handlers. These handler
	 * items will be used to build a chooser dialog with list of these items (if any) so a user may
	 * choose one of them to handle a specific content intent. Such dialog is created and showed whenever
	 * content intent is started via {@link #startWith(IntentStarter)} and there is at least one
	 * handler item.
	 *
	 * @param handler The desired handler item to add.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #withHandlers(ContentHandler...)
	 * @see #withDefaultHandlers(Context)
	 * @see #handlers()
	 */
	@SuppressWarnings("unchecked")
	public I withHandler(@NonNull final ContentHandler handler) {
		if (handlers == null) this.handlers = new ArrayList<>(1);
		handlers.add(handler);
		return (I) this;
	}

	/**
	 * Returns the list of content handlers to be displayed in a chooser dialog.
	 *
	 * @return List of handlers or {@link Collections#EMPTY_LIST} if no content handlers has been
	 * added yet.
	 *
	 * @see #withHandler(ContentHandler)
	 * @see #withHandlers(List)
	 * @see #withDefaultHandlers(Context)
	 */
	@SuppressWarnings("unchecked")
	@NonNull public List<ContentHandler> handlers() {
		return handlers == null ? Collections.EMPTY_LIST : new ArrayList<>(handlers);
	}

	/**
	 * Same as {@link #input(Uri)} with <var>uri</var> created from the given <var>file</var> if not
	 * {@code null}.
	 *
	 * @param file The desired file to be used to crate input Uri. May be {@code null} to clear
	 *             the current input uri.
	 */
	@SuppressWarnings("unchecked")
	public I input(@Nullable final File file) {
		if (file == null) input((Uri) null);
		else input(Uri.fromFile(file));
		return (I) this;
	}

	/**
	 * Sets an Uri to a content that should be previewed by an activity that can handle/preview the
	 * content of the data type specified via {@link #dataType(String)}. The specified Uri will be
	 * attached to an intent build via {@link #build(Context)} if there are no content handlers
	 * attached to this intent builder.
	 * <p>
	 * <b>Note</b>, that the current <b>data type</b> will be set to {@code null}, so {@link #dataType(String)}
	 * should be called immediately after a new Uri is set. A specific implementations of this
	 * ContentIntent builder may here specify a default data type.
	 *
	 * @param uri The desired uri, which should be delivered to the handling activity. May be
	 *            {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #uri()
	 */
	@SuppressWarnings("unchecked")
	public I input(@Nullable final Uri uri) {
		this.uri = uri;
		this.dataType = null;
		this.hasInputUri = uri != null;
		return (I) this;
	}

	/**
	 * Same as {@link #output(Uri)} with <var>uri</var> created from the given <var>file</var> if not
	 * {@code null}.
	 *
	 * @param file The desired file to be used to crate Uri. May be {@code null} to clear the current
	 *             output uri.
	 */
	@SuppressWarnings("unchecked")
	public I output(@Nullable final File file) {
		if (file == null) output((Uri) null);
		else output(Uri.fromFile(file));
		return (I) this;
	}

	/**
	 * Sets an Uri where should be stored a content provided by an activity that can handle/provide
	 * the content of the data type specified via {@link #dataType(String)}. The specified Uri will
	 * be attached to an intent of one of content handlers attached to this intent builder.
	 *
	 * @param uri The desired uri. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #uri()
	 */
	@SuppressWarnings("unchecked")
	public I output(@Nullable final Uri uri) {
		this.uri = uri;
		this.dataType = null;
		this.hasInputUri = false;
		return (I) this;
	}

	/**
	 * Returns the uri passed either via {@link #input(Uri)} or via {@link #output(Uri)}.
	 *
	 * @return Current uri or {@code null} if there was no uri specified yet.
	 */
	@Nullable public Uri uri() {
		return uri;
	}

	/**
	 * Sets a data (MIME) type for the content uri.
	 *
	 * @param type The desired MIME type for the uri specified via {@link #input(Uri)}.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #dataType()
	 */
	@SuppressWarnings("unchecked")
	public I dataType(@NonNull @MimeType.Value final String type) {
		this.dataType = type;
		return (I) this;
	}

	/**
	 * Returns the content's data (MIME) type.
	 *
	 * @return MIME type for the uri specified via {@link #input(Uri)} or {@code null} if no data
	 * type has been specified yet.
	 *
	 * @see #dataType(String)
	 */
	@Nullable @MimeType.Value public String dataType() {
		return dataType;
	}

	/**
	 */
	@Override public boolean startWith(@NonNull final IntentStarter starter) {
		final Context context = starter.getContext();
		if (handlers == null) {
			final Intent intent = build(context);
			if (isActivityForIntentAvailable(context, intent)) {
				return onStartWith(starter, intent);
			}
			notifyActivityNotFound(context);
			return false;
		}
		onShowChooserDialog(starter);
		return true;
	}

	/**
	 * Invoked from {@link #startWith(IntentStarter)} to show a chooser dialog if there is at least
	 * one {@link ContentHandler} attached.
	 *
	 * @param starter The intent starter that may be used to access context and also to start intent
	 *                for a selected content handler from the chooser dialog.
	 */
	protected void onShowChooserDialog(@NonNull final IntentStarter starter) {
		final int n = handlers.size();
		final CharSequence[] providerNames = new CharSequence[n];
		for (int i = 0; i < n; i++) {
			providerNames[i] = handlers.get(i).name;
		}
		final AlertDialog.Builder builder = new AlertDialog.Builder(starter.getContext());
		builder.setTitle(dialogTitle);
		builder.setItems(providerNames, new DialogInterface.OnClickListener() {

			/**
			 */
			@Override public void onClick(@NonNull final DialogInterface dialog, final int which) {
				final ContentHandler handler = handlers.get(which);
				if (handler.requestCode < 0) starter.startIntent(handler.intent);
				else starter.startIntentForResult(handler.intent, handler.requestCode);
			}
		});
		builder.show();
	}

	/**
	 * @throws IllegalStateException If there is at least one {@link ContentHandler} attached.
	 */
	@Override @NonNull public Intent build(@NonNull final Context context) {
		if (handlers == null) {
			return super.build(context);
		}
		throw new IllegalStateException("Cannot build intent for set of ContentHandlers.");
	}

	/**
	 */
	@Override protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (hasInputUri) {
			if (TextUtils.isEmpty(dataType)) {
				throw cannotBuildIntentException("No MIME type specified for input Uri.");
			}
		} else {
			throw cannotBuildIntentException("No input Uri specified.");
		}
	}

	/**
	 * Will be invoked only if there are no content handlers assigned to this intent builder.
	 */
	@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
		return new Intent(Intent.ACTION_VIEW).setDataAndType(uri, dataType);
	}

	/**
	 */
	@Override protected boolean onStartWith(@NonNull final IntentStarter starter, @NonNull final Intent intent) {
		return super.onStartWith(starter, Intent.createChooser(intent, dialogTitle));
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A ContentHandler is a simple class that may be used to add one item into {@link ContentIntent}
	 * builder. Such an item will be than displayed in a chooser dialog with all added handler items.
	 * Each ContentHandler must have its name specified that will be displayed in a list item's view.
	 * There must be also specified an intent that will be started whenever an item of a particular
	 * handler is clicked.
	 *
	 * @author Martin Albedinsky
	 * @since 1.0
	 *
	 * @see ContentHandler#ContentHandler(CharSequence, Intent)
	 */
	public static class ContentHandler {

		/**
		 * Name of this "content handler" to be displayed within chooser dialog list.
		 */
		final CharSequence name;

		/**
		 * Intent to be started when an item addressed to this handler has been clicked.
		 */
		final Intent intent;

		/**
		 * Request code used for {@link IntentStarter#startIntentForResult(Intent, int)}.
		 */
		int requestCode = -1;

		/**
		 * Creates a new instance of ContentHandler with the specified <var>name</var> and <var>intent</var>.
		 * <p>
		 * <b>Note</b>, that the given <var>intent</var> will be started via
		 * {@link IntentStarter#startIntent(Intent)} or via {@link IntentStarter#startIntentForResult(Intent, int)}
		 * if the request code specified via {@link #requestCode(int)} is {@code none-negative} number.
		 *
		 * @param name   Name of the new "content handler" to be displayed within <b>chooser dialog</b>
		 *               list.
		 * @param intent Intent to be started when an item associated with this handler within
		 *               <b>chooser dialog</b> is clicked.
		 */
		public ContentHandler(@NonNull final CharSequence name, @NonNull final Intent intent) {
			this.name = name;
			this.intent = intent;
		}

		/**
		 * Returns the name of this handler.
		 *
		 * @return This handlers's name.
		 */
		@NonNull public CharSequence name() {
			return name;
		}

		/**
		 * Returns the intent specified for this handler.
		 *
		 * @return This handler's intent.
		 */
		@NonNull public Intent intent() {
			return intent;
		}

		/**
		 * Sets a request code used when starting the intent specified during initialization.
		 *
		 * @param code The desired request code. This code should be used to identify result from
		 *             started intent in {@link Activity#onActivityResult(int, int, Intent)} or
		 *             {@link Fragment#onActivityResult(int, int, Intent)}, depends on from within
		 *             which context has been {@link ContentIntent} started.
		 * @return This handler to allow methods chaining.
		 *
		 * @see #requestCode()
		 */
		public ContentHandler requestCode(final int code) {
			this.requestCode = code;
			return this;
		}

		/**
		 * Returns the request code specified for this handler.
		 *
		 * @return This handler's request code.
		 *
		 * @see #requestCode(int)
		 */
		public int requestCode() {
			return requestCode;
		}
	}
}