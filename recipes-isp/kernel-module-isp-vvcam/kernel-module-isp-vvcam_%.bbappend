FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-isp-vvcam-add-IMX219-sensor-driver.patch \
    file://0002-isp-vvcam-add-OV5647-sensor-driver.patch \
    file://0003-isp-vvcam-retry-OV5647-chip-ID-read.patch \
"
