SRC_URI:append = " \
	file://linux-headers.cfg \
	file://linux-firmware-sdma.cfg \
	file://sdma.cfg \
"

FIRMWARE_DIR_SOURCE = "${WORKDIR}/recipe-sysroot"
FIRMWARE_FILE_NAME = "sdma-imx7d.bin"

do_patch[depends] += " \
    firmware-imx:do_populate_sysroot \
"

do_patch:append() {
    FW_FILE=$(find ${FIRMWARE_DIR_SOURCE} -name ${FIRMWARE_FILE_NAME})
    mkdir -p ${S}/lib/firmware/imx/sdma
    cp ${FW_FILE} ${S}/lib/firmware/imx/sdma
}
