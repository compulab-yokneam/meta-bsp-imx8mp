FILESEXTRAPATHS:prepend := "${THISDIR}/compulab/${PV}/imx8mp:"

require compulab/${PV}/imx8mp.inc

do_configure:append() {
    if [ -z ${SKIP_MACHINE_DEFCONFIG} ];then
        oe_runmake ${MACHINE}_defconfig
    fi
}

do_compile:prepend() {
    if [ ${BUILD_REPRODUCIBLE_BINARIES} -eq 1 ];then
        export SOURCE_DATE_EPOCH=$(date +%s)
    fi
}

COMPATIBLE_MACHINE = "(ucm-imx8m-plus|som-imx8m-plus)"
