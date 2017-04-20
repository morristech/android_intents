Change-Log
===============

### [Release 1.0.2](https://github.com/universum-studios/android_intents/releases/tag/1.0.2) ###
> 19.04.2017

- Deprecated `IntentsConfig` class as it was not used across the library.
- Default type of `CalendarIntent` has been changed from `CalendarIntent.TYPE_INSERT_EVENT` to 
  `CalendarIntent.TYPE_VIEW` so `new CalendarIntent().startWith(...)` will simply open a **Calendar**
  application.

### [Release 1.0.1](https://github.com/universum-studios/android_intents/releases/tag/1.0.1) ###
> 04.03.2017

- Fixed `ImageIntent.processResultIntent(...)` to properly decode image uri stream.
- Code quality improvements.

### [Release 1.0.0](https://github.com/universum-studios/android_intents/releases/tag/1.0.0) ###
> 15.12.2016

- First production release.