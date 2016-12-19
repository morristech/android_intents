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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 */
public abstract class ContentIntent<I extends ContentIntent<I>> extends BaseIntent<I> {

	/**
	 * Interface ===================================================================================
	 */

	/**
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

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Uri to content.
	 */
	Uri mUri;

	/**
	 * Data type of content pointed by {@link #mUri}.
	 */
	String mDataType;

	/**
	 * Flag indicating whether {@link #mUri} has been specified as input uri via {@link #input(Uri)}
	 * or not.
	 */
	private boolean mHasInputUri;

	/**
	 * Set of content handlers that will be used to create a chooser dialog (if there are any).
	 */
	private List<ContentHandler> mHandlers;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ContentIntent.
	 */
	public ContentIntent() {
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a time stamp in the {@link #CONTENT_FILE_TIME_STAMP_FORMAT} format for the current
	 * {@link Date} that may be used as name for a content file.
	 *
	 * @return String representation of the current time stamp.
	 */
	@NonNull
	public static String createContentFileTimeStamp() {
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
	@Nullable
	public static File createContentFile(@NonNull String fileName, @NonNull String externalDirectoryType) {
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
	 * @see #createContentFile(String, String)
	 */
	@Nullable
	public static File createContentFile(@NonNull String fileName, @NonNull File directory) {
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
	static String appendDefaultFileSuffixIfNotPresented(String fileName, String defaultSuffix) {
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
	public I withHandlers(@NonNull ContentHandler... handlers) {
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
	public I withHandlers(@Nullable List<ContentHandler> handlers) {
		if (handlers != null) {
			if (mHandlers == null) {
				this.mHandlers = new ArrayList<>(handlers.size());
			}
			mHandlers.addAll(handlers);
		} else {
			this.mHandlers = null;
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
	 * @see #withHandlers(ContentHandler...)
	 * @see #withDefaultHandlers()
	 * @see #handlers()
	 */
	@SuppressWarnings("unchecked")
	public I withHandler(@NonNull ContentHandler handler) {
		if (mHandlers == null) this.mHandlers = new ArrayList<>(1);
		mHandlers.add(handler);
		return (I) this;
	}

	/**
	 * Returns the list of content handlers to be displayed in a chooser dialog.
	 *
	 * @return List of handlers or {@link Collections#EMPTY_LIST} if no content handlers has been
	 * added yet.
	 * @see #withHandler(ContentHandler)
	 * @see #withHandlers(List)
	 * @see #withDefaultHandlers()
	 */
	@NonNull
	@SuppressWarnings("unchecked")
	public List<ContentHandler> handlers() {
		return mHandlers != null ? new ArrayList<>(mHandlers) : Collections.EMPTY_LIST;
	}

	/**
	 * Same as {@link #input(Uri)} with <var>uri</var> created from the given <var>file</var> if not
	 * {@code null}.
	 *
	 * @param file The desired file to be used to crate input Uri. May be {@code null} to clear
	 *             the current input uri.
	 */
	@SuppressWarnings("unchecked")
	public I input(@Nullable File file) {
		if (file != null) input(Uri.fromFile(file));
		else input((Uri) null);
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
	public I input(@Nullable Uri uri) {
		this.mUri = uri;
		this.mDataType = null;
		this.mHasInputUri = uri != null;
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
	public I output(@Nullable File file) {
		if (file != null) output(Uri.fromFile(file));
		else output((Uri) null);
		return (I) this;
	}

	/**
	 * Sets an Uri where should be stored a content provided by an activity that can handle/provide
	 * the content of the data type specified via {@link #dataType(String)}. The specified Uri will
	 * be attached to an intent of one of content handlers attached to this intent builder.
	 *
	 * @param uri The desired uri. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #uri()
	 */
	@SuppressWarnings("unchecked")
	public I output(@Nullable Uri uri) {
		this.mUri = uri;
		this.mDataType = null;
		this.mHasInputUri = false;
		return (I) this;
	}

	/**
	 * Returns the uri passed either via {@link #input(Uri)} or via {@link #output(Uri)}.
	 *
	 * @return Current uri or {@code null} if there was no uri specified yet.
	 */
	@Nullable
	public Uri uri() {
		return mUri;
	}

	/**
	 * Sets a data (MIME) type for the content uri.
	 *
	 * @param type The desired MIME type for the uri specified via {@link #input(Uri)}.
	 * @return This intent builder to allow methods chaining.
	 * @see #dataType()
	 */
	@SuppressWarnings("unchecked")
	public I dataType(@NonNull @MimeType.Value String type) {
		this.mDataType = type;
		return (I) this;
	}

	/**
	 * Returns the content's data (MIME) type.
	 *
	 * @return MIME type for the uri specified via {@link #input(Uri)} or {@code null} if no data
	 * type has been specified yet.
	 * @see #dataType(String)
	 */
	@Nullable
	@MimeType.Value
	public String dataType() {
		return mDataType;
	}

	/**
	 */
	@Override
	public boolean startWith(@NonNull IntentStarter starter) {
		final Context context = starter.getContext();
		if (mHandlers != null && !mHandlers.isEmpty()) {
			onShowChooserDialog(starter);
			return true;
		}
		final Intent intent = build(context);
		if (isActivityForIntentAvailable(context, intent)) {
			return onStartWith(starter, intent);
		}
		notifyActivityNotFound(context);
		return false;
	}

	/**
	 * Invoked from {@link #startWith(IntentStarter)} to show a chooser dialog if there is at least
	 * one {@link ContentHandler} attached.
	 *
	 * @param starter The intent starter that may be used to access context and also to start intent
	 *                for a selected content handler from the chooser dialog.
	 */
	protected void onShowChooserDialog(@NonNull final IntentStarter starter) {
		final int n = mHandlers.size();
		final CharSequence[] providerNames = new CharSequence[n];
		for (int i = 0; i < n; i++) {
			providerNames[i] = mHandlers.get(i).name;
		}
		final AlertDialog.Builder builder = new AlertDialog.Builder(starter.getContext());
		builder.setTitle(mDialogTitle);
		builder.setItems(providerNames,
				new DialogInterface.OnClickListener() {

					/**
					 */
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final ContentHandler handler = mHandlers.get(which);
						if (handler.requestCode < 0) starter.startIntent(handler.intent);
						else starter.startIntentForResult(handler.intent, handler.requestCode);
					}
				}
		);
		builder.show();
	}

	/**
	 * @throws IllegalStateException If there is at least one {@link ContentHandler} attached.
	 */
	@NonNull
	@Override
	public Intent build(@NonNull Context context) {
		if (mHandlers != null && !mHandlers.isEmpty()) {
			throw new IllegalStateException("Cannot build intent for set of ContentHandlers.");
		}
		return super.build(context);
	}

	/**
	 */
	@Override
	protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (!mHasInputUri) {
			throw cannotBuildIntentException("No input Uri specified.");
		}
		if (TextUtils.isEmpty(mDataType)) {
			throw cannotBuildIntentException("No MIME type specified for input Uri.");
		}
	}

	/**
	 * Will be invoked only if there are no content handlers assigned to this intent builder.
	 */
	@NonNull
	@Override
	protected Intent onBuild(@NonNull Context context) {
		return new Intent(Intent.ACTION_VIEW).setDataAndType(mUri, mDataType);
	}

	/**
	 */
	@Override
	protected boolean onStartWith(@NonNull IntentStarter starter, @NonNull Intent intent) {
		return super.onStartWith(starter, Intent.createChooser(intent, mDialogTitle));
	}

	/**
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
		public ContentHandler(@NonNull CharSequence name, @NonNull Intent intent) {
			this.name = name;
			this.intent = intent;
		}

		/**
		 * Returns the name of this handler.
		 *
		 * @return This handlers's name.
		 */
		@NonNull
		public CharSequence name() {
			return name;
		}

		/**
		 * Returns the intent specified for this handler.
		 *
		 * @return This handler's intent.
		 */
		@NonNull
		public Intent intent() {
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
		 * @see #requestCode()
		 */
		public ContentHandler requestCode(int code) {
			this.requestCode = code;
			return this;
		}

		/**
		 * Returns the request code specified for this handler.
		 *
		 * @return This handler's request code.
		 * @see #requestCode(int)
		 */
		public int requestCode() {
			return requestCode;
		}
	}
}
