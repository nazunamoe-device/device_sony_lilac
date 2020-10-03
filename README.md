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

        git clone https://github.com/shank03/local_manifests.git -b ten .repo/local_manifests

* Sync the repo:

        repo sync -c -j6 --no-clone-bundle --no-tags

* No need to extract vendor blobs, roomservice.xml will do it for us.

* Setup the environment

        . build/envsetup.sh
        lunch havoc_lilac-userdebug

* Build LineageOS

        mka -j8 bacon
