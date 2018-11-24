# CameraMask
**LatestVersion**

[ ![Download](https://api.bintray.com/packages/justinquote/maven/camera-mask/images/download.svg) ](https://bintray.com/justinquote/maven/camera-mask/_latestVersion)

<a href='https://bintray.com/justinquote/maven/camera-mask?source=watch' alt='Get automatic notifications about new "camera-mask" versions'><img src='https://www.bintray.com/docs/images/bintray_badge_color.png'></a>

camera mask library and demo


Scan QRCode to download demo application below:

![](/output/camera_mask_demo_qr_code.png)

### implementation
+ Gradle
```
compile 'jsc.kit.cameramask:camera-mask:_latestVersion'
```
+ Maven
```
<dependency>
  <groupId>jsc.kit.cameramask</groupId>
  <artifactId>camera-mask</artifactId>
  <version>_latestVersion</version>
  <type>pom</type>
</dependency>
```

### attrs
+ [CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)

| 名称 | 类型 | 描述 | 默认值 |
|:---:|:---|:---|:---|
|clvCameraLensSizeRatio|float|相机镜头（或扫描框）大小占View宽度的百分比||
|clvCameraLensTopMargin|dimension|相机镜头（或扫描框）与顶部的间距||
|clvCameraLensShape|枚举(`square`、`circular`)|相机镜头（或扫描框）形状||
|clvCameraLens|reference|相机镜头图片资源||
|clvMaskColor|color|相机镜头遮罩颜色||
|clvBoxBorderColor|color|扫描框边的颜色||
|clvBoxBorderWidth|dimension|扫描框边的粗细||
|clvBoxAngleColor|color|扫描框四个角的颜色||
|clvBoxAngleBorderWidth|dimension|扫描框四个角边的粗细||
|clvBoxAngleLength|dimension|扫描框四个角边的长度||
|clvText|string|提示文字||
|clvTextColor|color|提示文字颜色||
|clvTextSize|dimension|提示文字字体大小||
|clvTextMathParent|boolean|提示文字是否填充View的宽度。true与View等宽，false与相机镜头（或扫描框）等宽。||
|clvTextLocation|枚举(`belowCameraLens`、`aboveCameraLens`)|提示文字位于相机镜头（或扫描框）上方（或下方）||
|clvTextVerticalMargin|dimension|提示文字与相机镜头（或扫描框）的间距||
|clvTextLeftMargin|dimension|提示文字与View（或相机镜头或扫描框）的左间距||
|clvTextRightMargin|dimension|提示文字与View（或相机镜头或扫描框）的右间距||

+ [ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java)

| 名称 | 类型 | 描述 | 默认值 |
|:---:|:---|:---|:---|
|sbvSrc|reference|扫描条图片|[camera_mask_scanner_bar](/cameraMaskLibrary/src/main/res/drawable/camera_mask_scanner_bar.png)|


+ [CameraScannerMaskView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraScannerMaskView.java)

| 子View | 类型 | 属性 |
|:---:|:---|:---|
|cameraLensView|[CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)|[CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)所有属性|
|scannerBarView|[ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java)|[ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java)所有属性|



### Screenshots
+ [ScannerBarView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/ScannerBarView.java)

![ScannerBarView](/output/shots/scanner_bar_view_s.png)

+ [CameraLensView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraLensView.java)

![CameraLensView](/output/shots/camera_lens_view_s.png)

+ [CameraScannerMaskView](/cameraMaskLibrary/src/main/java/jsc/kit/cameramask/CameraScannerMaskView.java)

![CameraScannerMaskView](/output/shots/camera_scanner_mask_view_s.png)

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
