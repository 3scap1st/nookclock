# Clock for Nook Simple Touch

### 
I needed a customizable clock, and had an unused old ebook. This app shows current time, date, battery charge, indoor temperature and prevents lockscreen from being turned on.

### Usage tips:
For unknown reasons building an app for Android 2.1 in modern Android Studio leads to resource error crash after install, so I had to code it without using xml files. Be aware that an app is meant for 800x600 album layout, so if you want to use your android based ebook as a clock you'll probably have to tweak font size in MainActivity.java. To display temperature I am using the content of /sys/devices/platform/omap3epfb.0/graphics/fb0/epd_temp — location can also depend from your device (if you don't need that, just comment out related lines).

### How it looks:
![E-ink Clock](https://tinystash.undef.im/il/dtxcBEd6TMHUmvPpuXNRn7uhoxVqibHKTpcai5B1HTgbptPcgeKDGPnfmu4t9JYPmFM32S4Qt4xnN1yUgTGvz7fLANMZwr2v99ttcwUAEsmAA.jpg)

### Special thanks:
[Mikel](https://t.me/mikelupdates) for his [retro android development guide](https://medium.com/@mekal.contact/how-to-develop-and-backport-apps-for-android-2-1-in-2022-f880a66df702).
