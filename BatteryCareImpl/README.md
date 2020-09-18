### Coordination of BatteryCare Apps

<b>APP:</b> `BatteryCare`&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<b>APP:</b> `BatteryCareImpl` <br>
<b>PKG:</b> `com.xperia.battery_care`&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;  <b>PKG:</b> `com.xperia.battery_care_impl` <br>
<b>USE:</b> UI and Preference&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<b>USE:</b> Main system service and function
<br>
<br>
#### On Boot
- `BatteryCareImpl` starts the main `BatteryCare.java` service.
- `BatteryCareImpl` send it's stored preference through <b>broadcast</b> `com.xperia.battery_care.SYNC_PREF` to `BatteryCare`.
- <b>On receive</b> `BatteryCare` save preferences from `extras` from `intent`.
<br>

#### When user change preference in `BatteryCare`
- `BatteryCare` send the new preference through <b>broadcast</b> `com.xperia.battery_care.UPDATE_PREFERENCE` to `BatteryCareImpl`.
- `BatteryCareImpl` stores the new preference <b>on receive</b>.
