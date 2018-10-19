# CIS-357-HW5
Few interesting notes for this HW assignment.
1. In order to swap orientation, the line android:screenOrientation="sensor"had to be added in each activity tag in the manifest file.
2. After swapping orientation, from either landscape or portrait, data would always be reset to the default value for each unit label. To get around this, the tag android:freezesText="true" had to be added to the textview in each view.
