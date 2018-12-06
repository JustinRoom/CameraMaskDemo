# CameraMask
**LatestVersion**

[ ![Download](https://api.bintray.com/packages/justinquote/maven/camera-mask/images/download.svg) ](https://bintray.com/justinquote/maven/camera-mask/_latestVersion)

<a href='https://bintray.com/justinquote/maven/camera-mask?source=watch' alt='Get automatic notifications about new "camera-mask" versions'><img src='https://www.bintray.com/docs/images/bintray_badge_color.png'></a>

camera mask library and demo


Scan QRCode to download demo application below:

![](/app/src/main/res/drawable/camera_mask_demo_qr_code.png)

### 1、implementation
+ 1.1、Gradle
```
compile 'jsc.kit.cameramask:camera-mask:_latestVersion'
```
+ 1.2、Maven
```
<dependency>
  <groupId>jsc.kit.cameramask</groupId>
  <artifactId>camera-mask</artifactId>
  <version>_latestVersion</version>
  <type>pom</type>
</dependency>
```

### 2、attrs
+ 2.1、[CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)

| 名称 | 类型 | 描述 |
|:---|:---|:---|
|`clvCameraLensSizeRatio`|float|相机镜头（或扫描框）大小占View宽度的百分比|
|`clvCameraLensWidthWeight`|string,例如:`{1.5,4}`|相机镜头（或扫描框）宽度比重|
|`clvCameraLensHeightWeight`|string,例如:`{1.5,4}`|相机镜头（或扫描框）高度比重|
|`clvCameraLensWidth`|dimension|相机镜头（或扫描框）宽度|
|`clvCameraLensHeight`|dimension|相机镜头（或扫描框）高度|
|`clvCameraLensGravity`|enum(`top`、`center`、`bottom`)|相机镜头（或扫描框）位置|
|`clvCameraLensTopMargin`|dimension|相机镜头（或扫描框）Y轴偏移量|
|`clvCameraLensShape`|enum(`rectangle`、`circular`)|相机镜头（或扫描框）形状|
|`clvCameraLens`|reference|相机镜头图片资源|
|`clvMaskColor`|color|相机镜头遮罩颜色|
|`clvBoxBorderColor`|color|扫描框边的颜色|
|`clvBoxBorderWidth`|dimension|扫描框边的粗细|
|`clvShowBoxAngle`|boolean|是否显示扫描框四个角|
|`clvBoxAngleColor`|color|扫描框四个角的颜色|
|`clvBoxAngleBorderWidth`|dimension|扫描框四个角边的粗细|
|`clvBoxAngleLength`|dimension|扫描框四个角边的长度|
|`clvText`|string|提示文字|
|`clvTextColor`|color|提示文字颜色|
|`clvTextSize`|dimension|提示文字字体大小|
|`clvTextMathParent`|boolean|提示文字是否填充View的宽度。true与View等宽，false与相机镜头（或扫描框）等宽。|
|`clvTextLocation`|enum(`belowCameraLens`、`aboveCameraLens`)|提示文字位于相机镜头（或扫描框）上方（或下方）|
|`clvTextVerticalMargin`|dimension|提示文字与相机镜头（或扫描框）的间距|
|`clvTextLeftMargin`|dimension|提示文字与View（或相机镜头或扫描框）的左间距|
|`clvTextRightMargin`|dimension|提示文字与View（或相机镜头或扫描框）的右间距|

+ 2.2、[ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java)

| 名称 | 类型 | 描述 |
|:---|:---|:---|
|`sbvSrc`|reference|扫描条图片|


+ 2.3、[CameraScannerMaskView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraScannerMaskView.java)

| 子View | 类型 | 属性 |
|:---|:---|:---|
|`cameraLensView`|[CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)|[CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)所有属性|
|`scannerBarView`|[ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java)|[ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java)所有属性|

### 3、usage
+ 3.1、[CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java):
`{1.5,4}`好比LinearLayout的`weightSum`=4, 其中相机镜头所占比重为1.5
```
    <jsc.kit.cameramask.CameraLensView
        android:id="@+id/camera_lens_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:clvCameraLensGravity="top"
        app:clvCameraLensHeight="48dp"
        app:clvCameraLensWidth="64dp"
        app:clvCameraLensWidthWeight="{1.5,4}"
        app:clvCameraLensHeightWeight="{1.5,4}"
        app:clvCameraLensShape="rectangle"
        app:clvCameraLensTopMargin="@dimen/space_32"
        app:clvShowBoxAngle="true"
        app:clvText="Put QR code inside camera lens please."
        app:clvTextLocation="belowCameraLens"
        app:clvTextMathParent="true"
        app:clvTextSize="14sp"
        app:clvTextVerticalMargin="8dp" />
```

+ 3.2、[ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java):
```
    <jsc.kit.cameramask.ScannerBarView
        android:id="@+id/scanner_view"
        android:layout_width="160dp"
        android:layout_height="160dp"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="@dimen/space_32"
        app:svbSrc="drawable图片资源"
        android:background="#f2f2f2" />
```

+ 3.3、[CameraScannerMaskView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraScannerMaskView.java):
```
    <jsc.kit.cameramask.CameraScannerMaskView
        android:id="@+id/camera_scanner_mask_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:clvCameraLensWidthWeight="{1.5, 4}"
        app:clvCameraLensHeight="96dp"
        app:clvCameraLensHeightWeight="{2, 5}"
        app:clvCameraLensTopMargin="64dp"
        app:clvText="Put QR code inside camera lens please."
        app:clvTextVerticalMargin="16dp" />
```

| 组件 | 使用示例 |
|:---|:---|
|[ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java)|[ScannerBarViewFragment](/app/src/main/java/jsc/exam/com/cameramask/fragments/ScannerBarViewFragment.java)|
|[CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)|[CameraLensViewFragment](/app/src/main/java/jsc/exam/com/cameramask/fragments/CameraLensViewFragment.java)|
|[CameraScannerMaskView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraScannerMaskView.java)|[CameraScannerMaskViewFragment](/app/src/main/java/jsc/exam/com/cameramask/fragments/CameraScannerMaskViewFragment.java)|

### 4、Screenshots
+ 4.1、[ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java)

![ScannerBarView](/output/shots/scanner_bar_view_s.png)

+ 4.2、[CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)

![CameraLensView:picture](/output/shots/camera_lens_view_pic_s.png)
![CameraLensView:circle-shape](/output/shots/camera_lens_view_circle_s.png)
![CameraLensView:square-shape](/output/shots/camera_lens_view_square_s.png)
![CameraLensView:crop-bitmap](/output/shots/camera_lens_view_bitmap_s.png)

+ 4.3、[CameraScannerMaskView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraScannerMaskView.java)

![CameraScannerMaskView](/output/shots/camera_scanner_mask_view_s.png)

### 5、release log

##### version:0.3.0
+ 1、fix bugs
+ 2、optimize CameraLensView, add attrs:  
clvCameraLensWidthWeight：相机镜头宽度比重,例如:{1.5,4}  
clvCameraLensHeightWeight：相机镜头高度比重,例如:`{5,2}

##### version:0.2.1
+ 1、optimize [CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java), add attrs:  
clvCameraLensWidth：相机镜头（或扫描框）宽度  
clvCameraLensHeight：相机镜头（或扫描框）高  
clvCameraLensGravity(`top`、`center`、`bottom`)：相机镜头（或扫描框）位置  
clvShowBoxAngle：是否显示扫描框四个角  


##### version:0.1.2
+ 1、optimize [CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)

##### version:0.1.1
+ 1、add method in [CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)、[CameraScannerMaskView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraScannerMaskView.java):  
`Bitmap cropCameraLensRectBitmap(Bitmap src, boolean withRatio)`

### LICENSE
```
   Copyright 2018 JustinRoom

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
