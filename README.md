### ExDialog

自定义的 Dialog
只支持Androidx

#### get this project

**Step 1.** Add it in your root build.gradle at the end of repositories:

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.** Add the dependency

```groovy
	dependencies {
	        implementation 'com.github.hongInBlack:ExdDialog:1.0'
	}
```



#### 一、设置主题

##### 1.在App当前主题下增加

```xml
<item name="exDialogStyle">@style/ExDialogStyle</item>
```

##### 2.设置ExDialog默认属性

```xml
<style name="ExDialogStyle">
    <item name="exdTheme">@style/ExdStyle</item>
    <item name="exdPositiveTextSelector">@color/color_blue_text</item>
    <item name="exdNegativeTextSelector">@color/color_blue_text</item>
    <item name="exdPositiveBgSelector">@drawable/dialog_selector</item>
    <item name="exdNegativeBgSelector">@drawable/dialog_selector</item>
    <item name="exdListItemColor">@color/exd_black</item>
    <item name="exdSheetItemColor">#287de8</item>
    <item name="exdNegativeText">取消</item>
    <item name="exdPositiveText">确定</item>
    <item name="exdSheetCancelText">这是sheet取消</item>
</style>
```



#### 二、截图

| ![](<https://raw.githubusercontent.com/hongInBlack/ExdDialog/dev/images/Screenshot_20191216-154044_ExDialog.jpg>) | ![](<https://raw.githubusercontent.com/hongInBlack/ExdDialog/dev/images/Screenshot_20191216-154049_ExDialog.jpg>) | ![](<https://raw.githubusercontent.com/hongInBlack/ExdDialog/dev/images/Screenshot_20191216-154056_ExDialog.jpg>) |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](<https://raw.githubusercontent.com/hongInBlack/ExdDialog/dev/images/Screenshot_20191216-154102_ExDialog.jpg>) | ![](<https://raw.githubusercontent.com/hongInBlack/ExdDialog/dev/images/Screenshot_20191216-154107_ExDialog.jpg>) | ![](<https://raw.githubusercontent.com/hongInBlack/ExdDialog/dev/images/Screenshot_20191216-154118_ExDialog.jpg>) |
| ![](<https://raw.githubusercontent.com/hongInBlack/ExdDialog/dev/images/Screenshot_20191216-154124_ExDialog.jpg>) | ![](<https://raw.githubusercontent.com/hongInBlack/ExdDialog/dev/images/Screenshot_20191216-154128_ExDialog.jpg>) | ![](<https://raw.githubusercontent.com/hongInBlack/ExdDialog/dev/images/Screenshot_20191216-154134_ExDialog.jpg>) |





#### 三、用法

##### 1.标准提示

```java
new ExDialog.Builder(this)
        .autoDismiss(true)
        .title("温馨提示")
        .content("明日天气：东风有雨,明日天气：东风有雨,明日天气：东风有雨,明日天气：东风有雨,明日天气：东风有雨。")
        .onAction((dialog,isOk) -> showToast(isOk + "!"))
        .build()
        .show();
```

##### 2.只有标题

```java
new ExDialog.Builder(this)
        .autoDismiss(true)
        .title("温馨提示")
        .onAction((dialog,isOk) -> showToast(isOk + "!"))
        .build()
        .show();
```

##### 3.只有确定

```java
ExDialog.Builder.newInstance(this)
        .autoDismiss(true)
        .title("温馨提示")
        .content("明日天气：东风有雨")
        .singleAction()
        .onAction((dialog,isOk) -> showToast(isOk + "!"))
        .build()
        .show();
```

##### 4.自定义文本、颜色

```java
ExDialog.Builder.newInstance(this)
        .autoDismiss(true)
        .title("温馨提示")
        .content("明日天气：东风有雨")
        .negativeText("淡定淡定淡定淡定 淡定淡定淡定淡定 淡定淡定淡定淡定 淡定淡定淡定 淡定淡定淡定淡定 淡定淡定淡定淡定")
        .positiveText("打伞")
        .contentColorRes(R.color.exd_sheet_cancel_text)
        .negativeColor(getResources().getColor(R.color.colorPrimary))
        .positiveColor(getResources().getColor(R.color.exd_black))
        .onAction((dialog,isOk) -> showToast(isOk + "!"))
        .build()
        .show();
```

##### 5.带图标

```java
ExDialog.Builder.newInstance(this)
        .autoDismiss(true)
        .title("成功")
        .content("设备已经更新")
        .iconRes(R.drawable.icon_success)
        .singleAction()
        .onAction((dialog,isOk) -> showToast(isOk + "!"))
        .build()
        .show();
```

##### 6.输入框

```java
ExDialog.Builder.newInstance(this)
        .autoDismiss(true)
        .title("请输入密码")
        .content("长度大于等于10位")
        .inputTypeTextPassWord()
        .input("请输入密码",(dialog,input) -> {
            if (input.toString().trim().length() >= 10 || input.toString().trim().length() == 0) {
                dialog.positiveView().setEnabled(true);
            } else {
                dialog.positiveView().setEnabled(false);
            }

        })
        .onAction((dialog,isOk) -> {
            if (isOk) {
                showToast(dialog.positiveView().getText() + "!");
            } else {
                showToast("取消!");
            }
        })
        .build()
        .show();
```

##### 7.自定义

```java
new ExDialog.Builder(this)
        .autoDismiss(true)
        .negativeBg(null)
        .positiveBg(null)
        .customView(R.layout.coustom,true)
        .onAction((dialog,isOk) -> showToast(isOk + "!"))
        .build()
        .show();
```

##### 8.list

```java
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list.add("苹果");
            list.add("菠萝");
            list.add("西瓜");
        }

        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            colors.add(Color.BLACK);
        }
        colors.set(0,Color.parseColor("#DB4437"));
        colors.set(2,Color.parseColor("#86B950"));

        new ExDialog.Builder(this)
                .list(list)
                .title("水果种类")
                .content("你好，欢迎你，请您选择您喜欢吃的水果，如果觉得满意，请给五星好评！")
                .itemColors(colors)
                //                .selectPosition(0)
                .listItemTextColorRes(R.color.exd_dialog_blue)
                .onItemClick((dialog,position) -> showToast(list.get(position)))
                .build()
                .show();
```

##### 9.sheet

```java
ArrayList<String> list = new ArrayList<>();
    list.add("苹果");
    list.add("菠萝");
    list.add("西瓜");
    list.add("鸭梨");

    List<Integer> colors = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
        colors.add(Color.BLACK);
    }
    colors.set(0,Color.parseColor("#DB4437"));
    colors.set(2,Color.parseColor("#86B950"));

    new ExDialog.Builder(this)
            .sheet(list)
            .itemColors(colors)
            .onItemClick((dialog,position) -> showToast(list.get(position)))
            .build()
            .show();
```

