FILESEXTRAPATHS_prepend := "${THISDIR}/compulab/imx8mp:"

include compulab/imx8mp.inc

do_configure_append() {
    if [ -z ${SKIP_MACHINE_DEFCONFIG} ];then
        oe_runmake ${MACHINE}_defconfig
    fi
}

do_compile_prepend() {
    if [ ${BUILD_REPRODUCIBLE_BINARIES} -eq 1 ];then
        export SOURCE_DATE_EPOCH=$(date +%s)
    fi
}

COMPATIBLE_MACHINE = "(ucm-imx8m-plus|som-imx8m-plus)"