DEPENDS += " \
    ${IMX_EXTRA_FIRMWARE} \
    imx-atf \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os', '', d)} \
"
 
do_configure[depends] += " \
    ${@' '.join('%s:do_deploy' % r for r in '${IMX_EXTRA_FIRMWARE}'.split() )} \
    imx-atf:do_deploy \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os:do_deploy', '', d)} \
"

ATF_MACHINE_NAME ?= "bl31-${ATF_PLATFORM}.bin"
ATF_MACHINE_NAME:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', '-optee', '', d)}"

do_configure:append () {
    for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
        ln -sf ${DEPLOY_DIR_IMAGE}/${ddr_firmware} ${B}/${config}/
    done
    ln -sf ${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME} ${B}/${config}/bl31.bin
    ln -sf ${DEPLOY_DIR_IMAGE}/tee.bin ${B}/${config}/
}

do_deploy:append () {
    if [ -f "${B}/${config}/flash.bin" ];then
        install -m 0644 ${B}/${config}/flash.bin  ${DEPLOYDIR}/flash.bin_${MACHINE}_${DRAM_CONF}
    fi
}

do_install:append () {
    if [ -f "${B}/${config}/flash.bin" ];then
        install -d ${D}/boot
        install -m 0644 ${B}/${config}/flash.bin  ${D}/boot/flash.bin_${MACHINE}_${DRAM_CONF}
    fi
}

FILES:${PN} = "/boot/flash.bin_${MACHINE}_${DRAM_CONF}"
INSANE_SKIP:${PN} += "installed-vs-shipped"
