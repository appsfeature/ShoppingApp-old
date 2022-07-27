# DynamicApps

#### Support dynamic app builder with handle contents are: Link, HTML Viewer, PDF Viewer and Video Player.

#### Library size is : Kb






## Admin Panel Usage

### Videos ItemType Setup

```
    Title           : Video Title
    Description     : Video Description
    Link            : Video Id
    OtherProperties : {"isPlayerStyleMinimal":true}
```
#### Note:Add Youtube VideoId in Content Link Box.


### PDF ItemType Setup

```
    Title           : PDF Title
    Link            : file url / file name with extension.
```
#### Note:Pass file url if base url not configured in app side.


### HTML Viewer ItemType Setup

```
    Title           : Viewer Title
    Description     : HTML Body
```
#### Note:Html body contains the data of under Html body tag i.e. <body> Html body </body>


### Link ItemType Setup

```
    Title           : Website Title
    Link            : Website Link
```

## Adapter style other properties

```json
{
  "grid_count": 3,
  "grid_auto_adjust": true,
  "random_bg_color": true,
  "random_icon_color": true,
  "hide_title": true,
  "width": 200,
  "height": 400,
  "text_size": 16,
  "is_portrait": true,
  "scroll_speed": 5000,
  "is_enable_auto_scroll": true,
  "isPlayerStyleMinimal": true,
  "padding":{"left":200,"right":200,"top":100,"bottom":100},
  "is_remove_card": true,
  "is_order_by_asc": true,
  "is_browser_chrome": true
}
```
```
{"grid_count":3}
{"grid_auto_adjust":true}
{"random_bg_color":true}
{"random_icon_color":true}
{"hide_title":true}
{"width":200}           // use for image resize
{"height":200}          // use for image resize
{"text_size": 16}       // use for text resize
{"is_remove_card": true}// remove child card view
{"is_portrait":true}               // use for horizontal slider
{"scroll_speed": 5000}             // use for horizontal slider
{"is_enable_auto_scroll": true}    // use for horizontal slider
{"isPlayerStyleMinimal": true}     // use for change youtube player style
{"padding":{"left":200,"right":200,"top":100,"bottom":100}} // image view padding
{"is_order_by_asc": true}           // change list order.
{"is_browser_chrome": true}         // open link in-app chrome browser.
```
#### Note: Add these other property in parent category.
