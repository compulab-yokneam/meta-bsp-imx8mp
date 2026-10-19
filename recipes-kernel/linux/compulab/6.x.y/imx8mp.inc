SRC_URI:append = " \
	file://linux-headers.cfg \
	file://linux-firmware-sdma.cfg \
	file://sdma.cfg \
"

FIRMWARE_DIR_SOURCE = "${RECIPE_SYSROOT}${nonarch_base_libdir}/firmware/imx/sdma"
FIRMWARE_FILE_NAME = "sdma-imx7d.bin"

do_configure[depends] += " \
    firmware-imx:do_populate_sysroot \
"

# Use the firmware-imx binary in normal and externalsrc builds, which skip do_patch.
# If unavailable, let the kernel unpack its in-tree firmware archive.
do_configure:prepend() {
    FW_FILE="${FIRMWARE_DIR_SOURCE}/${FIRMWARE_FILE_NAME}"
    if [ -f "${FW_FILE}" ]; then
        install -D -m 0644 "${FW_FILE}" "${S}/lib/firmware/imx/sdma/${FIRMWARE_FILE_NAME}"
    fi
}
