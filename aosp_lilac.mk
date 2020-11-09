$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

WITH_VOLTE := false
IS_PE := true

# Inherit device configuration
$(call inherit-product, device/sony/lilac/device.mk)

# Product API level
$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_o.mk)

### BOOTANIMATION
TARGET_SCREEN_HEIGHT := 1280
TARGET_SCREEN_WIDTH := 720
TARGET_BOOT_ANIMATION_RES := 720
TARGET_BOOTANIMATION_HALF_RES := true

### Havoc Stuffs
TARGET_GAPPS_ARCH := arm64
TARGET_INCLUDE_STOCK_ARCORE := true
TARGET_SUPPORTS_PIXEL_WALLPAPER := true
TARGET_SUPPORTS_GOOGLE_RECORDER := true
IS_PHONE := true
WITH_GAPPS := true

### FaceUnlockService
TARGET_DISABLE_ALTERNATIVE_FACE_UNLOCK := false

### PE
$(call inherit-product, vendor/aosp/config/common_full_phone.mk)

## Device identifier. This must come after all inclusions
PRODUCT_NAME := aosp_lilac
PRODUCT_DEVICE := lilac
PRODUCT_BRAND := Sony
PRODUCT_MODEL := G8441
PRODUCT_MANUFACTURER := Sony
BUILD_VERSION_TAGS := release-keys

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_NAME=lilac \
    PRIVATE_BUILD_DESC="redfin-user 11 RD1A.201105.003.A1 6886512 release-keys" \
    BUILD_NUMBER=6886512

# Pixel 5 November fingerprint
BUILD_FINGERPRINT := google/redfin/redfin:11/RD1A.201105.003.A1/6886512:user/release-keys
#BUILD_FINGERPRINT := Sony/G8441/G8441:9/47.2.A.11.228/3311891731:user/release-keys

PRODUCT_GMS_CLIENTID_BASE := android-sony-mobile
