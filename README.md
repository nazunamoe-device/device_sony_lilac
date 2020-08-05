Device configuration for Sony Xperia XZ1 Compact (lilac)
========================================================

Description
-----------

This repository is for Havoc OS on Sony Xperia XZ1 Compact (lilac).

How to build Havoc OS
----------------------

* Make a workspace:

        mkdir -p ~/havoc
        cd ~/havoc

* Initialize the repo:

        repo init -u https://github.com/Havoc-OS/android_manifest.git -b ten

* Create a local manifest:

        vim .repo/local_manifests/roomservice.xml

        <?xml version="1.0" encoding="UTF-8"?>
        <manifest>
            <remote name="git" fetch="https://github.com/" />

            <!-- SONY -->
            <project name="whatawurst/android_kernel_sony_msm8998" path="kernel/sony/msm8998" remote="git" revision="lineage-17.1" />
            <project name="shank03/android_device_sony_common-treble" path="device/sony/common-treble" remote="git" revision="havoc-10" />
            <project name="shank03/android_device_sony_yoshino" path="device/sony/yoshino" remote="git" revision="havoc-10" />
            <project name="shank03/android_device_sony_lilac" path="device/sony/lilac" remote="git" revision="havoc-10" />

            <!-- Pinned blobs for lilac -->
            <project name="shank03/android_vendor_sony_lilac" path="vendor/sony/lilac" remote="git" revision="master" />

            <project name="shank03/vendor_google-customization" path="vendor/google-customization" remote="git" revision="ten" />
        </manifest>

* Sync the repo:

        repo sync -c -j6 --no-clone-bundle -no-tags

* No need to extract vendor blobs, roomservice.xml will do it for us.

* Setup the environment

        . build/envsetup.sh
        lunch havoc_lilac-userdebug

* Build LineageOS

        mka -j8 bacon
