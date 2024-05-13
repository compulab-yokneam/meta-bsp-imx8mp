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
    ln -sf ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME} ${B}/${config}/bl31.bin
    ln -sf ${DEPLOY_DIR_IMAGE}/tee.bin ${B}/${config}/
}

do_deploy:append () {
    if [ -f "${B}/${config}/flash.bin" ];then
        install -m 0777 ${B}/${config}/flash.bin  ${DEPLOYDIR}/flash.bin-${MACHINE}_${DRAM_CONF}
    fi
}
