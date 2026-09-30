FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://0001-isp-imx-add-IMX219-camera-support.patch"

FILES_SOLIBS_VERSIONED += "${libdir}/libimx219.so"
