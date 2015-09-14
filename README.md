# RubberLoaderView

[![Android Gems](http://www.android-gems.com/badge/greenfrvr/rubber-loader.svg?branch=master)](http://www.android-gems.com/lib/greenfrvr/rubber-loader)

Android indeterminate loader widget with rubber shape and color animations.

Click picture below to watch it in action.

[![Demo app](https://github.com/greenfrvr/rubber-loader/blob/master/screenshots/rubber_loader_recommend.png)](http://www.youtube.com/watch?v=ixr83xFCRQ0)

## Demo
Downlaod latest demo app from Play Market:

<a href="https://play.google.com/store/apps/details?id=com.greenfrvr.rubberloader.sample">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>

## Gradle Dependency
[ ![Download](https://api.bintray.com/packages/greenfrvr/maven/rubber-loader/images/download.svg) ](https://bintray.com/greenfrvr/maven/rubber-loader/_latestVersion)

Easily reference the library in your Android projects using this dependency in your module's build.gradle file:

```Gradle 
dependencies {
    compile 'com.github.greenfrvr:rubber-loader:1.0.0@aar'
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

##### Size

Currently 6 pre-defined sizes are available.

| Value  | Sizes  |
| :------------ |:---------------:|
| EXTRA_TINY     | 24dp * 12dp |
| TINY      | 48dp * 24dp        |
| SMALL (default) | 72dp * 36dp |
| NORMAL | 96dp * 48dp |
| MEDIUM  | 120dp * 60dp |
| LARGE | 144dp * 72dp |


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

Define 2 colors wich will form smooth color transition.  

```xml
    <attr name="loaderPrimeColor" format="color|reference"/>
    <attr name="loaderExtraColor" format="color|reference"/>
```
If you set only `loaderPrimeColor`, its value will be set to `loaderExtraColor`, so loader will be filled with solid prime color. 

## Usage

There are 2 methods which starts `RubberLoaderView` animation: `RubberLoaderView.startLoading()` and `RubberLoaderView.startLoading(long delay)`

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
