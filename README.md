[![Android Gems](http://www.android-gems.com/badge/greenfrvr/rubber-loader.svg?branch=master)](http://www.android-gems.com/lib/greenfrvr/rubber-loader)

# RubberLoaderView
### Version 1.1.2 available!

[![Join the chat at https://gitter.im/greenfrvr/rubber-loader](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/greenfrvr/rubber-loader?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RubberLoaderView-green.svg?style=flat)](https://android-arsenal.com/details/1/2489)
[![Android Gems](http://www.android-gems.com/badge/greenfrvr/rubber-loader.svg?branch=master)](http://www.android-gems.com/lib/greenfrvr/rubber-loader)

Android indeterminate loader widget with rubber shape and color animations.
Inspired by<a href="https://dribbble.com/shots/2000305-Loading-dark">
              <img alt="Dribbble" width="25" height="25" align="top"
                   src="https://d13yacurqjgara.cloudfront.net/assets/dribbble-ball-dnld-9887db49a749236ed542c4c553c4f27f.png" />
            </a>
    

Click picture below to watch it in action.

[![Demo app](https://github.com/greenfrvr/rubber-loader/blob/master/screenshots/rubber_loader_recommend.png)](http://www.youtube.com/watch?v=ixr83xFCRQ0)

## Demo
Downlaod latest demo app from Play Market:

<a href="https://play.google.com/store/apps/details?id=com.greenfrvr.rubberloader.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-AC-global-none-all-co-pr-py-PartBadges-Oct1515-1">
	<img alt="Get it on Google Play" 
				src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" 
				height=60 />
</a>

## Gradle Dependency
[ ![Download](https://api.bintray.com/packages/greenfrvr/maven/rubber-loader/images/download.svg) ](https://bintray.com/greenfrvr/maven/rubber-loader/_latestVersion)

Easily reference the library in your Android projects using this dependency in your module's build.gradle file:

```Gradle 
dependencies {
    compile 'com.github.greenfrvr:rubber-loader:1.1.2@aar'
}
```
Library available on both jCenter and Maven Central, but in case of any issues (library can't be resolved) use Bintray repo.

Add repository to your app's build.gradle file:

```Gradle
repositories {
    maven {
        url 'https://dl.bintray.com/greenfrvr/maven/'
    }
}
```
This will reference Bintray's Maven repository that contains hashtags widget directly, rather than going through jCenter first.

## Customizing
All attributes can be defined in layout .xml file or programmatically. Below is a list of available attributes.

##### Loader modes

Different loader modes has different animation behaviour. Default mode is **normal**.

```xml
	<attr name="loaderMode" format="enum">
        <enum name="normal" value="0"/>
        <enum name="equal" value="1"/>
        <enum name="centered" value="2"/>
    </attr>
```

##### Size

Currently 6 pre-defined sizes are available. Loader with ripple need more space.

| Value  | Sizes  | Sizes with ripples |
| :------------ |:---------------:|:------:|
| EXTRA_TINY     | 27dp * 12dp | 36dp * 36dp |
| TINY      | 54dp * 24dp        | 72dp * 72dp |
| SMALL (default) | 81dp * 36dp | 108dp * 108dp |
| NORMAL | 108dp * 48dp | 144dp * 144dp |
| MEDIUM  | 135dp * 60dp | 180dp * 180dp |
| LARGE | 162dp * 72dp | 216dp * 216dp |


```xml
    <attr name="loaderSize" format="enum">
        <enum name="extra_tiny" value="0"/>
        <enum name="tiny" value="1"/>
        <enum name="small" value="2"/>
        <enum name="normal" value="3"/>
        <enum name="medium" value="4"/>
        <enum name="large" value="5"/>
    </attr>
```

##### Color

Define 2 colors which will form smooth color transition.

```xml
    <attr name="loaderPrimeColor" format="color|reference"/>
    <attr name="loaderExtraColor" format="color|reference"/>
    <attr name="loaderRippleColor" format="color|reference"/>
```
If you set only `loaderPrimeColor`, its value will be set to `loaderExtraColor`, so loader will be filled with solid prime color. 

##### Ripples

Select `loaderRippleMode` to add ripple animations effect. Default mode is `none`. Loaders with ripple will have bigger sizes than without them.

```xml
	<attr name="loaderRippleMode" format="enum">
        <enum name="none" value="0"/>
        <enum name="normal" value="1"/>
        <enum name="reverse" value="2"/>
        <enum name="cycle" value="3"/>
    </attr>
```

## Usage

There are 2 methods which starts `RubberLoaderView` animation: `RubberLoaderView.startLoading()` and `RubberLoaderView.startLoading(long delay)`. To stop `RubberLoader` animation call `RubberLoaderView.stopLoading()`.

Also you can set different interpolation functions by calling `RubberLoaderView.setInterpolator(TimeInterpolator interpolator)`. It's highly recommended to use following interpolators: `PulseInterpolator`, `PulseInverseInterpolator` and `LinearInterpolator`.

##License

     Copyright 2015 greenfrvr

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
