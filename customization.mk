# Copyright (C) 2020 Shashank Verma
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

CUST_PATH := device/sony/lilac

# Camera Configuration
PRODUCT_COPY_FILES += \
    $(CUST_PATH)/vendor/etc/camera/camera_config.xml:$(TARGET_COPY_OUT_VENDOR)/etc/camera/camera_config.xml \
    $(CUST_PATH)/vendor/etc/camera/imx219_chromatix.xml:$(TARGET_COPY_OUT_VENDOR)/etc/camera/imx219_chromatix.xml \
    $(CUST_PATH)/vendor/etc/camera/imx400_chromatix.xml:$(TARGET_COPY_OUT_VENDOR)/etc/camera/imx400_chromatix.xml

PRODUCT_COPY_FILES += \
    $(CUST_PATH)/vendor/etc/permissions/android.hardware.camera.autofocus.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.autofocus.xml \
    $(CUST_PATH)/vendor/etc/permissions/android.hardware.camera.external.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.external.xml \
    $(CUST_PATH)/vendor/etc/permissions/android.hardware.camera.manual_postprocessing.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.manual_postprocessing.xml \
    $(CUST_PATH)/vendor/etc/permissions/android.hardware.camera.manual_sensor.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.manual_sensor.xml \
    $(CUST_PATH)/vendor/etc/permissions/android.hardware.camera.raw.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.raw.xml \
    $(CUST_PATH)/vendor/etc/permissions/android.hardware.camera.full.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.full.xml \
    $(CUST_PATH)/vendor/etc/permissions/android.hardware.camera.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.xml

# AR config for Play Store
PRODUCT_COPY_FILES += \
    $(CUST_PATH)/vendor/etc/permissions/android.hardware.camera.ar.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.camera.ar.xml

# APNs Conf
#PRODUCT_COPY_FILES += \
	$(CUST_PATH)/vendor/etc/apns-conf.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/apns-conf.xml

# Limit dex2oat threads to improve thermals
PRODUCT_PROPERTY_OVERRIDES += \
    dalvik.vm.dex2oat-threads=2 \
    dalvik.vm.image-dex2oat-threads=4

PRODUCT_PROPERTY_OVERRIDES += \
    ro.build.selinux=1 \
    ro.opa.eligible_device=true

#VoLTE
ifeq ($(WITH_VOLTE),true)
$(warning ************* WITH_VOLTE is Enabled ***************************)
PRODUCT_PROPERTY_OVERRIDES += \
    persist.dbg.volte_avail_ovr=1 \
    persist.dbg.vt_avail_ovr=1 \
    persist.dbg.wfc_avail_ovr=1 \
    persist.vendor.ims.dropset_feature=0 \
    persist.vendor.radio.add_power_save=1 \
    persist.vendor.radio.force_on_dc=true \
    persist.radio.custom_ecc=1 \
    persist.radio.data_con_rprt=1 \
    persist.radio.data_ltd_sys_ind=1 \
    persist.radio.ignore_dom_time=10 \
    persist.radio.rat_on=combine \
    persist.radio.sib16_support=1 \
    persist.radio.RATE_ADAPT_ENABLE=1 \
    persist.radio.ROTATION_ENABLE=1 \
    persist.radio.VT_ENABLE=1 \
    persist.radio.VT_HYBRID_ENABLE=1 \
    persist.radio.is_wps_enabled=true \
    persist.radio.videopause.mode=1 \
    persist.radio.sap_silent_pin=1 \
    persist.radio.always_send_plmn=true \
    persist.rcs.supported=0 \
    persist.dbg.ims_volte_enable=1 \
    persist.vendor.radio.is_voip_enabled=1 \
    persist.vendor.radio.rat_on=combine \
    persist.vendor.radio.voice_on_lte=1 \
    persist.vendor.radio.calls.on.ims=1 \
    persist.radio.calls.on.ims=1
endif

# Camera
#include device/sony/lilac/camera/camera.mk

# Apex
ifeq ($(IS_PE),false)
TARGET_FLATTEN_APEX := false
PRODUCT_PACKAGES += \
    com.android.apex.cts.shim.v1_prebuilt \
    com.android.conscrypt \
    com.android.media \
    com.android.media.swcodec\
    com.android.resolv \
    com.android.tzdata

include vendor/google-customization/config.mk
endif

PRODUCT_PROPERTY_OVERRIDES += \
    persist.camera.mobicat=2 \
    persist.camera.stats.debugexif=3080192 \
    persist.ts.rtmakeup=false \
    persist.vendor.camera.tintless.skip=1

# Use Vulkan for UI rendering
#PRODUCT_PROPERTY_OVERRIDES += debug.hwui.renderer=skiavk

# USB debugging at boot
# Do not enable if build type is user
ifneq ($(TARGET_BUILD_VARIANT),user)
PRODUCT_PROPERTY_OVERRIDES += \
    persist.sys.usb.config=mtp,adb \
    ro.adb.secure=0 \
    ro.secure=0 \
    ro.debuggable=1
endif

# Packages
PRODUCT_PACKAGES += \
    PixelLiveWallpaperPrebuilt \
    libshim_camera \
    VendorSupport-preference

include device/sony/lilac/prebuilts/prebuilts.mk

# IMS
PRODUCT_PACKAGES += \
    ims-ext-common_system \
    ims-ext-common \
    telephony-ext \
    qti-telephony-utils \
    qti_telephony_utils.xml \
    qti-telephony-hidl-wrapper \
    qti_telephony_hidl_wrapper.xml \
    qtiImsInCallUi \
    ConfURIDialer

# CNE
PRODUCT_PACKAGES += \
    cneapiclient \
    com.quicinc.cne \
    services-ext

# LIGHTS
PRODUCT_PACKAGES += lights.$(TARGET_DEVICE)

# Voice Processing
PRODUCT_PACKAGES += libqcomvoiceprocessingdescriptors

# External exFat tools
PRODUCT_PACKAGES += \
    mkfs.exfat \
    fsck.exfat

# Charger
# Health (in addition to own board libhealth)
#PRODUCT_PACKAGES += libhealthd.$(TARGET_DEVICE)

# BatteryCare
PRODUCT_PACKAGES += \
    BatteryCare \
    BatteryCareImpl
