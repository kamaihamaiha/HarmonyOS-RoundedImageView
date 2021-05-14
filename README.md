## RoundedImageView

## Introduction

A fast ImageView (and Drawable) that supports rounded corners (and ovals or circles) based on the original example from Romain Guy.
It supports many additional features including ovals, rounded rectangles and ScaleTypes.

There are many ways to create rounded corners in openharmony, but this is the fastest and best one that I know of because it:
* does **not** create a copy of the original pixelMap
* does **not** use a clipPath which is not hardware accelerated and not anti-aliased.

### Features supported:
- Load Image from Resource folder in rectangular shape
- Load Image from Resource folder in rounded rectangular shape
- Load Image from Resource folder in oval shape
- Load Image from Color resource in oval shape
- Load Image from Color resource folder in rounded rectangular shape


## Usage Instruction

```xml
    <com.makeramen.roundedimageview.RoundedImageView
        ohos:id="$+id:one_riv"
        ohos:height="match_content"
        ohos:width="match_content"
        ohos:image_src="$media:photo1"
        ohos:orientation="vertical"
        ohos:top_padding="100vp"
        app:riv_border_width="3vp"
        app:riv_border_color="#4CAF50"
        app:riv_oval="true"
        />

```
```java

  RoundedImageView imgview = null;
    imgview = new RoundedImageView( classcontext);
    imgview.setImageResource(ResourceTable.Media_jellyfish);
    imgview.setScaleMode(Image.ScaleMode.CENTER);
```


## Installation Tutorial

```
Method 1: Generate har package from library and add it to lib folder.
       add following code to gradle of entry
       
       implementation fileTree(dir: 'libs', include: ['*.jar', '*.har'])
       
Method 2:
    allprojects{
        repositories{
            mavenCentral()
        }
    }

    implementation 'io.openharmony.tpc.thirdlib:RoundedImageView:1.0.1'
```


## License

    Copyright 2017 Vincent Mi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.