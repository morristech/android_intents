Change-Log
===============
> Regular configuration update: _01.06.2018_

More **detailed changelog** for each respective version may be viewed by pressing on a desired _version's name_.

## Version 1.x ##

### [1.1.0](https://github.com/universum-studios/android_intents/releases/tag/v1.1.0) ###
> 15.06.2018

- Small updates and improvements.

### [1.0.5](https://github.com/universum-studios/android_intents/releases/tag/v1.0.5) ###
> 27.11.2017

- Removed `components` and `methods` **deprecated** in previous versions.
- Updated **dependencies** versions and _Gradle_ configuration.

### [1.0.4](https://github.com/universum-studios/android_intents/releases/tag/v1.0.4) ###
> 09.08.2017

- **Deprecated** `packageName` attribute of `PlayIntent` and added **better named** alternative `applicationId`.

### [1.0.3](https://github.com/universum-studios/android_intents/releases/tag/v1.0.3) ###
> 29.04.2017

- Removed **deprecated** `IntentsConfig` class.

### [1.0.2](https://github.com/universum-studios/android_intents/releases/tag/v1.0.2) ###
> 19.04.2017

- Default type of `CalendarIntent` has been changed from `CalendarIntent.TYPE_INSERT_EVENT` to 
  `CalendarIntent.TYPE_VIEW` so `new CalendarIntent().startWith(...)` will simply open a **Calendar**
  application.

### [1.0.1](https://github.com/universum-studios/android_intents/releases/tag/v1.0.1) ###
> 04.03.2017

- Code quality improvements.

### [1.0.0](https://github.com/universum-studios/android_intents/releases/tag/v1.0.0) ###
> 15.12.2016

- First production release.