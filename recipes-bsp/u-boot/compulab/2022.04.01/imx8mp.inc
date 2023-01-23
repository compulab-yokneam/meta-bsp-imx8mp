SRC_URI:append = " \
	file://0001-compulab-imx8mp-configs-remove-CONFIG_MICRON_4G.patch \
"

do_compile:prepend () {
    oe_runmake ${MACHINE}_defconfig
}
