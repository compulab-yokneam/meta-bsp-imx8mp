FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-isp-imx-add-IMX219-camera-support.patch \
    file://0002-isp-imx-add-OV5647-camera-support.patch \
"

FILES_SOLIBS_VERSIONED += " \
    ${libdir}/libimx219.so \
    ${libdir}/libov5647.so \
"
