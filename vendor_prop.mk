# Disable HDR
PRODUCT_PROPERTY_OVERRIDES += \
    vendor.display.hwc_disable_hdr=1

# Radio
PRODUCT_PROPERTY_OVERRIDES += \
    persist.vendor.radio.block_allow_data=1

# Apex
PRODUCT_PROPERTY_OVERRIDES += ro.apex.updatable=true

# Assistant
PRODUCT_PROPERTY_OVERRIDES += ro.opa.eligible_device=true

# Camera Test
PRODUCT_PROPERTY_OVERRIDES += \
    persist.vendor.camera.awb.sync=2 \
    persist.vendor.camera.expose.aux=1 \
    persist.vendor.camera.imglib.usefdlite=1 \
    vendor.camera.aux.packagelist=org.codeaurora.snapcam \
    vendor.camera.hal1.packagelist=com.skype.raider,com.google.android.talk \
    persist.vendor.camera.tnr.cds=1 \
    persist.vendor.process.plates=1 \
    persist.vendor.process.plates=1 \
    persist.vendor.camera.eis.enable=1 \
    persist.vendor.camera.HAL3.enabled=1 \
    persist.vendor.camera.is_type=5 \
    persist.vendor.camera.is_mode=5 \
    persist.vendor.camera.max.previewfps=60 \
    persist.vendor.camera.tnr_cds=1 \
    persist.vendor.camera.tnr.video=1 \
    persist.vendor.camera.tnr.preview=1 \
    persist.vendor.camera.tnr.snapshot=1 \
    persist.vendor.camera.llnoise=1 \
    persist.vendor.tnr.process.plates=2 \
    persist.vendor.denoise.process.plates=2 \
    vendor.camera.lowpower.record.enable=1 \
    persist.vendor.camera.rdi.mode=1 \
    persist.vendor.camera.video.CDS=1
