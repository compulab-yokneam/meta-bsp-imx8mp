FILESEXTRAPATHS:prepend := "${THISDIR}/compulab/imx8mp:"

require compulab/imx8mp.inc

do_compile:prepend() {
    if [ ${BUILD_REPRODUCIBLE_BINARIES} -eq 1 ];then
        export SOURCE_DATE_EPOCH=$(date +%s)
    fi
}
