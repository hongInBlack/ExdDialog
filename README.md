### ExDialog

自定义 的Dialog

#### 一、设置主题

##### 1.在当前主题下增加 

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

#### 二、用法

##### 1.标准提示

```java
new ExDialog.Builder(this)
        .autoDismiss(true)
        .title("温馨提示")
        .content("明日天气：东风有雨,明日天气：东风有雨,明日天气：东风有雨。")
        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
        .build()
        .show();
```

##### 2.只有标题

```java
new ExDialog.Builder(this)
        .autoDismiss(true)
        .title("温馨提示")
        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
        .build()
        .show();
```

##### 3.只有确定

```java
new ExDialog.Builder(this)
        .autoDismiss(true)
        .title("温馨提示")
        .content("明日天气：东风有雨")
        .singleAction()
        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
        .build()
        .show();
```

##### 4.自定义文本、颜色

```java
new ExDialog.Builder(this)
        .autoDismiss(true)
        .title("温馨提示")
        .content("明日天气：东风有雨")
        .negativeText("淡定")
        .positiveText("打伞")
        .contentColorRes(R.color.exd_sheet_cancel_text)
        .negativeColor(getResources().getColor(R.color.colorPrimary))
        .positiveColor(getResources().getColor(R.color.exd_black))
        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
        .build()
        .show();
```

##### 5.带图标

```java
new ExDialog.Builder(this)
        .autoDismiss(true)
        .title("成功")
        .content("设备已经更新")
        .iconRes(R.drawable.icon_success)
        .singleAction()
        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
        .build()
        .show();
```

##### 6.输入框

```java
new ExDialog.Builder(this)
        .autoDismiss(true)
        .title("请输入密码")
        .content("长度大于等于10位")
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
        .customView(R.layout.coustom,true)
        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
        .build()
        .show();
```

##### 8.list

```java
ArrayList<String> list = new ArrayList<>();
for (int i = 0; i < 6; i++) {
    list.add("苹果");
    list.add("菠萝");
    list.add("西瓜");
    list.add("鸭梨");
}

new ExDialog.Builder(this)
        .list(list)
        .title("水果种类")
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

new ExDialog.Builder(this)
        .sheet(list)
        .onItemClick((dialog,position) -> showToast(list.get(position)))
        .build()
        .show();
```

