FILESEXTRAPATHS:prepend := "${THISDIR}/tools:${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-isp-imx-add-IMX219-camera-support.patch \
    file://0002-isp-imx-add-OV5647-camera-support.patch \
    file://0003-isp-imx-support-IMX219-and-OV5647-on-CSI2.patch \
    file://0004-isp-imx-support-mixed-IMX219-and-OV5647-cameras.patch \
    file://isp-camera-devices.sh \
"

RDEPENDS:${PN} += "gstreamer1.0 v4l-utils"

FILES_SOLIBS_VERSIONED += " \
    ${libdir}/libimx219.so \
    ${libdir}/libov5647.so \
"

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/isp-camera-devices.sh \
        ${D}${bindir}/isp-camera-devices
}
